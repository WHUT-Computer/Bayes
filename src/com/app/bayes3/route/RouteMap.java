package com.app.bayes3.route;

/*
 * ������������
 */
import java.util.List;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapActivity;
import com.amap.mapapi.map.MapController;
import com.amap.mapapi.map.MapView;
import com.app.bayes.bean.Msgbean;
import com.app.bayes3.R;
import com.app.bayes3.util.Rotate3dAnimation;

public class RouteMap extends MapActivity{
	
	private Context mContext = null;
	//mMapView��ͼ��ͼ
	private MapView mMapView = null ;
	private MapController mMapController = null;
	private GeoPoint mMapCenter = null;
	
	@SuppressWarnings("unused")
	private TextView Top_Tip = null;
	private ImageButton Traffic = null;
	private Button chooseNode  = null;
	
	private RelativeLayout mViews = null;
	private RelativeLayout nodeListView = null;
	private RelativeLayout routeMapView = null;
	
	private ListView startList = null ;
	private ListView endList = null ;
	private RadioGroup ways = null;
	private RadioButton short_way = null;
	private RadioButton bayes_way = null;
	
	private int startId = -1 ;
	private int endId = -1 ;
	private List< M_Point > route  = null;
	
	@SuppressWarnings("unused")
	private ConnectServers connectServer = null;
	private ShrotRoute myMapData = null;
	
	//������Nodeͼ��
	private int[] node_icon = new int[]{
		R.drawable.v3_indoormap_marker_1png2 ,
		R.drawable.v3_indoormap_marker_2png2 ,
		R.drawable.v3_indoormap_marker_3png2 ,
		R.drawable.v3_indoormap_marker_4png2 ,
		R.drawable.v3_indoormap_marker_5png2 ,
		R.drawable.v3_indoormap_marker_6png2 ,
		R.drawable.v3_indoormap_marker_7png2 ,
		R.drawable.v3_indoormap_marker_8png2 ,
		R.drawable.v3_indoormap_marker_9png2 ,
		R.drawable.v3_indoormap_marker_10png2 ,
		R.drawable.v3_indoormap_marker_11png2 ,
		R.drawable.v3_indoormap_marker_12png2 ,
		R.drawable.v3_indoormap_marker_13png2 ,
		R.drawable.v3_indoormap_marker_14png2 ,
		R.drawable.v3_indoormap_marker_15png2 ,
		R.drawable.v3_indoormap_marker_16png2 ,
		R.drawable.v3_indoormap_marker_17png2 ,
		R.drawable.v3_indoormap_marker_18png2 ,
		R.drawable.v3_indoormap_marker_19png2 ,
		R.drawable.v3_indoormap_marker_20png2 ,
		R.drawable.v3_indoormap_marker_21png2 ,
		R.drawable.v3_indoormap_marker_22png2 ,
		R.drawable.v3_indoormap_marker_23png2 ,
		R.drawable.v3_indoormap_marker_24png2 ,
		R.drawable.v3_indoormap_marker_25png2 ,
		R.drawable.v3_indoormap_marker_26png2 ,
		R.drawable.v3_indoormap_marker_27png2 ,
	};
	
