package com.osnproject.tasteme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.osnproject.tasteme.HTTP.RestClientv2;
import com.osnproject.tasteme.Model.Comment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.SimpleAdapter.ViewBinder;

public class MyCommentsActivity extends Activity{
	public static final int MSG_RECV_OK = 1;
	public static final int MSG_RECV_FAIL = 2;
	public static final int MSG_RECV_EMPTY = 3;
	
	TextView txt_username;
	TextView txt_description;
	TextView txt_loading;
	ImageView img_icon;
	ListView list_comments;
	
	List<Comment> comments;
	int userId;
	int priority;
	int groupid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_said);
	
		txt_username = (TextView)findViewById(R.id.txt_usersaid_name);
		txt_description = (TextView)findViewById(R.id.txt_usersaid_description);
		txt_loading = (TextView)findViewById(R.id.txt_usersaid_loading);
		img_icon = (ImageView)findViewById(R.id.img_usersaid_photo);
		list_comments = (ListView)findViewById(R.id.list_usersaid_comments);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		txt_username.setText(bundle.getString("username"));
		txt_description.setText(bundle.getString("description"));
		userId = bundle.getInt("userid");
		priority = bundle.getInt("priority");
		groupid = bundle.getInt("groupid");
		if(intent.getParcelableExtra("image")!=null)
			img_icon.setImageBitmap((Bitmap)intent.getParcelableExtra("image"));
		
		getCommentsThread thread = new getCommentsThread();
		thread.start();
		
	}
	
	public void backClick(View v){
		finish();
	}
	
	public void fillComments(){
		ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
		
		for(int i=0;i<comments.size();i++){
			Comment comment = comments.get(i);
			
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			String productname = comment.getProductName();
			if(productname.length()>20)
				productname = productname.substring(0, 20) + "..";
			
			map.put("productid", comment.getProductId());
			
			map.put("productname", productname);

			map.put("text", comment.getText());
			
			map.put("date", comment.getDate().substring(0, 10));
			
			if(comment.getBitmap() != null)
				map.put("image", comment.getBitmap());
			else
				map.put("image", R.drawable.no_image);
			
			int score = comment.getScore();
			if(score > 0 && score <= 10){
				map.put("score", R.drawable.rate_1);
			}else if(score > 10 && score <= 20){
				map.put("score", R.drawable.rate_2);
			}else if(score > 20 && score <= 30){
				map.put("score", R.drawable.rate_3);
			}else if(score > 30 && score <= 40){
				map.put("score", R.drawable.rate_4);
			}else if(score > 40 && score <= 50){
				map.put("score", R.drawable.rate_5);
			}else if(score > 50 && score <= 60){
				map.put("score", R.drawable.rate_6);
			}else if(score > 60 && score <= 70){
				map.put("score", R.drawable.rate_7);
			}else if(score > 70 && score <= 80){
				map.put("score", R.drawable.rate_8);
			}else if(score > 80 && score <= 90){
				map.put("score", R.drawable.rate_9);
			}else if(score > 90 && score <= 100){
				map.put("score", R.drawable.rate_10);
			}
			
			listItem.add(map);
		}
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(getApplicationContext(), listItem,
				R.layout.listcomp_mycomments, new String[]{"productname","text","date","image","score"},
				new int[] {R.id.txt_mycmtlist_productname,R.id.txt_mycmtlist_comment,R.id.txt_mycmtlist_time,
			R.id.img_mycmtlist_image,R.id.img_mycmtlist_rate});
		
		listItemAdapter.setViewBinder(new ViewBinder() {  
			public boolean setViewValue(View view, Object data,  
                    String textRepresentation) {  
                
                if(view instanceof ImageView  && data instanceof Bitmap){  
                    ImageView iv = (ImageView) view;  
                  
                    iv.setImageBitmap((Bitmap) data);  
                    return true;  
                }else  
                return false;  
            }
        });  
		
		list_comments.setAdapter(listItemAdapter);
		list_comments.setOnItemClickListener(productListListener);
	}
	
	public AdapterView.OnItemClickListener productListListener = new AdapterView.OnItemClickListener() {             //a product in a list is clicked

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			HashMap<String, Object> item = (HashMap<String, Object>)arg0.getItemAtPosition(arg2); 
			int productId = (Integer)item.get("productid");
			
			Bundle bundle = new Bundle();
			bundle.putInt("productId", productId);
			bundle.putInt("userId", userId);
			bundle.putInt("priority", priority);
			bundle.putInt("groupId", groupid);
			
			Intent intent = new Intent(getApplicationContext(),ProductDetailActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			
		}
		
	};
	
	
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what == MSG_RECV_OK){
				fillComments();
				txt_loading.setVisibility(View.INVISIBLE);
			}else if(msg.what == MSG_RECV_EMPTY){
				txt_loading.setText("You haven't said anything. \r\nTry to write some comments!");
			}
		}
	};
	
	public class getCommentsThread extends Thread{
		@Override
		public void run(){
			RestClientv2 rc = new RestClientv2();
			comments = rc.getCommentByUser(userId);
			rc.fillBitmapComments(comments, Comment.IMAGEFILL_PRODUCT);
			
			Message msg = handler.obtainMessage();
			if(comments == null)
				msg.what = MSG_RECV_FAIL;
			else if(comments.isEmpty())
				msg.what = MSG_RECV_EMPTY;
			else
				msg.what = MSG_RECV_OK;
			
			handler.sendMessage(msg);
			
		}
	}
}
