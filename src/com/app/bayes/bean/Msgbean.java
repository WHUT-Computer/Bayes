package com.app.bayes.bean;

import java.util.List;
import com.app.bayes3.route.M_Point;

public class Msgbean {
	
	private List<Integer> routeNo;
	private List<Integer> routeNodeNo;
	private List<M_Point> routeNodeGeo;
	private List<M_Point> allNodeGeo;
	
	public List<Integer> getRouteNo() {
		return routeNo;
	}
	public void setRouteNo(List<Integer> routeNo) {
		this.routeNo = routeNo;
	}
	public List<Integer> getRouteNodeNo() {
		return routeNodeNo;
	}
	public void setRouteNodeNo(List<Integer> routeNodeNo) {
		this.routeNodeNo = routeNodeNo;
	}
	public List<M_Point> getRouteNodeGeo() {
		return routeNodeGeo;
	}
	public void setRouteNodeGeo(List<M_Point> routeNodeGeo) {
		this.routeNodeGeo = routeNodeGeo;
	}
	public List<M_Point> getAllNodeGeo() {
		return allNodeGeo;
	}
	public void setAllNodeGeo(List<M_Point> allNodeGeo) {
		this.allNodeGeo = allNodeGeo;
	}
	
}

