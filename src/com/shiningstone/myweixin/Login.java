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
    	if("buaa".equals(EtUsername.getText().toString()) && "123".equals(EtPassword.getText().toString()))   //�ж� �ʺź�����
        {
             //Intent intent = new Intent();
             //intent.setClass(Login.this,LoadingActivity.class);
             //startActivity(intent);
        }
        else if("".equals(EtUsername.getText().toString()) || "".equals(EtPassword.getText().toString()))   //�ж� �ʺź�����
        {
        	//new AlertDialog.Builder(Login.this)
			//.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
			//.setTitle("��¼����")
			//.setMessage("΢���ʺŻ������벻��Ϊ�գ�\n��������ٵ�¼��")
			//.create().show();
        }
        else
        {
        	//new AlertDialog.Builder(Login.this)
			//.setIcon(getResources().getDrawable(R.drawable.login_error_icon))
			//.setTitle("��¼ʧ��")
			//.setMessage("΢���ʺŻ������벻��ȷ��\n������������룡")
			//.create().show();
        }
    	
    	//��¼��ť
    	/*
      	Intent intent = new Intent();
		intent.setClass(Login.this,Whatsnew.class);
		startActivity(intent);
		Toast.makeText(getApplicationContext(), "��¼�ɹ�", Toast.LENGTH_SHORT).show();
		this.finish();*/
    }  
    
    public void onBtnBackClicked(View v) {     //������ ���ذ�ť
      	finish();
    }  
    
    public void onBtnForgetClicked(View v) {     //�������밴ť
    	Uri uri = Uri.parse("http://3g.qq.com"); 
    	Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
    	startActivity(intent);
    	//Intent intent = new Intent();
    	//intent.setClass(Login.this,Whatsnew.class);
        //startActivity(intent);
      }  
}
