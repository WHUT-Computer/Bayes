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
 * 找最短路径    迪杰斯特拉 算法
 * @author wangxiaoyang
 *
 */

public class ShrotRoute {
	
	private List<Integer> routeNo = null;						//存储最短路径的路段编号
	private List<Integer> routeNodeNo = null;					//存储最短路径上的结点编号
	private List<String> routeNodeName = null;					//存储最短路径上的结点名称
	private List<M_Point> routeNodeGeo = null;					//存储最短路径上的结点经纬度
	private List<String> allNodeName = null;					//所有结点的名称
	private List<M_Point> allNodeGeo = null;					//所有结点的经纬度
	
	private static float MaxDist = Float.MAX_VALUE;				//用于初始化路段的长度
	private float[] dist = null;								//各个结点到起点的最短距离  会不断更新
	private float[][] nodeDist = null;							//crose[i][j]表示结点i到j这一路段的长度
	private boolean[] isShort = null;							//存储找到了最短路径的结点  true 表示已找到最短路径
	private int[] lastNode = null;								//path[v] 表示 起点 到 v 最短路径上  v 之前的那个结点
	private int n = 0 ;											//结点数
	public int[][] a2bNo = null;								//a2bNo[i][j]为路段 I-J 的路段编号
	
	private int start = 0;
	private int end = 0;
	
	public ShrotRoute(Context context){
		init(context);
	}
	
	public void init(Context context){
		
		routeNo = new ArrayList<Integer>();
		routeNodeNo = new ArrayList<Integer>();
		routeNodeName = new ArrayList< String >();
		routeNodeGeo = new ArrayList<M_Point>();
		
		allNodeName = new ArrayList< String >();
		allNodeGeo = new ArrayList<M_Point>();
		
		//InputStream BufferedReader 流用于读取data
		try {
			InputStream myFile = context.getResources().openRawResource(R.raw.nodenameinfo);
			BufferedReader br = null;
			//注意编码
			br = new BufferedReader(new InputStreamReader(myFile,"gb2312"));
			String strTemp;
			M_Point mPoint;
			while(! ((strTemp = br.readLine()) == null)){	
				String[] temp = strTemp.split(",");
				allNodeName.add(temp[0]);
				mPoint = new M_Point(Double.parseDouble(temp[1]), Double.parseDouble(temp[2]));
				allNodeGeo.add(mPoint);
			}
			br.close();
			myFile.close();
			
			n = (byte) allNodeName.size();			//节点数
			nodeDist = new float[n][n];				//路段长度
			a2bNo = new int[n][n];					//路段编号
			
			dist = new float[n];					//到起点的最短距离
			isShort = new boolean[n];				//队列
			lastNode = new int[n];					//存储路径
			
			for(int i = 0 ; i < n ; i++){
				for(int j = 0 ; j < n ; j++){
					if(i == j) nodeDist[i][j]= 0;
					else nodeDist[i][j] = MaxDist;
				}
			}
			//routeInfo 包含路段信息 由   路段起点  路段终点  路段长度  和 路段编号 组成
			
			myFile = context.getResources().openRawResource(R.raw.routeinfo);
			br = new BufferedReader(new InputStreamReader(myFile,"gb2312"));
			
			while(! ((strTemp = br.readLine()) == null) ){
				String[] temp = strTemp.split(",");
				initRouteData(temp[0], temp[1], Float.parseFloat(temp[2]),Integer.parseInt(temp[3]));
			}
			
			br.close();
			myFile.close();
			
		} catch (UnsupportedEncodingException e) {
			Log.e("debug",e.toString());
		}
		catch(IOException e){
			Log.e("debug",e.toString());
		}
	}
	
	int i , j;
	public void initRouteData(String A,String B, float dist , int No){
		i = allNodeName.indexOf(A);
		j = allNodeName.indexOf(B);
		nodeDist[i][j]= nodeDist[j][i] = dist;
		a2bNo[i][j] = a2bNo[j][i] = No;
	}
	
	public void fixPath(String startPoint,String endPoint){
		start = allNodeName.indexOf(startPoint);
		end = allNodeName.indexOf(endPoint);
		this.fixPath(start, end);
	}
	
	public void fixPath(int startPoint,int endPoint){
		
		//清除上次的数据
		routeNo.clear();
		routeNodeNo.clear();
		routeNodeName.clear();
		routeNodeGeo.clear();
		
		start = startPoint;
		end = endPoint;
		
		int v;
		//初始化dist
		for(v = 0 ; v < n ; v++){
			lastNode[v] = start;
			if(nodeDist[start][v] < MaxDist) dist[v] = nodeDist[start][v];
			else dist[v] = MaxDist;
			isShort[v] = false;
		}
		isShort[start] = true;			//start加入s 
		int i = start;				//i用来标记离start最近的v
		float min;					//存储离start距离的最小值
		//DIJ算法
		for(int k = 0; k < n ; k++){
			min = MaxDist;
			for(v = 0 ; v < n ; v++ ){
				if( !isShort[v] && ( dist[v] < min )){
					i = v;
					min = dist[v];
				}
			}
			isShort[i] = true;
			for(int w = 0 ; w < n; w++){
				//如果 start  经 i 到 w 比 start 直接到 w 近   修改  dist[w] ， 并将 path[w] 为 i ，表示  start 到 w 要先经过 i
				if(!isShort[w] && ( dist[w] > min + nodeDist[i][w] ) ){
					dist[w] = min + nodeDist[i][w];
					lastNode[w] = i;
				}
			}
		}
		
		//得到最短路径各顶点的名称  坐标
		getShortNodes(end);
		
		//存储最短路径的路段编号
		int nn = routeNodeName.size()-1;
		int c = 0;
		int point ;
		int nextpoint;
		for(c = 0 ; c < nn ; c++ ){
			Log.d("debug", " " + c );
			point =  routeNodeNo.get( c );
			nextpoint =  routeNodeNo.get( c+1 );
			routeNo.add( a2bNo[point][nextpoint] );
		}
	}
	
	public void getShortNodes(int i){
		if(lastNode[i] != start) getShortNodes(lastNode[i]);
		else {
			Log.d("debug",allNodeName.get(start));
			routeNodeNo.add(start);
			routeNodeName.add(allNodeName.get(start));
			routeNodeGeo.add(allNodeGeo.get(start));
		}
		Log.d("debug",allNodeName.get(i));
		routeNodeNo.add(i);
		routeNodeName.add(allNodeName.get(i));
		routeNodeGeo.add(allNodeGeo.get(i));
	}
	
	public List< Integer > getRouteNo(){
		return routeNo;
	}
	
	public List<String> getShortestRoute(){
		return routeNodeName;
	}
	
	public int[] getRouteNodeNo(){
		int n = routeNodeName.size();
		int[] routeNodeNo = new int[n];
		for(int i = 0 ; i < n ; i++){
			routeNodeNo[i] = allNodeName.indexOf(routeNodeName.get(i));
		}
		return routeNodeNo;
	}
	
	public List<String> getNodeName(){
		return allNodeName;
	}
	
	public List<M_Point> getNodeGeo(){
		return allNodeGeo;
	}
	
	public List<M_Point> getRouteGeo(){
		return routeNodeGeo;
	}
	
}
