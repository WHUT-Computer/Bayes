package com.app.bayes3.util;

import android.database.sqlite.SQLiteDatabase;

/**
 * ȫ�ֵĿ��Ʊ���
 * @author wangxiaoyang
 *
 */
public class Utils {
	
	public static SQLiteDatabase db = null;
	
	public static final int POISEARCH=1000;
	
	public static final int ERROR=1001;
	public static final int FIRST_LOCATION=1002;
	
	public static final int ROUTE_START_SEARCH=2000;//·���滮�������
	public static final int ROUTE_END_SEARCH=2001;//·���滮�������
	public static final int ROUTE_SEARCH_RESULT=2002;//·���滮���
	public static final int ROUTE_SEARCH_ERROR=2004;//·���滮����ʼ�������쳣	
	
	public static final int REOCODER_RESULT=3000;//���������
	public static final int DIALOG_LAYER=4000;
	public static final int POISEARCH_NEXT=5000;
	
	public static final int BUSLINE_RESULT=6000;
	public static final int BUSLINE_DETAIL_RESULT=6001;
	
	public static final String CONNECT_URL = "192.168.1.113";
	public static final int PORT = 8080;
	
}
