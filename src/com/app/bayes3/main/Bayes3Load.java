package com.app.bayes3.main;

import com.app.bayes3.R;
import com.app.bayes3.util.FileHelper;
import com.app.bayes3.util.Utils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class Bayes3Load extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_view);
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Intent main = new Intent(Bayes3Load.this, MainView.class);
				startActivity(main);
				overridePendingTransition(R.anim.zoom_exit, R.anim.zoom_enter);
				finish();
			}
		}).start();
		
		if(!FileHelper.importDB(this)){
			this.finish();
		}
		Utils.db = FileHelper.getDatabase();
		
	}

}
