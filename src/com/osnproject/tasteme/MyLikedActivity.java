package com.osnproject.tasteme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.osnproject.tasteme.HTTP.RestClientv2;
import com.osnproject.tasteme.Model.ActionLike;
import com.osnproject.tasteme.Model.Comment;
import com.osnproject.tasteme.Model.ProductBasicInf;

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

public class MyLikedActivity extends Activity{
	public static final int MSG_RECV_OK = 1;
	public static final int MSG_RECV_FAIL = 2;
	public static final int MSG_RECV_EMPTY = 3;
	
	TextView txt_username;
	TextView txt_description;
	TextView txt_loading;
	ImageView img_icon;
	ListView list_likes;
	
	List<ActionLike> likes;
	int userId;
	int priority;
	int groupid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_liked);
	
		txt_username = (TextView)findViewById(R.id.txt_userliked_name);
		txt_description = (TextView)findViewById(R.id.txt_userliked_description);
		txt_loading = (TextView)findViewById(R.id.txt_userliked_loading);
		img_icon = (ImageView)findViewById(R.id.img_userliked_photo);
		list_likes = (ListView)findViewById(R.id.list_userliked_comments);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		txt_username.setText(bundle.getString("username"));
		txt_description.setText(bundle.getString("description"));
		userId = bundle.getInt("userid");
		priority = bundle.getInt("priority");
		groupid = bundle.getInt("groupid");
		if(intent.getParcelableExtra("image")!=null)
			img_icon.setImageBitmap((Bitmap)intent.getParcelableExtra("image"));
		
		GetLikesThread thread = new GetLikesThread();
		thread.start();
		
	}
	
	public void backClick(View v){
		finish();
	}
	
	public SimpleAdapter likeAdapter(){                      //load all the likes
		ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
		
		for(int i=0;i<likes.size();i++){
			ActionLike like = likes.get(i);
			
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			map.put("name", like.getProductName());
			map.put("description", like.getProductDescription());
			map.put("bitmap", like.getBitmap());
			map.put("productid", like.getProductId());
			
			listItem.add(map);
		}
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(getApplicationContext(), listItem,
				R.layout.listcomp_myliked, new String[]{"name","description","bitmap"},
				new int[] {R.id.txt_myliked_title,R.id.txt_myliked_describ,R.id.img_myliked_img});
		
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
		
		return listItemAdapter;
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
				list_likes.setAdapter(likeAdapter());
				txt_loading.setVisibility(View.INVISIBLE);
				list_likes.setOnItemClickListener(productListListener);
			}
			else if(msg.what == MSG_RECV_EMPTY){
				txt_loading.setText("You haven't liked anyone. \r\nLet's do it!");
			}
		}
	};
	
	public class GetLikesThread extends Thread{
		@Override
		public void run(){
			RestClientv2 rc = new RestClientv2();
			likes = rc.getLikeByUser(userId);
			rc.fillBitmapLikes(likes);
			
			Message msg = handler.obtainMessage();
			if(likes == null)
				msg.what = MSG_RECV_FAIL;
			else if(likes.isEmpty())
				msg.what = MSG_RECV_EMPTY;
			else
				msg.what = MSG_RECV_OK;
			
			handler.sendMessage(msg);
			
		}
	}
}
