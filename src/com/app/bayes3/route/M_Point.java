package com.app.bayes3.route;

/*
 * �洢�ڵ��γ��
 */
public class M_Point{
	
	private double Lon;		//����
	private double Lat;		//γ��
	
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