	//����·���ϵ�Node��ͼ��
	private int[] routeNode = new int[]{
		R.drawable.v3_indoormap_marker_1 ,
		R.drawable.v3_indoormap_marker_2 ,
		R.drawable.v3_indoormap_marker_3 ,
		R.drawable.v3_indoormap_marker_4 ,
		R.drawable.v3_indoormap_marker_5 ,
		R.drawable.v3_indoormap_marker_6 ,
		R.drawable.v3_indoormap_marker_7 ,
		R.drawable.v3_indoormap_marker_8 ,
		R.drawable.v3_indoormap_marker_9 ,
		R.drawable.v3_indoormap_marker_10 ,
		R.drawable.v3_indoormap_marker_11 ,
		R.drawable.v3_indoormap_marker_12 ,
		R.drawable.v3_indoormap_marker_13 ,
		R.drawable.v3_indoormap_marker_14 ,
		R.drawable.v3_indoormap_marker_15 ,
		R.drawable.v3_indoormap_marker_16 ,
		R.drawable.v3_indoormap_marker_17 ,
		R.drawable.v3_indoormap_marker_18 ,
		R.drawable.v3_indoormap_marker_19 ,
		R.drawable.v3_indoormap_marker_20 ,
		R.drawable.v3_indoormap_marker_21 ,
		R.drawable.v3_indoormap_marker_22 ,
		R.drawable.v3_indoormap_marker_23 ,
		R.drawable.v3_indoormap_marker_24 ,
		R.drawable.v3_indoormap_marker_25 ,
		R.drawable.v3_indoormap_marker_26 ,
		R.drawable.v3_indoormap_marker_27 ,
	};
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.route_map_view);
		initData();
	}

	/* ��ͼ��ʼ��������ʾ�������� */
	private void initData(){
		mContext = this;
		//���õ�ͼΪʸ��ģʽ 
    	this.setMapMode(MAP_MODE_VECTOR);
    	mMapView = (MapView)findViewById(R.id.map_view_in_route);
    	//�õ�mMapView�Ŀ���Ȩ�������������ƺ�����ƽ�ƺ����� 
    	mMapController =  mMapView.getController();
    	//�ø����ľ�γ�ȹ���һ��GeoPoint����λ��΢��(��*1E6)30.524118,114.335489
    	mMapCenter = new GeoPoint((int) (30.531093 * 1E6), (int) (114.323862 *  1E6));
    	//���õ�ͼ���ĵ� 
    	mMapController.setCenter(mMapCenter);		
    	//���õ�ͼzoom ���� 
    	mMapController.setZoom(14); 
    	
    	//��ʼ��������ں�ͼ����Ϣ����ȥ
    	myMapData = new ShrotRoute(mContext);
    	//�ڵ�ͼ����ʾ��ʼ�ĸ�����
    	drawNode(myMapData.getNodeGeo(), node_icon);
  
    	//�ֱ��ȡ·���滮�������������յ��ѡ����棬ͨ�����ÿɼ������л�����
    	mViews = (RelativeLayout)findViewById(R.id.views);
    	nodeListView = (RelativeLayout)findViewById(R.id.route_choose_node_view);
    	routeMapView = (RelativeLayout)findViewById(R.id.route_map_view);
    	
        Top_Tip = (TextView)findViewById(R.id.route_map_tip);

        //����յ�ѡ��ť
		chooseNode = (Button)findViewById(R.id.choose_node_in_route);
		//Dijkstra�㷨��ť
		short_way = (RadioButton)findViewById(R.id.short_way);
		//Bayes�㷨��ť
		bayes_way = (RadioButton)findViewById(R.id.bayes_way);
		
		chooseNode.setText("ѡ�������յ�");
		chooseNode.setOnClickListener(new OnClickListener() {
			private String text;
			@Override
			public void onClick(View v) {
				text = ((Button)v).getText().toString();
				if(text.equals("ѡ�������յ�")){
					initListView();
					clearLastChoose();
					applyRotation(0,90);
					chooseNode.setText("ȷ��");
				}
				else if(text.equals("ȷ��")){
					if(checkListView()){
						applyRotation(360, 270);
						//��Ҫ����ѡ��ķ���ȷ����������
						if(short_way.isChecked()){
							drawResultRouteDijkstra();//Dijkstra�㷨
						} else if (bayes_way.isChecked()) {
							drawResultRouteBayes();
						}
						chooseNode.setText("ѡ�������յ�");
					}
				}
			}
		});
		
		//ʵʱ��ͨ��
		Traffic = (ImageButton)findViewById(R.id.route_mapview_traffic);
		Traffic.setImageResource(R.drawable.v3_traffic_unchecked);
    	Traffic.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setTraffic();
			}
		});
	}
	
	/* ʵʱ��ͨ��ť�Ĵ����� */
	private void setTraffic(){
    	if(mMapView.isTraffic()) {
			mMapView.setTraffic(false);
			Traffic.setImageResource(R.drawable.v3_traffic_unchecked);
		} else {
			mMapView.setTraffic(true);
			Traffic.setImageResource(R.drawable.v3_traffic_checked);
		}
    }
	
	/* �ڵ�ͼ����ʾ�������� */
	private void drawNode(List<M_Point> nodeGeo , int[] node ){
		byte n = (byte) nodeGeo.size();
		for( byte i = 0 ; i < n ; i++ ){
			NodeOverlay nodesOverlay = new NodeOverlay(mContext, nodeGeo.get(i), node[i]);
			mMapView.getOverlays().add(nodesOverlay);
		}
	}
	
	/* ��ʼ������յ��ѡ���б� */
	private void initListView(){
		//��ʼ��·���滮��ʽ�ĵ�ѡ��ť��
		if(ways == null){
			ways = (RadioGroup)findViewById(R.id.ways);
		}
		//��ʼ������յ��б�
		if(startList == null || endList == null){
			//ѡ�����¼�����
			ItemClick itemClick = new ItemClick();
			
			sAdapter = new ListAdapter();
			startList = (ListView) findViewById(R.id.StartListView);
			startList.setAdapter(sAdapter);
			startList.setOnItemClickListener(itemClick);
			
			eAdapter = new ListAdapter();
			endList = (ListView) findViewById(R.id.EndListView);
			endList.setAdapter(eAdapter);
			endList.setOnItemClickListener(itemClick);
		}
	}
	
	/* ����ϴε�ѡ���� */
	private void clearLastChoose(){
		if(startId == -1 || endId == -1) 
			return ;
		sAdapter.bool_tip[startId] = !sAdapter.bool_tip[startId];
		sAdapter.notifyDataSetChanged();
		startId = -1;
		eAdapter.bool_tip[endId] = !eAdapter.bool_tip[endId];
		eAdapter.notifyDataSetChanged();
		endId = -1;
	}
	
	private ListAdapter sAdapter = null;
	private ListAdapter eAdapter = null;
	
	class ListAdapter extends BaseAdapter{
		List<String> nodeName = myMapData.getNodeName();
		public boolean bool_tip[];
		public ListAdapter(){
			int n = nodeName.size();
			bool_tip = new boolean[n];
			for(int i = 0 ; i < n ; i++){
				bool_tip[i] = false;
			}
		}
		@Override
		public int getCount() {
			return nodeName.size();
		}

		@Override
		public Object getItem(int position) {
			return nodeName.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(null == convertView){
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.route_node_list_item_view, null);
				holder = new ViewHolder();
				holder.text = (TextView)convertView.findViewById(R.id.nodeName);
				holder.tip = (ImageView)convertView.findViewById(R.id.choose_tip);
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.text.setText(nodeName.get(position));
			if(bool_tip[position]){
				holder.tip.setBackgroundResource(R.drawable.v3_tick);
			}
			else {
				holder.tip.setBackgroundResource(R.drawable.unchoose_tip);
			}
			return convertView;
		}
		class ViewHolder{
			TextView text;
			ImageView tip;
		}
		private ViewHolder holder;
	}
	
	class ItemClick implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(parent.equals(startList)) {
				if(startId != -1){
					sAdapter.bool_tip[startId] = !sAdapter.bool_tip[startId];
				} 
				sAdapter.bool_tip[position] = !sAdapter.bool_tip[position];
				sAdapter.notifyDataSetChanged();
				startId = position;
			}
			else if(parent.equals(endList)) {
				if(endId != -1){
					eAdapter.bool_tip[endId] = !eAdapter.bool_tip[endId];
				} 
				eAdapter.bool_tip[position] = !eAdapter.bool_tip[position];
				eAdapter.notifyDataSetChanged();
				endId = position;
			}
		}
	}
	
	private RouteInfo routeinfo = null;
	/* ʹ��Dijkstra�㷨Ѱ�����·�� */
	private void drawResultRouteDijkstra() {
		myMapData.fixPath( startId, endId );
		//�������·�ε���ϸ��γ����Ϣ����ϸ����Ϣ�洢��txt�ļ���
		if(null == routeinfo ) routeinfo = new RouteInfo(mContext);
		//�������·
		if(mMapView.getOverlays()!=null){
			mMapView.getOverlays().clear();
		}
		List<Integer> routeNo = myMapData.getRouteNo();
		int n = routeNo.size();
		int i = 0;
		List< List<M_Point> > allRouteM_Point = routeinfo.getRoutes();
		for( i = 0 ; i < n ; i++)
		{
			route =  allRouteM_Point.get( routeNo.get(i) );
			RouteOverlay a2b = new RouteOverlay(route);
			mMapView.getOverlays().add(a2b);
		}
//		int[] newNodeIcon = new int[14];
		int [] newNodeIcon = new int[node_icon.length];
		System.arraycopy(node_icon, 0, newNodeIcon, 0, node_icon.length);
		int[] routeNodeNo = myMapData.getRouteNodeNo();
		n = routeNodeNo.length;
		for(i = 1 ; i < n-1 ; i++){
			newNodeIcon[ routeNodeNo[i] ] = routeNode[ routeNodeNo[i] ];
		}
		newNodeIcon[routeNodeNo[0]] = R.drawable.v3_icon_qidian;
		newNodeIcon[routeNodeNo[n-1]] = R.drawable.v3_icon_zhongdian;
		M_Point center = myMapData.getNodeGeo().get(routeNodeNo[0]);
		mMapCenter = new GeoPoint((int) (center.getLon() * 1E6),
    			(int) (center.getLat() *  1E6));
		mMapController.setCenter(mMapCenter);
		drawNode(myMapData.getNodeGeo(), newNodeIcon);
		mMapView.postInvalidate();
	}
	
	/* ʹ��Bayes�㷨Ѱ�����·�� */
	private void drawResultRouteBayes() {
		
		if(null == routeinfo ) routeinfo = new RouteInfo(mContext);
		
		Msgbean bean = null;
		GetShortestRouteByBayes bayes = new GetShortestRouteByBayes();
		bean = bayes.fixPath( startId, endId );
		
		if(mMapView.getOverlays()!=null){
			mMapView.getOverlays().clear();
		}
		List<Integer> routeNo = bean.getRouteNo();
		int n = routeNo.size();
		List< List<M_Point> > allRouteM_Point = routeinfo.getRoutes();
		for(int i = 0 ; i < n ; i++)
		{
			route =  allRouteM_Point.get( routeNo.get(i) );
			RouteOverlay a2b = new RouteOverlay(route);
			mMapView.getOverlays().add(a2b);
		}
		
		int [] newNodeIcon = new int[node_icon.length];
		System.arraycopy(node_icon, 0, newNodeIcon, 0, node_icon.length);
		int [] routeNodeNo = new int[bean.getRouteNodeNo().size()];
		n = routeNodeNo.length;
		for(int i=0; i<n; i++) {
			routeNodeNo[i] = bean.getRouteNodeNo().get(i);
		}
		
		for(int i = 1 ; i < n-1 ; i++){
			newNodeIcon[ routeNodeNo[i] ] = routeNode[ routeNodeNo[i] ];
		}
		newNodeIcon[routeNodeNo[0]] = R.drawable.v3_icon_qidian;
		newNodeIcon[routeNodeNo[n-1]] = R.drawable.v3_icon_zhongdian;
		M_Point center = bean.getAllNodeGeo().get(routeNodeNo[0]);
		mMapCenter = new GeoPoint((int) (center.getLon() * 1E6),
    			(int) (center.getLat() *  1E6));
		mMapController.setCenter(mMapCenter);
		drawNode(bean.getAllNodeGeo(), newNodeIcon);
		mMapView.postInvalidate();
	}
	
	/* �ж������յ��Ƿ�Ϸ� */
	private boolean checkListView(){
		if(-1 == startId){
			showToast("��ѡ�����");
			return false;
		}
		if(-1 == endId){
			showToast("��ѡ���յ�");
			return false;
		}
		if(startId == endId){
			showToast("�����յ㲻����ͬ");
			return false;
		}
		return true;
	}
	
	public void showToast(String showString) {
		Toast.makeText(mContext, showString, Toast.LENGTH_SHORT).show();
	}
	
	
	//3D��ת�������
	private float centerX = 0.0f;
	private float centerY = 0.0f;
	private float depthZ = 320.0f;
	//3D��ת����
	private Rotate3dAnimation rotation = null;
	//��̫�������
	private AccelerateInterpolator acce = new AccelerateInterpolator();
	//�ôζ���������Ϻ�,������һ��������Ϊ����������
	private DisplayNextView dnext = new DisplayNextView();
	
	private int NextAnims = 10000;
    
    private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			if(NextAnims == msg.what){
				NextAnim();
			}
			super.handleMessage(msg);
		}
    };
	
    /**
     * Setup a new 3D rotation on the container view.
     * @param position the item that was clicked to show a picture, or -1 to show the list
     * @param start the start angle at which the rotation must begin
     * @param end the end angle of the rotation
     */
    private void applyRotation(float start, float end) {
    	
    	if(0.0f == centerX || 0.0f == centerY ){
    		// Find the center of the container
    		centerX = mViews.getWidth() / 2.0f;
    		centerY = mViews.getHeight() / 2.0f;
    	}
    	if(null == rotation){
    		rotation = new Rotate3dAnimation(start, end, centerX, centerY, depthZ, true);
    		rotation.setDuration(500);
    		rotation.setFillAfter(true);
    		rotation.setInterpolator(acce);	
    	}
    	else{
    		rotation.set3DAnimation(start, end, true);
    	}
        rotation.setAnimationListener(dnext);
        mViews.startAnimation(rotation);
    }
    
    /**
     * This class listens for the end of the first half of the animation.
     * It then posts a new action that effectively swaps the views when the container
     * is rotated 90 degrees and thus invisible.
     */
    private final class DisplayNextView implements Animation.AnimationListener {
    	
        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
        	mHandler.sendEmptyMessage(NextAnims);
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }
	
    /**
     * This class is responsible for swapping the views and start the second
     * half of the animation.
     */
    private void NextAnim(){
    	if (routeMapView.getVisibility() == View.VISIBLE) {
    		routeMapView.setVisibility(View.GONE);
            nodeListView.setVisibility(View.VISIBLE);
            nodeListView.requestFocus();
            rotation.set3DAnimation(270, 360, false);
        } else {
        	nodeListView.setVisibility(View.GONE);
        	routeMapView.setVisibility(View.VISIBLE);
        	routeMapView.requestFocus();
            rotation.set3DAnimation(90, 0, false);
        }
        rotation.setAnimationListener(null);
        mViews.startAnimation(rotation);
    }

}
