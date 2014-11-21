package com.osnproject.tasteme;

import java.util.List;

import com.osnproject.tasteme.HTTP.RestClientv2;
import com.osnproject.tasteme.Model.MD5;
import com.osnproject.tasteme.Model.User;
import com.osnproject.tasteme.UserActivity.GetUserThread;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserLoginActivity extends Activity{
	public static final int MSG_USERFOUND = 1;
	public static final int MSG_USERNOTFOUND = 2;
	public static final int RESULT_USERCREATE = 1;
	
	EditText edt_Username;
	EditText edt_Password;
	Button btn_Login;
	Button btn_Signup;
	
	User user_got;
	String name = null;
	String pswrd = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy); 
        
        edt_Username = (EditText)findViewById(R.id.edt_create_name);
        edt_Password = (EditText)findViewById(R.id.edt_create_pass);
        btn_Login = (Button)findViewById(R.id.btn_login);
        btn_Signup = (Button)findViewById(R.id.btn_signup);
        
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(intent == null) return;
		
		Bundle bundle = intent.getExtras();
		switch(requestCode){
		case RESULT_USERCREATE:
			Intent newintent = new Intent();
			newintent.putExtras(bundle);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		}
		
	}
	
	public void loginClick(View v){
		if(v == btn_Login){
			name = edt_Username.getText().toString();
			pswrd = edt_Password.getText().toString();
			
			if(name.contains(" ")){
				Toast.makeText(getApplicationContext(), "Please do not contain space", Toast.LENGTH_SHORT).show();
				return;
			}
			
			MD5 md5 = new MD5();
			pswrd = md5.md5encode(pswrd);
			
			getThread thread = new getThread();
			thread.start();
		}
		else if(v == btn_Signup){
			Intent intent = new Intent(getApplicationContext(), UserSignupActivity.class);
			startActivityForResult(intent, RESULT_USERCREATE);
		}
	}
	
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what == MSG_USERNOTFOUND){
				Toast.makeText(getApplicationContext(), "Username or Password is Incorrect. Please try again.", Toast.LENGTH_LONG).show();
			}
			else if(msg.what == MSG_USERFOUND){
				Bundle bundle = new Bundle();
				bundle.putInt("UserId", user_got.getUserId());
				bundle.putString("Username", user_got.getUserName());
				bundle.putInt("GroupId", user_got.getGroupId());
				bundle.putInt("priority", user_got.getPriority());
				Intent intent = new Intent();
				intent.putExtras(bundle);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		}
	};
	
	public class getThread extends Thread{
		@Override
		public void run(){
			RestClientv2 rc = new RestClientv2();
			List<User> users = rc.getUserByNamePWD(name, pswrd);
			
			Message msg = handler.obtainMessage();
			if(users == null || users.isEmpty()){
				msg.what = MSG_USERNOTFOUND;
				handler.sendMessage(msg);
			}
			else{
				user_got = users.get(0);
				msg.what = MSG_USERFOUND;
				handler.sendMessage(msg);
			}
		}
	}

}
