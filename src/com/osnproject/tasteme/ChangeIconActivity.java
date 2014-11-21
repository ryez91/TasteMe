package com.osnproject.tasteme;

import java.io.InputStream;
import java.net.URL;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChangeIconActivity extends Activity{
	public static final int MSG_IMGGOT = 1;
	
	ImageView img_icon;
	TextView txt_title;
	Button btn_tryagain;
	Button btn_submit;
	
	Bitmap bitmap;
	String imgPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changeicon);
		
		img_icon = (ImageView)findViewById(R.id.img_user_changeicon);
		txt_title = (TextView)findViewById(R.id.txt_user_changeicon_title);
		btn_tryagain = (Button)findViewById(R.id.btn_user_changeicon_retry);
		btn_submit = (Button)findViewById(R.id.btn_user_changeicon_ok);
		
		Bundle bundle = getIntent().getExtras();
		imgPath = bundle.getString("image");
		
		imageThread thread = new imageThread();
		thread.start();
	}
	
	public void btnClick(View v){            //button is clicked
		if(v == btn_tryagain){
			btn_tryagain.setEnabled(false);
			txt_title.setText("Picking for You...");
			Random ran = new Random();
			int ranNum = ran.nextInt(31);
			imgPath = "icon_" + Integer.toString(ranNum) + ".png";
			
			imageThread thread = new imageThread();
			thread.start();
		}
		else{
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("image", imgPath);
			intent.putExtras(bundle);
			intent.putExtra("bitmap", bitmap);
			setResult(Activity.RESULT_OK,intent);
			finish();
		}
	}
	
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what==MSG_IMGGOT){
				btn_tryagain.setEnabled(true);
				img_icon.setImageBitmap(bitmap);
				txt_title.setText("Your Picture Is");
			}
		}
	};
	
	public class imageThread extends Thread{
	
		@Override
		public void run(){
			try{  
	            URL url=new URL("http://24.91.28.7:4001/userimages/" + imgPath);  
	             
	            InputStream is=url.openStream();  
	            
	            bitmap=BitmapFactory.decodeStream(is);  
	          
	            is.close();  
	            
	            Message msg = handler.obtainMessage();
	            msg.what = MSG_IMGGOT;
	            handler.sendMessage(msg);
	            
	        }  
	       catch(Exception ex)  
	       {  
	           ex.printStackTrace();  
	       }  
			
		}
	}
	
	
}
