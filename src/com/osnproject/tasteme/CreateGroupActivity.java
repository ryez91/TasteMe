package com.osnproject.tasteme;

import java.util.Random;

import com.osnproject.tasteme.HTTP.RestClientv2;
import com.osnproject.tasteme.Model.Group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGroupActivity extends Activity{
	public static final int MSG_CREATE_SUCESS = 1;
	
	EditText edt_groupName;
	EditText edt_groupDescrib;
	Button btn_submit;
	
	int userId;
	Group newGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_group);
		
		Bundle bundle = getIntent().getExtras();
		userId = bundle.getInt("userid");
		
		edt_groupName = (EditText)findViewById(R.id.etxt_groupcreate_name);
		edt_groupDescrib = (EditText)findViewById(R.id.etxt_groupcreate_descrip);
		btn_submit = (Button)findViewById(R.id.btn_groupcreate_create);
	}
	
	public void createClick(View v){
		String groupName = edt_groupName.getText().toString();
		if(groupName.equals("")){
			Toast.makeText(getApplicationContext(), "Please Input a Name", Toast.LENGTH_SHORT).show();
			return;
		}
		
		String groupDescrib = edt_groupDescrib.getText().toString();
		if(groupDescrib.equals("")){
			Toast.makeText(getApplicationContext(), "Please Input Description", Toast.LENGTH_SHORT).show();
			return;
		}
		
		newGroup = new Group();
		newGroup.setGroupName(groupName);
		newGroup.setDescription(groupDescrib);
		newGroup.setCreatorId(userId);
		
		Random ran = new Random();
		int ranNum = ran.nextInt(10);
		newGroup.setGroupIcon("icon_" + Integer.toString(ranNum) + ".jpg");
		
		createThread thread = new createThread();
		thread.start();
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what == MSG_CREATE_SUCESS){
				Toast.makeText(getApplicationContext(), "Group Created", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();         //just for completeness
				Bundle bundle = new Bundle();
				intent.putExtras(bundle);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		}
	};
	
	public class createThread extends Thread{
		
		@Override
		public void run(){
			RestClientv2 rc = new RestClientv2();
			rc.createNewGroup(newGroup);
			
			Message msg = handler.obtainMessage();
			msg.what = MSG_CREATE_SUCESS;
			handler.sendMessage(msg);
		}
	}
}
