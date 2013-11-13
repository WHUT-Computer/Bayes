
package com.app.bayes3.route;

/**
 * �������������ͨѶ
 * ͨѶ��ʽ����Json
 * 
 * �ͻ��˷��͸�ʽ��
 * 	{  
 * 		"start":{["startID":"" , "start_lon":"" , "start_lat":"" , "start_name" : ""]},   
 * 		"end":{["endID":"" , "end_lon":"" , "end_lat":"" , "end_name" : ""]} 
 * 	}  
 * 
 * 	�ͻ��˽��ܸ�ʽ��
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
	private String message = null;//����������յ�
	private PrintWriter pushMsg = null;
	private BufferedReader getMsg = null;
	private static String CONNECT_URL = Utils.CONNECT_URL;
	private int PORT = Utils.PORT;
	
	public ConnectServers(int start, int end) {
		message = "" + start + "," + end;
	}

	//��ȡIP
	public static void setConUrl(String str)
	{
	    CONNECT_URL=str;
	}
	
	public String getResult() {
		try{
			//�����ͻ���socket,ע��:������localhost��127.0.0.1��Androidģ�������Լ���Ϊlocalhost
			socket = new Socket(CONNECT_URL, PORT);
			Log.d("debug", "socket start ");
			
			//��������
			pushMsg = new PrintWriter(
					new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())),
					true);
			pushMsg.println(message);
			Log.d("debug", "pushMsg : " + message);
			
			//��������
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
