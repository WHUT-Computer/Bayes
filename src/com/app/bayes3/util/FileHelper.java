/**
 * �����������ǵ������ݿ�
 * @author wangxiaoyang
 */

package com.app.bayes3.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.app.bayes3.R;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.util.Log;

public class FileHelper {
	
	//Environment.getExternalStorageDirectory().getPath()��ȡSDcard·��
	private static String sdcard = Environment.getExternalStorageDirectory().getPath().trim().toString();
	
	//��������ݿ��ļ���
	public static  String DB_NAME = "location.db"; 	
	
	//���ݿ�İ�װ·��
	public static  String DB_PATH = sdcard + "/Bayes/db";
	
	//���ݿ����ֻ��������ݿ��λ��
	private static String DB = DB_PATH + "/" + DB_NAME;  
	
	private static File mFile;
	private static File mParent;
	
	//�������ݿ�
	public static boolean importDB(Context context){
		if(! Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
			return false;
		}
		mParent = new File(DB_PATH);
		mFile = new File(mParent,DB_NAME);
		//�ж����ݿ��ļ��Ƿ���ڣ�����������ִ�е��룬����ֱ�Ӵ����ݿ�
		if ( !mFile.exists() ) {
			InputStream mInputStream = context.getResources().openRawResource(R.raw.location); 
			createFile(context, mInputStream, mFile);
		}
		return mFile.exists();
	}
	
	//��ȡ���ݿ�
	public static SQLiteDatabase getDatabase(){
		if(! Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
			return null;
		}
		SQLiteDatabase db = SQLiteDatabase.openDatabase( DB , null,SQLiteDatabase.OPEN_READWRITE );
		Log.d("debug", "db isReadOnly "+(db.isReadOnly()));
		return db;
	}
	
	public Images getImage(String Imgpath){
		if(! Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ){
			return null;
		}
		Images mImages = new Images();
		return mImages;
	}
	
	private static void createFile(Context context,InputStream mInputStream ,File file){
		try {
			// Make sure the Parent directory exists.
			mParent.mkdirs();
			// Very simple code to copy a picture from the application's
			// resource into the external file.  Note that this code does
			// no error checking, and assumes the picture is small (does not
			// try to copy it in chunks).  Note that if external storage is
			// not currently mounted this will silently fail.
			InputStream is = mInputStream;
			OutputStream os = new FileOutputStream(file);
			byte[] data = new byte[is.available()];
			is.read(data);
			os.write(data);
			is.close();
			os.close();
			Log.d("debug", "file = "+file.getPath());
			// Tell the media scanner about the new file so that it is
			// immediately available to the user.
			MediaScannerConnection.scanFile(
					context,
					new String[] { file.toString() }, null,
					new MediaScannerConnection.OnScanCompletedListener() {
						@Override
						public void onScanCompleted(String path, Uri uri) {
							Log.d("debug", "Scanned " + path + ":");
							Log.d("debug", "-> uri=" + uri);
						}
					});
		} catch (IOException e) {
			// Unable to create file, likely because external storage is
			// not currently mounted.
			Log.d("debug", "Error writing " + mFile, e);
		}
	}
	
	
}


