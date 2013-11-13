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
 * �����·��    �Ͻ�˹���� �㷨
 * @author wangxiaoyang
 *
 */

public class ShrotRoute {
	
	private List<Integer> routeNo = null;						//�洢���·����·�α��
	private List<Integer> routeNodeNo = null;					//�洢���·���ϵĽ����
	private List<String> routeNodeName = null;					//�洢���·���ϵĽ������
	private List<M_Point> routeNodeGeo = null;					//�洢���·���ϵĽ�㾭γ��
	private List<String> allNodeName = null;					//���н�������
	private List<M_Point> allNodeGeo = null;					//���н��ľ�γ��
	
	private static float MaxDist = Float.MAX_VALUE;				//���ڳ�ʼ��·�εĳ���
	private float[] dist = null;								//������㵽������̾���  �᲻�ϸ���
	private float[][] nodeDist = null;							//crose[i][j]��ʾ���i��j��һ·�εĳ���
	private boolean[] isShort = null;							//�洢�ҵ������·���Ľ��  true ��ʾ���ҵ����·��
	private int[] lastNode = null;								//path[v] ��ʾ ��� �� v ���·����  v ֮ǰ���Ǹ����
	private int n = 0 ;											//�����
	public int[][] a2bNo = null;								//a2bNo[i][j]Ϊ·�� I-J ��·�α��
	
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
		
		//InputStream BufferedReader �����ڶ�ȡdata
		try {
			InputStream myFile = context.getResources().openRawResource(R.raw.nodenameinfo);
			BufferedReader br = null;
			//ע�����
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
			
			n = (byte) allNodeName.size();			//�ڵ���
			nodeDist = new float[n][n];				//·�γ���
			a2bNo = new int[n][n];					//·�α��
			
			dist = new float[n];					//��������̾���
			isShort = new boolean[n];				//����
			lastNode = new int[n];					//�洢·��
			
			for(int i = 0 ; i < n ; i++){
				for(int j = 0 ; j < n ; j++){
					if(i == j) nodeDist[i][j]= 0;
					else nodeDist[i][j] = MaxDist;
				}
			}
			//routeInfo ����·����Ϣ ��   ·�����  ·���յ�  ·�γ���  �� ·�α�� ���
			
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
		
		//����ϴε�����
		routeNo.clear();
		routeNodeNo.clear();
		routeNodeName.clear();
		routeNodeGeo.clear();
		
		start = startPoint;
		end = endPoint;
		
		int v;
		//��ʼ��dist
		for(v = 0 ; v < n ; v++){
			lastNode[v] = start;
			if(nodeDist[start][v] < MaxDist) dist[v] = nodeDist[start][v];
			else dist[v] = MaxDist;
			isShort[v] = false;
		}
		isShort[start] = true;			//start����s 
		int i = start;				//i���������start�����v
		float min;					//�洢��start�������Сֵ
		//DIJ�㷨
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
				//��� start  �� i �� w �� start ֱ�ӵ� w ��   �޸�  dist[w] �� ���� path[w] Ϊ i ����ʾ  start �� w Ҫ�Ⱦ��� i
				if(!isShort[w] && ( dist[w] > min + nodeDist[i][w] ) ){
					dist[w] = min + nodeDist[i][w];
					lastNode[w] = i;
				}
			}
		}
		
		//�õ����·�������������  ����
		getShortNodes(end);
		
		//�洢���·����·�α��
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
