package com.app.bayes3.route;

/*
 * 存储节点姐纬度
 */
public class M_Point{
	
	private double Lon;		//经度
	private double Lat;		//纬度
	
	public M_Point(double lon,double lat){
		this.setLon(lon);
		this.setLat(lat);
	}
	
	public void setGeo(double lon,double lat){
		this.setLon(lon);
		this.setLat(lat);
	}
	
	public double getLon() {
		return Lon;
	}
	public void setLon(double lon) {
		Lon = lon;
	}
	public double getLat() {
		return Lat;
	}
	public void setLat(double lat) {
		Lat = lat;
	}
	
}