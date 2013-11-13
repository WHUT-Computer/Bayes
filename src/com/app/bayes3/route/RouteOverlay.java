package com.app.bayes3.route;

import java.util.ArrayList;
import java.util.List;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import com.amap.mapapi.core.GeoPoint;
import com.amap.mapapi.map.MapView;
import com.amap.mapapi.map.Overlay;
import com.amap.mapapi.map.Projection;

/**
 * 
 * @author wangxiaoyang
 * ����ĳһ��·�� �����·��
 */

public class RouteOverlay extends Overlay{
	//routePoints�洢ĳһ·�������е�ľ�γ��
	private List<M_Point> routePoints = null;
	//����
	private Paint paintLine = null ;
	public RouteOverlay( List<M_Point> r ){
		routePoints = r;
		//���û�����ʽ
		paintLine = new Paint();
		paintLine.setColor(Color.rgb(54, 114, 227));
		paintLine.setStrokeWidth(4.0f);
		paintLine.setStyle(Paint.Style.STROKE);
	}
	//draw ����
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		//projection ����γ��ת��Ϊ��Ļ�ϵ�����
		Projection projection = mapView.getProjection();
		//route �洢��γ�Ⱦ�ת���������
		List<Point> route = new ArrayList<Point>();
		Point screenPoint = new Point();
		byte n = (byte)routePoints.size();
		for(byte i=0; i < n ; i++){
			GeoPoint point = new GeoPoint(
					(int) (routePoints.get(i).getLon() * 1E6), (int) (routePoints.get(i).getLat() * 1E6)); 
			screenPoint = projection.toPixels(point, null); 
			route.add(screenPoint);
		}
		for(int i = 0 ; i < route.size() - 1 ;i++){
			Point point1 = route.get(i);
			Point point2 = route.get(i+1);
			canvas.drawLine(point1.x, point1.y, point2.x, point2.y, paintLine);
		}
	}

	@Override
	public boolean onTap(GeoPoint arg0, MapView arg1) {
//		System.out.println("����� Route");
		return super.onTap(arg0, arg1);
		
	}
	
}