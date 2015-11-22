package com.shiningstone.myweixin;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;

public class Appstart extends Activity{
	private static int SHOW_TIME_ms = 1000;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.appstart);
		
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run(){
				Intent intent = new Intent (Appstart.this,Welcome.class);			
				startActivity(intent);
				finish();
			}
		}, SHOW_TIME_ms);
   }
}