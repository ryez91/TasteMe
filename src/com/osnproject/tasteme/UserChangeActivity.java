package com.osnproject.tasteme;

import java.util.List;

import com.osnproject.tasteme.HTTP.RestClientv2;
import com.osnproject.tasteme.Model.User;
import com.osnproject.tasteme.ProductDetailActivity.GetDetailThread;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserChangeActivity extends Activity{
	public static final int MSG_GETUSER = 1;
	public static final int MSG_UPDATEUSER = 2;
	public static final int MSG_RECV_USER = 11;
	public static final int MSG_RECV_UPDATEFINISH = 12;
	public static final int MSG_RECV_FAILED = 0;
	
	public static final int RESULT_CHANGEICON = 1;

	int userId;
	TextView txt_userName;
	TextView txt_userId;
	ImageView img_userIcon;
	EditText edt_age;
	EditText edt_description;
	EditText edt_prefer;
	Button btn_changepassword;
	Button btn_submit;
	
	User userMe;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changeproperty);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		userId = bundle.getInt("userid");
		
		txt_userName=(TextView)findViewById(R.id.txt_userchange_username);
		txt_userId=(TextView)findViewById(R.id.txt_userchange_userid);
		img_userIcon=(ImageView)findViewById(R.id.img_userchange);
		edt_age=(EditText)findViewById(R.id.edt_userchange_age);
		edt_description=(EditText)findViewById(R.id.edt_userchange_describ);
		edt_prefer=(EditText)findViewById(R.id.edt_userchange_prefer);
		btn_changepassword=(Button)findViewById(R.id.btn_userchange_psword);
		btn_submit=(Button)findViewById(R.id.btn_userchange_submit);
		
		btn_changepassword.setEnabled(false);
		btn_submit.setEnabled(false);
		img_userIcon.setOnClickListener(imgClicker);
		
		Intent intentResult = new Intent();
		Bundle bundleResult = new Bundle();
		bundleResult.putInt("UserId", userId);
		intentResult.putExtras(bundleResult);
		setResult(Activity.RESULT_OK, intentResult);
		
		UserThread thread = new UserThread(MSG_GETUSER);
		thread.start();
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(intent==null) return;
		
		Bundle bundle = intent.getExtras();
		switch(requestCode){
		case RESULT_CHANGEICON:                              //after writing comment
			String imgPath = bundle.getString("image");
			userMe.setImage(imgPath);
			Bitmap bitmap = (Bitmap)intent.getParcelableExtra("bitmap");
			img_userIcon.setImageBitmap(bitmap);
			break;
		}
	}
	
	public void backClick(View v){
		finish();
	}
	
	public OnClickListener imgClicker = new View.OnClickListener() {        //change image button clicked
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(), ChangeIconActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("image", userMe.getImage());
			intent.putExtras(bundle);
			startActivityForResult(intent, RESULT_CHANGEICON);
		}
	};
	
	public void changeClick(View v){            //button changepassword and submit
		if(v == btn_changepassword){
			
		}else if(v == btn_submit){
			String str_age = edt_age.getText().toString();
			if(str_age != null && !str_age.equals(""))
				userMe.setAge(Integer.valueOf(str_age));
			
			userMe.setDescription(edt_description.getText().toString());
			userMe.setPreference(edt_prefer.getText().toString());
			
			UserThread thread = new UserThread(MSG_UPDATEUSER);
			thread.start();
		}
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_RECV_USER:
				txt_userName.setText(userMe.getUserName());
				txt_userId.setText("User Id: "+Integer.toString(userMe.getUserId()));
				img_userIcon.setImageBitmap(userMe.getUserImage());
				if(userMe.getAge()!=-1)
					edt_age.setText(Integer.toString(userMe.getAge()));
				if(!userMe.getDescription().contains("null"))
					edt_description.setText(userMe.getDescription());
				if(!userMe.getPreference().contains("null"))
					edt_prefer.setText(userMe.getPreference());
				btn_changepassword.setEnabled(true);
				btn_submit.setEnabled(true);
				break;
			case MSG_RECV_UPDATEFINISH:
				Toast.makeText(getApplicationContext(), "Update Finish!", Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
		}
	};
	
	public class UserThread extends Thread{
		private int action;
		
		public UserThread(int action){
			this.action = action;
		}
		
		@Override
		public void run(){
			switch(action){
			case MSG_GETUSER:
				RestClientv2 rc = new RestClientv2();
				List<User> users = rc.getUserById(userId);
				
				Message msg = handler.obtainMessage();
				
				if(users != null && !users.isEmpty()){
					rc.fillBitmapUsers(users);
					userMe = users.get(0);
					msg.what = MSG_RECV_USER;
				}else{
					msg.what = MSG_RECV_FAILED;
				}
				
				handler.sendMessage(msg);
				break;
				
			case MSG_UPDATEUSER:
				RestClientv2 rc2 = new RestClientv2();
				rc2.updateUser(userMe);
				Message msg2 = handler.obtainMessage();
				msg2.what = MSG_RECV_UPDATEFINISH;
				handler.sendMessage(msg2);
				break;
			}
		}
	}

}
