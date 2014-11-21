package com.osnproject.tasteme;

import java.util.List;
import java.util.Random;

import com.osnproject.tasteme.HTTP.RestClientv2;
import com.osnproject.tasteme.Model.MD5;
import com.osnproject.tasteme.Model.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class UserSignupActivity extends Activity{
	public static final int MSG_CREATESUCCESS = 1;
	public static final int MSG_CHECKSUCCESS = 2;
	
	View view_dialog_1;
	View view_dialog_2;
	View view_dialog_3;
	TextView t_Title;
	TextView t_State;
	EditText e_Username;
	EditText e_Password;
	EditText e_PasswordConf;
	Button b_Next;
	SeekBar seek_Sour;
	SeekBar seek_Sweet;
	SeekBar seek_Bitter;
	SeekBar seek_Spicy;
	SeekBar seek_Salty;
	Button b_Finish;
	
	String username, password;
	
	User newUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_signup);
		
		view_dialog_1 = findViewById(R.id.view_create_dialog1);
		view_dialog_2 = findViewById(R.id.view_create_dialog2);
		view_dialog_3 = findViewById(R.id.view_create_dialog3);
		t_Title = (TextView)findViewById(R.id.user_txt_signuptitle);
		t_State = (TextView)findViewById(R.id.txt_create_state);
		e_Username = (EditText)findViewById(R.id.edt_create_name);
		e_Password = (EditText)findViewById(R.id.edt_create_pass);
		e_PasswordConf = (EditText)findViewById(R.id.edt_create_passconf);
		b_Next = (Button)findViewById(R.id.btn_create_next);
		b_Finish = (Button)findViewById(R.id.btn_create_finish);
		seek_Sour = (SeekBar)findViewById(R.id.user_seek_sour);
		seek_Sweet = (SeekBar)findViewById(R.id.user_seek_sweet);
		seek_Bitter = (SeekBar)findViewById(R.id.user_seek_bitter);
		seek_Spicy = (SeekBar)findViewById(R.id.user_seek_spicy);
		seek_Salty = (SeekBar)findViewById(R.id.user_seek_salty);
		
		view_dialog_2.setVisibility(View.INVISIBLE);
		view_dialog_3.setVisibility(View.INVISIBLE);
		t_Title.setText("Create an Account (1/2)");
	}
	
	public void signupClick(View v){
		if(v == b_Next){
			username = e_Username.getText().toString();
			if(username == null || username.length() == 0){
				Toast.makeText(getApplicationContext(), "Please Input a Username", Toast.LENGTH_SHORT).show();
				return;
			}
			if(username.contains(" ")){
				Toast.makeText(getApplicationContext(), "Please do not contain space", Toast.LENGTH_SHORT).show();
				return;
			}
			
			password = e_Password.getText().toString();
			if(password == null || password.length() == 0){
				Toast.makeText(getApplicationContext(), "Please Input a Password", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if(!password.equals(e_PasswordConf.getText().toString())){
				Toast.makeText(getApplicationContext(), "Password does't Match", Toast.LENGTH_SHORT).show();
				return;
			}
			
			Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha_hide);
			view_dialog_1.startAnimation(animation);
			view_dialog_1.setVisibility(View.INVISIBLE);
			
			animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha_show);
			view_dialog_2.startAnimation(animation);
			view_dialog_2.setVisibility(View.VISIBLE);
			
			t_Title.setText("Create an Account (2/2)");
			
		}
		else if(v == b_Finish){
			int pro_Sour = seek_Sour.getProgress();
			int pro_Sweet = seek_Sweet.getProgress();
			int pro_Bitter = seek_Bitter.getProgress();
			int pro_Spicy = seek_Spicy.getProgress();
			int pro_Salty = seek_Salty.getProgress();
			
			MD5 md5 = new MD5();           //encode password
			password = md5.md5encode(password);
			
			newUser = new User();
			newUser.initUser(username, password, pro_Sour, pro_Sweet, pro_Bitter, pro_Spicy, pro_Salty);
			newUser.setPriority(User.PRI_USER);
			Random ran = new Random();
			int ranNum = ran.nextInt(31);
			newUser.setImage("icon_" + Integer.toString(ranNum) + ".png");
			
			if(pro_Sour == 50 && pro_Sweet == 50 && pro_Bitter == 50 && pro_Spicy == 50 && pro_Salty == 50){
				String msgShow = "Telling us your preference will help us offer you better service."
						+ "\r\n\r\n" + "Create the account anyway?";
				AlertDialog.Builder createDialog=new AlertDialog.Builder(UserSignupActivity.this);
				createDialog.setIcon(android.R.drawable.ic_dialog_info);
				createDialog.setTitle("Create");
				createDialog.setMessage(msgShow);
				createDialog.setPositiveButton("Yes, Create", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						createUser();
					}
				});
				createDialog.setNegativeButton("Cancel", null);
				createDialog.show();
			}else
				createUser();
		}
	}
	
	public void createUser(){
		Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha_hide);
		view_dialog_2.startAnimation(animation);
		view_dialog_2.setVisibility(View.INVISIBLE);
		
		animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha_show);
		view_dialog_3.startAnimation(animation);
		view_dialog_3.setVisibility(View.VISIBLE);
		
		createThread thread = new createThread();
		thread.start();
	}
	
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what == MSG_CREATESUCCESS){
				t_State.setText("Checking the Account...");
				checkThread thread = new checkThread();
				thread.start();
			}
			else if(msg.what == MSG_CHECKSUCCESS){
				t_State.setText("Account Created");
				
				Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha_show);
				view_dialog_3.startAnimation(animation);
				view_dialog_3.setVisibility(View.VISIBLE);
				
				Bundle bundle = new Bundle();
				User user = (User) msg.obj;
				bundle.putInt("UserId", user.getUserId());
				bundle.putString("Username", user.getUserName());
				bundle.putInt("GroupId", user.getGroupId());
				bundle.putInt("priority", user.getPriority());
				Intent intent = new Intent();
				intent.putExtras(bundle);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		}
	};
	
	public class checkThread extends Thread{
		@Override
		public void run(){
			RestClientv2 rc = new RestClientv2();
			List<User> users = rc.getUserByNamePWD(username, password);
			if(users != null && !users.isEmpty()){
				
				Message msg = handler.obtainMessage();
				msg.what = MSG_CHECKSUCCESS;
				msg.obj = users.get(0);
				handler.sendMessage(msg);
			}
		}
	}
	
	public class createThread extends Thread{
		
		@Override
		public void run(){
			RestClientv2 rc = new RestClientv2();
			rc.createNewUser(newUser);
			
			Message msg = handler.obtainMessage();
			msg.what = MSG_CREATESUCCESS;
			handler.sendMessage(msg);
		}
	}
	
	
}
