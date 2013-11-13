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
	private int buttonStyle = 0;     //0表示按钮为搜索按钮  1表示按钮为下一页按钮
	private int curpage = 1;
	@SuppressWarnings("unused")
	private int cnt = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_search);
		mContext = this;
		mMapView = (MapView) findViewById(R.id.poisearch_MapView);
		//设置启用内置的缩放控件
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		mMapController = mMapView.getController();  
		//用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		mMapCenter = new GeoPoint((int) (30.524118 * 1E6),(int) (114.335489 *  1E6));  
		//设置地图中心点
		mMapController.setCenter(mMapCenter);  
		//设置地图zoom级别
		mMapController.setZoom(14); 
		searchText = (EditText) findViewById(R.id.TextViewSearch);
//		searchText.isFocused();
		searchText.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//搜索框文本变化时 修改按钮的提示语为“搜索” 按钮类型改为0
				search.setText("搜索");
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
		//获得用户的输入
		String user_search = searchText.getText().toString().trim();
		progDialog.setMessage("正在搜索:\n" + user_search);
		progDialog.show();
		//修改按钮类型为 "下一页"
		buttonStyle = 1;
		search.setText("下一页");
		curpage = 1;
		cnt = 0;
		try {
			PoiSearch poiSearch = new PoiSearch(MyPoiSearch.this,
					// 设置搜索字符串，027为城市区号（需要获取当前城市的区号可在程序载入时定位获取）
					new PoiSearch.Query(user_search, PoiTypeDef.All, cityCode)); 
			//在当前地图显示范围内查找
			poiSearch.setBound(new SearchBound(mMapView));
			//设置搜索每次最多返回结果数
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
	
	//监听返回键
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
							// 将结果的第一页添加到PoiOverlay
							poiOverlay = new PoiOverlay(drawable, poiItems);
							// 将poiOverlay标注在地图上
							poiOverlay.addToMap(mMapView); 
							poiOverlay.showPopupWindow(0);
							return;
						}
					}
					showToast("无相关结果！");
				} catch (AMapException e) {
					showToast("网络连接错误！");
				}
			} else if (msg.what == Utils.ERROR) {
				progDialog.dismiss();
				showToast("搜索失败,请检查网络连接！");
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
						poiOverlay.addToMap(mMapView); // 将poiOverlay标注在地图上
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
