package com.app.bayes3.main;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.app.bayes3.R;
import com.app.bayes3.poisearch.MyPoiSearch;
import com.app.bayes3.route.ConnectServers;
import com.app.bayes3.route.RouteMap;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;

public class MainView extends MapActivity {
	
	private Context mContext = null;						//������
	
	private static MapView mMapView = null;				    //��ͼ��ͼ��ʵ����
	private static MapController mMapController = null;		//��ͼ������
	private static GeoPoint mMapCenter = null;				//��ͼ��ͼ���е�
	private static CharSequence text2="ȡ��";                //NegativeButton
	private static CharSequence text1="ȷ��";                //PositiveButton
	private static int zoom=14;
	
	private Button mPoiSerach = null;						//POI������ť
	private Button mRoute = null;							//·���滮��ť
	private Button mMapLayer = null;						//��ͼͼ��ѡ��ť
	private Button mShare = null;                           //·������
	private Button mMoreSet = null;                         //�������ð�ť
	private Button mAmplify=null;                           //����ť
	private Button mShrink=null;                            //��С��ť
	private ImageButton Traffic = null;                     //ʵʱ��ͨ����ť
	private MapLayerDialog mLayerDialog = null;             //��ͼͼ��ѡ��
	private PopupWindow MoreSet = null;						//�������õ������������Ϊ�Ի���
	@SuppressWarnings("unused")
	private TextView mLocation = null;						//λ����ʾ
	
