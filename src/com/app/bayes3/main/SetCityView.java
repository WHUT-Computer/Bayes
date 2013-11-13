package com.app.bayes3.main;

import java.util.ArrayList;
import java.util.List;

import com.app.bayes3.R;
import com.app.bayes3.util.Utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class SetCityView extends Activity implements OnItemClickListener{
	private Context mContext = null;
	@SuppressWarnings("unused")
	private RelativeLayout setView = null;
	private TextView title = null;
	private ListView setList = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_city_view);
		mContext = this;
		setView = ( RelativeLayout )findViewById(R.id.set_city_view);
		title = (TextView)findViewById(R.id.set_city_list_title);
		title.setText("ȫ��ʡ����");
		setList = (ListView)findViewById(R.id.set_city_list_view);
		initSData();
		mAdapter = new LAdapter();
		setList.setAdapter(mAdapter);
		setList.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		temp = (String)mAdapter.getItem(position);
		if(inSheng){
			inSheng = false;
			title.setText(temp);
			
			mAdapter = new LAdapter(temp);
			setList.setAdapter(mAdapter);
//			initCData(temp);
//			inCity = true;
//			mAdapter.notifyDataSetChanged();
		}
		else {
			location[0] = lat[position];
			location[1] = lon[position];
			Intent result = new Intent();
			result.putExtra("location", location);
			this.setResult(2, result);
			this.finish();
		}
	}
	
	private double location[] = {1.0,1.0}; 
	
	private boolean inSheng = true;
//	private boolean inCity = false;
	private List<String> data = new ArrayList<String>();
	private String temp ;
	private Cursor cursor;
	
	private void initSData(){
		data.clear();
		// ����SQLiteDatabase�����query�������в�ѯ������һ��Cursor���������ݿ��ѯ���صĽ��������
		cursor = Utils.db.query(
				true, "location_data", 
				new String[]{"sname"}, 
				null, null, null, null, null, null);
		while (cursor.moveToNext()) {
			data.add(cursor.getString(0));
		}
	}
	
	
	private double[] lon; 
	private double[] lat; 
	private void initCData(String sname){
		data.clear();
		// ����SQLiteDatabase�����query�������в�ѯ������һ��Cursor���������ݿ��ѯ���صĽ��������
		// ��һ������String������
		// �ڶ�������String[]:Ҫ��ѯ������
		// ����������String����ѯ����
		// ���ĸ�����String[]����ѯ�����Ĳ���
		// ���������String:�Բ�ѯ�Ľ�����з���
		// ����������String���Է���Ľ����������
		// ���߸�����String���Բ�ѯ�Ľ����������
		cursor = Utils.db.query("location_data", new String[] { "cname","lon","lat"}, 
				"sname = ?",new String[]{sname}, 
				null, null, null);
		int n = cursor.getCount();
		lon = new double[n] ;
		lat = new double[n] ;
		int count = 0;
		while (cursor.moveToNext()) {
			data.add(cursor.getString(0));
			lon[count] = cursor.getDouble(1);
			lat[count++] = cursor.getDouble(2);
		}
		
	}
	
//	private void setTheMapCenter(double Lon , double Lat ){
//    	MainView.mMapCenter = new GeoPoint((int) (Lon * 1E6),(int) (Lat *  1E6));
//    	MainView.mMapController.setCenter(MainView.mMapCenter);
//    }
	
	LAdapter mAdapter = null;
	class LAdapter extends BaseAdapter{
		
		public LAdapter(){
			initSData();
		}
		
		public LAdapter(String cname){
			initCData(cname);
		}
		
		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(null == convertView){
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.set_city_list_item_view, null);
				text = (TextView)convertView.findViewById(R.id.set_city_item);
				convertView.setTag(text);
			}
			else{
				text = (TextView) convertView.getTag();
			}
			text.setText(data.get(position));
			return convertView;
		}
		
		TextView text;
	}
	
}
