package com.app.bayes.bean;

import java.util.ArrayList;
import java.util.List;

import com.app.bayes3.route.M_Point;

public class JsonToBean {

	private static List<Integer> routeNo = null;
	private static List<Integer> routeNodeNo = null;
	private static List<M_Point> routeNodeGeo = null;
	private static List<M_Point> allNodeGeo = null;
	private static Msgbean bean = null;
	
	public static Msgbean jsonToBean (String json) {
		
		String [] list = new String [4];
		String [] elements = null;
		//�ֱ�õ�ÿһ������������
		list = json.split(";");
		
		//ȡ����һ������������
		elements = list[0].split(",");
		routeNo = new ArrayList<Integer>();
		for(int i=0; i<elements.length; i++) {
			routeNo.add(Integer.valueOf(elements[i])-1);
		}
		//ȡ���ڶ�������������
		elements = list[1].split(",");
		routeNodeNo = new ArrayList<Integer>();
		for(int i=0; i<elements.length; i++) {
			routeNodeNo.add(Integer.valueOf(elements[i])-1);
		}
		//ȡ������������������
		elements = list[2].split(",");
		routeNodeGeo = new ArrayList<M_Point>();
		String [] str = new String [2];
		for(int i=0; i<elements.length; i++) {
			str = elements[i].split("-");
			routeNodeGeo.add(new M_Point(Double.valueOf(str[0]), Double.valueOf(str[1])));
		}
		//ȡ�����ĸ�����������
		elements = list[3].split(",");
		allNodeGeo = new ArrayList<M_Point>();
		for(int i=0; i<elements.length; i++) {
			str = elements[i].split("-");
			allNodeGeo.add(new M_Point(Double.valueOf(str[0]), Double.valueOf(str[1])));
		}
		
		bean = new Msgbean();
		bean.setRouteNo(routeNo);
		bean.setRouteNodeNo(routeNodeNo);
		bean.setRouteNodeGeo(routeNodeGeo);
		bean.setAllNodeGeo(allNodeGeo);
		return bean;
	}
	
}
