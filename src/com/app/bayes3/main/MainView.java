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
	
	private Context mContext = null;						//上下文
	
	private static MapView mMapView = null;				    //地图视图类实例，
	private static MapController mMapController = null;		//地图控制器
	private static GeoPoint mMapCenter = null;				//地图视图的中点
	private static CharSequence text2="取消";                //NegativeButton
	private static CharSequence text1="确定";                //PositiveButton
	private static int zoom=14;
	
	private Button mPoiSerach = null;						//POI搜索按钮
	private Button mRoute = null;							//路径规划按钮
	private Button mMapLayer = null;						//地图图层选择按钮
	private Button mShare = null;                           //路况分享
	private Button mMoreSet = null;                         //其他设置按钮
	private Button mAmplify=null;                           //扩大按钮
	private Button mShrink=null;                            //缩小按钮
	private ImageButton Traffic = null;                     //实时交通流按钮
	private MapLayerDialog mLayerDialog = null;             //地图图层选择
	private PopupWindow MoreSet = null;						//其他设置弹窗（可以理解为对话框）
	@SuppressWarnings("unused")
	private TextView mLocation = null;						//位置提示
	
	private Intent next = null;                             //用于Activity间的切换
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_view);
		mContext = this;
		initMapView();
		initButtons();
	}
	
	/* 地图初始化 */
	private void initMapView(){
    	//设置地图为矢量模式 
    	//this.setMapMode(MAP_MODE_VECTOR);
    	mMapView = (MapView)findViewById(R.id.mapView);
    	// 得到mMapView 的控制权,可以用它控制和驱动平移和缩放 
    	mMapController =  mMapView.getController();
    	//用给定的经纬度构造一个GeoPoint，单位是微度(度* 1E6) 30.524118,114.335489
    	mMapCenter = new GeoPoint((int) (30.531093 * 1E6),
    			(int) (114.323862 *  1E6));
    	//设置地图中心点 
    	mMapController.setCenter(mMapCenter);		
    	//设置地图zoom 级别 
    	mMapController.setZoom(zoom); 
    }
	
	/* 按钮初始化 */
	private void initButtons() {
		mPoiSerach = (Button)findViewById(R.id.top_poi);
		mRoute = (Button)findViewById(R.id.top_my_route);
		mMapLayer = (Button)findViewById(R.id.top_map_layer);
		mMoreSet = (Button)findViewById(R.id.top_more_set);
		mShare = (Button)findViewById(R.id.top_share);
		
		//监听的实例
		ButtonClick mClick = new ButtonClick();
		
		mPoiSerach.setOnClickListener(mClick);
		mRoute.setOnClickListener(mClick);
		mMapLayer.setOnClickListener(mClick);
		mMoreSet.setOnClickListener(mClick);
		mShare.setOnClickListener(mClick);
		
		//实时交通流
		Traffic = (ImageButton)findViewById(R.id.mainview_traffic);
		Traffic.setImageResource(R.drawable.v3_traffic_unchecked);
		MapLayerDialog.isSelect[0] = false;
		Traffic.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setTraffic();
			}
		});
		
		//扩大，缩小
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
	
	/* 实时交通流切换 */
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

	/* 事件监听器 */
	class ButtonClick implements OnClickListener {
		public void onClick(View v) {
			//POI搜索
			if( ((Button)v).equals( mPoiSerach ) ) {
				next = new Intent(mContext, MyPoiSearch.class);
				startActivity(next);
				overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
			}
			//导航
			else if( ((Button)v).equals( mRoute ) ) {
				next = new Intent(mContext, RouteMap.class);
				startActivity(next);
				overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
			}
			//图层选择
			else if( ((Button)v).equals( mMapLayer ) ) {
				if(null == mLayerDialog) {
					mLayerDialog = new MapLayerDialog(mContext, R.style.Theme_MapLayerDialog, 
							mMapView, Traffic);
				}
				mLayerDialog.show();
			}
			//路况分享
			else if(((Button)v).equals(mShare)) {
				 /**参数说明： *Context 当前所在的Activity，如果在TabActivity的子Activity中应该使用TabActivity。
				[必须] *SHARE_MEDIA 选择分享平台（这里选择了新浪微博）。
				[必须] *String 选择分享编辑页中的默认文字。
				[可选] *byte[] 分享图片的二进制数据.[可选] */ 
				UMServiceFactory.shareTo(MainView.this, SHARE_MEDIA.SINA, "亲，分享一下现在的路况吧！", null); 
				
			}
			//更多设置
			else if( ((Button)v).equals( mMoreSet ) ){
				theMoreSetPopWin();
			}
		}
	}
	
	/* 点击设置的处理方法 */
	private void theMoreSetPopWin(){
		if(null == MoreSet){
			ListView listView = (ListView) getLayoutInflater().inflate(R.layout.more_set_view, null);
			List<String> data = new ArrayList<String>();
	        data.add("切换城市");
	        data.add("更换IP");
	        data.add("设置");
	        data.add("退出");
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
	    	//以下两行设置点击popup空白区点击消失
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
	
	//输入IP
	private void readIP()
	{
		final EditText editText = new EditText(this);
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		
		    builder.setTitle("连接服务器")
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
