package com.app.bayes3.poisearch;

import java.util.List;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.amap.mapapi.core.AMapException;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.core.PoiItem;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.PoiOverlay;
import com.amap.mapapi.poisearch.PoiPagedResult;
import com.amap.mapapi.poisearch.PoiSearch;
import com.amap.mapapi.poisearch.PoiSearch.SearchBound;
import com.amap.mapapi.poisearch.PoiTypeDef;
import com.app.bayes3.R;
import com.app.bayes3.util.Utils;

public class MyPoiSearch extends MapActivity{
	
	private Context mContext = null;
	
	private MapView mMapView;
	private MapController mMapController;
	private GeoPoint mMapCenter;
	
	private EditText searchText;
	private PoiPagedResult result;
	private ProgressDialog progDialog = null;
	private PoiOverlay poiOverlay;
	private Button search;
	private int buttonStyle = 0;     //0��ʾ��ťΪ������ť  1��ʾ��ťΪ��һҳ��ť
	private int curpage = 1;
	@SuppressWarnings("unused")
	private int cnt = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_search);
		mContext = this;
		mMapView = (MapView) findViewById(R.id.poisearch_MapView);
		//�����������õ����ſؼ�
		// �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
		mMapController = mMapView.getController();  
		//�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢�� (�� * 1E6)
		mMapCenter = new GeoPoint((int) (30.524118 * 1E6),(int) (114.335489 *  1E6));  
		//���õ�ͼ���ĵ�
		mMapController.setCenter(mMapCenter);  
		//���õ�ͼzoom����
		mMapController.setZoom(14); 
		searchText = (EditText) findViewById(R.id.TextViewSearch);
//		searchText.isFocused();
		searchText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//�������ı��仯ʱ �޸İ�ť����ʾ��Ϊ�������� ��ť���͸�Ϊ0
				search.setText("����");
				buttonStyle = 0 ;
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		
		search = (Button)findViewById(R.id.next);
		search.setOnClickListener(new View.OnClickListener(){
			public void onClick(View arg0) {
				switch(buttonStyle){
					//do poi search
					case 0:doPOI_Search("027");break;
					//show next page result
					case 1:handler.sendMessage(Message.obtain(handler,Utils.POISEARCH_NEXT));break;
				}
			}
		});
		
		progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(true);
		
		
	}
	
	private void doPOI_Search(String cityCode){
//		searchText.isFocused();
		//����û�������
		String user_search = searchText.getText().toString().trim();
		progDialog.setMessage("��������:\n" + user_search);
		progDialog.show();
		//�޸İ�ť����Ϊ "��һҳ"
		buttonStyle = 1;
		search.setText("��һҳ");
		curpage = 1;
		cnt = 0;
		try {
			PoiSearch poiSearch = new PoiSearch(MyPoiSearch.this,
					// ���������ַ�����027Ϊ�������ţ���Ҫ��ȡ��ǰ���е����ſ��ڳ�������ʱ��λ��ȡ��
					new PoiSearch.Query(user_search, PoiTypeDef.All, cityCode)); 
			//�ڵ�ǰ��ͼ��ʾ��Χ�ڲ���
			poiSearch.setBound(new SearchBound(mMapView));
			//��������ÿ����෵�ؽ����
			poiSearch.setPageSize(10);
			result = poiSearch.searchPOI();
			if(result != null) {
				cnt = result.getPageCount();
			}
			handler.sendMessage(Message.obtain(handler,Utils.POISEARCH));
		} catch (AMapException e) {
			handler.sendMessage(Message.obtain(handler,Utils.ERROR));
			e.printStackTrace();
		}
	}
	
	
	public void showToast(String showString) {
		Toast.makeText(mContext, showString, Toast.LENGTH_SHORT).show();
	}
	
	//�������ؼ�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.FLAG_CANCELED && event.getRepeatCount() == 0){
			this.finish();
			overridePendingTransition(R.anim.zoom_exit, R.anim.zoom_enter);
		}
		return super.onKeyDown(keyCode, event);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == Utils.POISEARCH) {
				progDialog.dismiss();
				try {
					if (result != null) {
						List<PoiItem> poiItems = result.getPage(1);
						if (poiItems != null && poiItems.size() > 0) {
							mMapController.animateTo(poiItems.get(0).getPoint());
							if (poiOverlay != null) {
								poiOverlay.removeFromMap();
							}
							Drawable drawable = getResources().getDrawable(R.drawable.da_marker_red);
							// ������ĵ�һҳ��ӵ�PoiOverlay
							poiOverlay = new PoiOverlay(drawable, poiItems);
							// ��poiOverlay��ע�ڵ�ͼ��
							poiOverlay.addToMap(mMapView); 
							poiOverlay.showPopupWindow(0);
							return;
						}
					}
					showToast("����ؽ����");
				} catch (AMapException e) {
					showToast("�������Ӵ���");
				}
			} else if (msg.what == Utils.ERROR) {
				progDialog.dismiss();
				showToast("����ʧ��,�����������ӣ�");
			} else if (msg.what == Utils.POISEARCH_NEXT) {
				curpage++;
				try {
					List<PoiItem> poiItems = result.getPage(curpage);
					if (poiItems != null && poiItems.size() > 0) {
						mMapController.setZoom(13);
						mMapController.animateTo(poiItems.get(0).getPoint());
						if(poiOverlay != null) {
							poiOverlay.removeFromMap();
						}
						Drawable drawable = getResources().getDrawable(R.drawable.da_marker_red);
						poiOverlay = new PoiOverlay(drawable, poiItems);
						poiOverlay.addToMap(mMapView); // ��poiOverlay��ע�ڵ�ͼ��
						poiOverlay.showPopupWindow(0);
						mMapView.invalidate();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	};
}