	private Intent next = null;                             //����Activity����л�
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		mContext = this;
		initMapView();
		initButtons();
	}
	
	/* ��ͼ��ʼ�� */
	private void initMapView(){
    	//���õ�ͼΪʸ��ģʽ 
    	//this.setMapMode(MAP_MODE_VECTOR);
    	mMapView = (MapView)findViewById(R.id.mapView);
    	// �õ�mMapView �Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ����� 
    	mMapController =  mMapView.getController();
    	//�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢��(��* 1E6) 30.524118,114.335489
    	mMapCenter = new GeoPoint((int) (30.531093 * 1E6),
    			(int) (114.323862 *  1E6));
    	//���õ�ͼ���ĵ� 
    	mMapController.setCenter(mMapCenter);		
    	//���õ�ͼzoom ���� 
    	mMapController.setZoom(zoom); 
    }
	
	/* ��ť��ʼ�� */
	private void initButtons() {
		mPoiSerach = (Button)findViewById(R.id.top_poi);
		mRoute = (Button)findViewById(R.id.top_my_route);
		mMapLayer = (Button)findViewById(R.id.top_map_layer);
		mMoreSet = (Button)findViewById(R.id.top_more_set);
		mShare = (Button)findViewById(R.id.top_share);
		
		//������ʵ��
		ButtonClick mClick = new ButtonClick();
		
		mPoiSerach.setOnClickListener(mClick);
		mRoute.setOnClickListener(mClick);
		mMapLayer.setOnClickListener(mClick);
		mMoreSet.setOnClickListener(mClick);
		mShare.setOnClickListener(mClick);
		
		//ʵʱ��ͨ��
		Traffic = (ImageButton)findViewById(R.id.mainview_traffic);
		Traffic.setImageResource(R.drawable.v3_traffic_unchecked);
		MapLayerDialog.isSelect[0] = false;
		Traffic.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setTraffic();
			}
		});
		
		//������С
		mAmplify=(Button)findViewById(R.id.amplify);
		mAmplify.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				zoom=zoom+1;
				mMapController.setZoom(zoom);
			}
		});
		mShrink=(Button)findViewById(R.id.shrink);
		mShrink.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v)
			{
				zoom=zoom-1;
				mMapController.setZoom(zoom);
			}
		});
	}
	
	/* ʵʱ��ͨ���л� */
	private void setTraffic() {
    	if(mMapView.isTraffic()) {
			mMapView.setTraffic(false);
			Traffic.setImageResource(R.drawable.v3_traffic_unchecked);
		} else {
			mMapView.setTraffic(true);
			Traffic.setImageResource(R.drawable.v3_traffic_checked);
		}
    	Log.d("debug", "before "+MapLayerDialog.isSelect[0]);
    	MapLayerDialog.isSelect[0] = !MapLayerDialog.isSelect[0];
    	if( null != mLayerDialog ) 
    		mLayerDialog.updataView();
    }

	/* �¼������� */
	class ButtonClick implements OnClickListener {
		public void onClick(View v) {
			//POI����
			if( ((Button)v).equals( mPoiSerach ) ) {
				next = new Intent(mContext, MyPoiSearch.class);
				startActivity(next);
				overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
			}
			//����
			else if( ((Button)v).equals( mRoute ) ) {
				next = new Intent(mContext, RouteMap.class);
				startActivity(next);
				overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
			}
			//ͼ��ѡ��
			else if( ((Button)v).equals( mMapLayer ) ) {
				if(null == mLayerDialog) {
					mLayerDialog = new MapLayerDialog(mContext, R.style.Theme_MapLayerDialog, 
							mMapView, Traffic);
				}
				mLayerDialog.show();
			}
			//·������
			else if(((Button)v).equals(mShare)) {
				 /**����˵���� *Context ��ǰ���ڵ�Activity�������TabActivity����Activity��Ӧ��ʹ��TabActivity��
				[����] *SHARE_MEDIA ѡ�����ƽ̨������ѡ��������΢������
				[����] *String ѡ�����༭ҳ�е�Ĭ�����֡�
				[��ѡ] *byte[] ����ͼƬ�Ķ���������.[��ѡ] */ 
				UMServiceFactory.shareTo(MainView.this, SHARE_MEDIA.SINA, "�ף�����һ�����ڵ�·���ɣ�", null); 
				
			}
			//��������
			else if( ((Button)v).equals( mMoreSet ) ){
				theMoreSetPopWin();
			}
		}
	}
	
	/* ������õĴ����� */
	private void theMoreSetPopWin(){
		if(null == MoreSet){
			ListView listView = (ListView) getLayoutInflater().inflate(R.layout.more_set_view, null);
			List<String> data = new ArrayList<String>();
	        data.add("�л�����");
	        data.add("����IP");
	        data.add("����");
	        data.add("�˳�");
	        listView.setAdapter(new ArrayAdapter<String>(mContext, R.layout.more_set_view_item,data));
	        listView.setOnItemClickListener(new OnItemClickListener () {
	        	@Override
	        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        		MoreSet.dismiss();
	        		switch(position){
	        			case 0:
	        				startSetCityView();break;
	        			case 1:
	        				readIP();
	        				break;
	        			case 2:
	        				break;
	        			case 3:
	        				MainView.this.finish();
	        				break;
	        		}
	        	}
	        });
	    	MoreSet = new PopupWindow(listView,150,LayoutParams.WRAP_CONTENT);
	    	//�����������õ��popup�հ��������ʧ
	    	MoreSet.setBackgroundDrawable(new BitmapDrawable());
	    	MoreSet.setOutsideTouchable(true);
	    	MoreSet.setFocusable(true); 
		}
		
		if(MoreSet.isShowing()){
			MoreSet.dismiss();
		} else {
			MoreSet.update(); 
			MoreSet.showAsDropDown(mMoreSet);
		}
	}
	
	//����IP
	private void readIP()
	{
		final EditText editText = new EditText(this);
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		
		    builder.setTitle("���ӷ�����")
		    .setView(editText)
		    .setNegativeButton(text2,  new DialogInterface.OnClickListener(){
		    	public void onClick(DialogInterface dialog,int which){
		    		
		    	}
		    })
		    .setPositiveButton(text1, new DialogInterface.OnClickListener(){
		    	public void onClick(DialogInterface dialog,int which){
		    		Editable editable = editText.getText();
		    		String str = editable.toString();
		    		ConnectServers.setConUrl(str);
		    	}
		    })
		    .create()
		    .show();
	}
	
	private void startSetCityView() {
		next = new Intent(mContext, SetCityView.class);
		startActivityForResult(next, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(1 == requestCode && 2 == resultCode ) {
			Log.d("debug", "result");
//			double l[] = data.getDoubleArrayExtra("location");
//			mMapCenter = new GeoPoint((int) (l[0] * 1E6),(int) (l[1] *  1E6));
//			mMapController.setCenter(mMapCenter);
		}
	}
	
}
