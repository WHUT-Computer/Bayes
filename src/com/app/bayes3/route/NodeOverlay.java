package com.app.bayes3.route;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Overlay;

public class NodeOverlay extends Overlay{
	
	private Context context;
	private int node ;
	private M_Point point ;
	
	public NodeOverlay(Context c, M_Point p, int _node){
		this.context = c ;
		this.point = p ; 
		this.node = _node;
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		//projection 将经纬度转化为屏幕上的坐标
		GeoPoint gpoint = new GeoPoint(
				(int) (point.getLon() * 1E6), (int) (point.getLat() * 1E6)); 
		Point screenPts = new Point();
		mapView.getProjection().toPixels(gpoint, screenPts);
		
        Bitmap bmp = BitmapFactory.decodeResource( context.getResources(), node);            
        int x = bmp.getWidth();
        int y = bmp.getHeight();
        canvas.drawBitmap(bmp, screenPts.x-x/2, screenPts.y-y, null); 
	}
	
	//点击事件
	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
		return super.onTap(arg0, arg1);
	}
}
