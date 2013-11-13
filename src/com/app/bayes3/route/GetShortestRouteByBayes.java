package com.app.bayes3.route;

import com.app.bayes.bean.JsonToBean;
import com.app.bayes.bean.Msgbean;

public class GetShortestRouteByBayes {

	private ConnectServers server = null;
	//带最短路径信息
	private Msgbean bean = null;
	
	public GetShortestRouteByBayes () {}
	
	public Msgbean fixPath(int start, int end) {
		server = new ConnectServers(start, end);
		String str = server.getResult();
		bean = JsonToBean.jsonToBean(str);
		return bean;
	}
	
}
