package com.shiningstone.myweixin;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	private EditText EtUsername;
	private EditText EtPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        EtUsername = (EditText)findViewById(R.id.login_user_edit);
        EtPassword = (EditText)findViewById(R.id.login_passwd_edit);
    }

    public void onBtnLoginClicked(View v) {
    	if("buaa".equals(EtUsername.getText().toString()) && "123".equals(EtPassword.getText().toString()))   //判断 帐号和密码
        {
             //Intent intent = new Intent();
             //intent.setClass(Login.this,LoadingActivity.class);
             //startActivity(intent);
        }
        else if("".equals(EtUsername.getText().toString()) || "".equals(EtPassword.getText().toString()))   //判断 帐号和密码
        {
        	//new AlertDialog.Builder(Login.this)
			//.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
			//.setTitle("登录错误")
			//.setMessage("微信帐号或者密码不能为空，\n请输入后再登录！")
			//.create().show();
        }
        else
        {
        	//new AlertDialog.Builder(Login.this)
			//.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
			//.setTitle("登录失败")
			//.setMessage("微信帐号或者密码不正确，\n请检查后重新输入！")
			//.create().show();
        }
    	
    	//登录按钮
    	/*
      	Intent intent = new Intent();
		intent.setClass(Login.this,Whatsnew.class);
		startActivity(intent);
		Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
		this.finish();*/
    }  
    
    public void onBtnBackClicked(View v) {     //标题栏 返回按钮
      	finish();
    }  
    
    public void onBtnForgetClicked(View v) {     //忘记密码按钮
    	Uri uri = Uri.parse("http://3g.qq.com"); 
    	Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
    	startActivity(intent);
    	//Intent intent = new Intent();
    	//intent.setClass(Login.this,Whatsnew.class);
        //startActivity(intent);
      }  
}
