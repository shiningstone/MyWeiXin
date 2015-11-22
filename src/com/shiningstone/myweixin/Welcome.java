package com.shiningstone.myweixin;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class Welcome extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
    }
    public void onBtnLoginClicked(View v) {  
      	Intent intent = new Intent();
		intent.setClass(Welcome.this,Login.class);
		startActivity(intent);
      }  
    public void onBtnRegisterClicked(View v) {  
      	Intent intent = new Intent();
		intent.setClass(Welcome.this,MainWeixin.class);
		startActivity(intent);
      }  
   
}
