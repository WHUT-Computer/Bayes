
package com.app.bayes3.route;

/**
 * 负责与服务器的通讯
 * 通讯格式采用Json
 * 
 * 客户端发送格式：
 * 	{  
 * 		"start":{["startID":"" , "start_lon":"" , "start_lat":"" , "start_name" : ""]},   
 * 		"end":{["endID":"" , "end_lon":"" , "end_lat":"" , "end_name" : ""]} 
 * 	}  
 * 
 * 	客户端接受格式：
 * 	{
 * 		"line1":
 * 		[
 * 			{ "lon":"" , "lat":"" },
 *  		{ "lon":"" , "lat":"" },
 *   		{ "lon":"" , "lat":"" }
 * 		],
 * 		"line2":
 * 		[
 * 			{ "lon":"" , "lat":"" },
 *  		{ "lon":"" , "lat":"" },
 *   		{ "lon":"" , "lat":"" }
 * 		]
 * 	}
 * @author wangxiaoyang
 * 2013-1-2
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.app.bayes3.util.Utils;

import android.util.Log;

public class ConnectServers {
	
	private Socket socket = null;  
	private String message = null;//保存起点与终点
	private PrintWriter pushMsg = null;
	private BufferedReader getMsg = null;
	private static String CONNECT_URL = Utils.CONNECT_URL;
	private int PORT = Utils.PORT;
	
	public ConnectServers(int start, int end) {
		message = "" + start + "," + end;
	}

	//获取IP
	public static void setConUrl(String str)
	{
	    CONNECT_URL=str;
	}
	
	public String getResult() {
		try{
			//创建客户端socket,注意:不能用localhost或127.0.0.1，Android模拟器把自己作为localhost
			socket = new Socket(CONNECT_URL, PORT);
			Log.d("debug", "socket start ");
			
			//发送数据
			pushMsg = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
					true);
			pushMsg.println(message);
			Log.d("debug", "pushMsg : " + message);
			
			//接收数据
			message = "";
            getMsg = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String str = null;
            while ((str = getMsg.readLine()) != null) {
            	message += str;
            }
            Log.d("debug", "getMsg : " + message);
            
        } catch (UnknownHostException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
        finally{
        	try {
        		if (null != socket) 
        			socket.close();  
        		pushMsg.close();  
                getMsg.close(); 
            } catch (IOException e) {
                e.printStackTrace();  
            }  
        }
		return message.trim();
	}
	
}
