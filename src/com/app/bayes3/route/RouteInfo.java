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
 * 获取路段编号，结点等信息
 */
public class RouteInfo {
	
	//List<Gpoint> 存储各个路段上的点的经纬度
	//List< List<Gpoint> > 存储路段
	private List< List<M_Point> > route = null;
	
	public RouteInfo(Context context) {
		
		route = new ArrayList< List<M_Point> >();
		
		//InputStream BufferedReader 流用于读取data
		InputStream myFile = context.getResources().openRawResource(R.raw.data);
		BufferedReader br = null;
		String strtemp = null;
		
		try {
			//注意编码
			br = new BufferedReader(new InputStreamReader(myFile,"gb2312"));
		} catch (UnsupportedEncodingException e1) {
			Log.e("debug",e1.toString());
		}
		
		try {
			strtemp = br.readLine();
			while (true) {
				//p为某一路段的数据开始的标志
				if(strtemp.substring(0, 1).equals("p")){
					// points存储该路段上的点的经纬度
					List<M_Point> points = new ArrayList<M_Point>();
					if((strtemp = br.readLine()) == null) break;
					while( (!strtemp.substring(0, 1).equals("p")) ){
						//split() 可按照指定的符号将指定String切割
						String[] temp = strtemp.split(",");
						//创建点，该点会在绘制时被转化为屏幕上的坐标
						M_Point point = new M_Point( 
								Double.parseDouble(temp[0]) , Double.parseDouble(temp[1]) );
						points.add(point);
						//该路段的数据读完，跳出循环
						if((strtemp = br.readLine()) == null ) break;
					}
					//将该路段的经纬度加入到路段列表中
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
