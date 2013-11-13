package com.app.bayes3.route;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.app.bayes3.R;

import android.content.Context;
import android.util.Log;

/**
 * @author wangxiaoyang
 * ��ȡ·�α�ţ�������Ϣ
 */
public class RouteInfo {
	
	//List<Gpoint> �洢����·���ϵĵ�ľ�γ��
	//List< List<Gpoint> > �洢·��
	private List< List<M_Point> > route = null;
	
	public RouteInfo(Context context) {
		
		route = new ArrayList< List<M_Point> >();
		
		//InputStream BufferedReader �����ڶ�ȡdata
		InputStream myFile = context.getResources().openRawResource(R.raw.data);
		BufferedReader br = null;
		String strtemp = null;
		
		try {
			//ע�����
			br = new BufferedReader(new InputStreamReader(myFile,"gb2312"));
		} catch (UnsupportedEncodingException e1) {
			Log.e("debug",e1.toString());
		}
		
		try {
			strtemp = br.readLine();
			while (true) {
				//pΪĳһ·�ε����ݿ�ʼ�ı�־
				if(strtemp.substring(0, 1).equals("p")){
					// points�洢��·���ϵĵ�ľ�γ��
					List<M_Point> points = new ArrayList<M_Point>();
					if((strtemp = br.readLine()) == null) break;
					while( (!strtemp.substring(0, 1).equals("p")) ){
						//split() �ɰ���ָ���ķ��Ž�ָ��String�и�
						String[] temp = strtemp.split(",");
						//�����㣬�õ���ڻ���ʱ��ת��Ϊ��Ļ�ϵ�����
						M_Point point = new M_Point( 
								Double.parseDouble(temp[0]) , Double.parseDouble(temp[1]) );
						points.add(point);
						//��·�ε����ݶ��꣬����ѭ��
						if((strtemp = br.readLine()) == null ) break;
					}
					//����·�εľ�γ�ȼ��뵽·���б���
					route.add(points);
				}
				if(strtemp == null) break;
			}
			br.close();
			myFile.close();
		} catch (IOException e) {
			Log.e("debug",e.toString());
		}
	}
	
	public List< List<M_Point> > getRoutes(){
		return route;
	}
	
}
