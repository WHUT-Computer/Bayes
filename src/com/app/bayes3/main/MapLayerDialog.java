package com.app.bayes3.main;

import com.amap.mapapi.map.MapView;
import com.app.bayes3.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MapLayerDialog extends Dialog implements OnItemClickListener{
	
	private Context mContext = null; 
	private MapView mMapView = null; 
	private ImageButton mTraffic = null;
	public static boolean isEnable[] = {true,true,true,true};
	private String[] menu_item = {													
			"实时交通",
			"卫星地图",
			"街景地图",
			"矢量模式"
	};
	public static boolean isSelect[] = {false,false,false,false};
	
	public MapLayerDialog(Context context, int theme,MapView mapView,ImageButton Traffic) {
		super(context, theme);
		this.mContext = context;
		this.mMapView = mapView;
		this.mTraffic = Traffic;
		initData();
	}
	
	public MapLayerDialog(Context context, int theme) {
		super(context, theme);
		this.mContext = context;
		initData();
	}

	protected MapLayerDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.mContext = context;
		initData();
	}

	public MapLayerDialog(Context context) {
		super(context);
		this.mContext = context;
		initData();
	}
	
	private void initData(){
		setContentView(R.layout.map_layer_dialog);
		ListView menu = (ListView)findViewById(R.id.MapLayerMenu);
		mAdapter = new LAdapter();
		menu.setAdapter(mAdapter);
		menu.setOnItemClickListener(this);
	}
	
	private LAdapter mAdapter = null;
	class LAdapter extends BaseAdapter{
		
		public int getCount() {
			return menu_item.length;
		}
		@Override
		public Object getItem(int position) {
			return isSelect[position];
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				holder = new Holder();
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.map_layer_list_item_view, null);
				holder.text = (TextView)convertView.findViewById(R.id.menu_tip);
				holder.img = (ImageView)convertView.findViewById(R.id.select_tip);
				convertView.setTag(holder);
			}
			else {
				holder = (Holder) convertView.getTag();
			}
			holder.text.setText(menu_item[position]);
			
			if(MapLayerDialog.isSelect[position]){
				holder.img.setBackgroundResource(R.drawable.v3_tick);
			}
			else {
				holder.img.setBackgroundResource(R.drawable.unchoose_tip);
			}
			if(MapLayerDialog.isEnable[position]){
				convertView.setEnabled(true);
			}
			else {
				convertView.setEnabled(false);
			}
			return convertView;
		}
		
		class Holder{
			TextView text = null;
			ImageView img = null;
		}
		Holder holder;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(0 == position){
			setTraffic();
		}
		else {
			setMapLayer();
		}
		changeState();
		updataView();
		this.dismiss();
	}
	
	private void changeState(){
		if(isSelect[1]){
			
		}
		
	}
	
	private void setMapLayer(){
    	if(isSelect[1]){
    		mMapView.setSatellite(true);
    	} else {
    		mMapView.setSatellite(false);
    	}
    	if(isSelect[2]){
    		mMapView.setStreetView(true);
    	} else {
    		mMapView.setStreetView(false);
    	}
    	if(isSelect[3]){
    		mMapView.setVectorMap(true);
    	} else {
    		mMapView.setVectorMap(false);
    	}
    }
	
	private void setTraffic(){
    	if(mMapView.isTraffic()) {
			mMapView.setTraffic(false);
			mTraffic.setImageResource(R.drawable.v3_traffic_unchecked);
		} else {
			mMapView.setTraffic(true);
			mTraffic.setImageResource(R.drawable.v3_traffic_checked);
		}
    	isSelect[0] = !isSelect[0];
    }
	
	public void updataView(){
		mAdapter.notifyDataSetChanged();
	}
	
}
