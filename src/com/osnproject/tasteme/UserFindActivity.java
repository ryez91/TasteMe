package com.osnproject.tasteme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.osnproject.tasteme.HTTP.RestClientv2;
import com.osnproject.tasteme.Model.ActionLike;
import com.osnproject.tasteme.Model.ProductBasicInf;
import com.osnproject.tasteme.UserActivity.GetUserThread;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class UserFindActivity extends Activity{
	public static final int RESULT_USERLOGIN = 1;
	public static final int MSG_GET_LIKES = 1;
	public static final int MSG_GET_PRODUCT = 2;
	
	public static final int MSG_RECV_LIKES = 11;
	public static final int MSG_RECV_PRODUCT = 12;
	
	int userId = -1;
	List<ActionLike> mylikes;          //the list of like of the user logged in
	List<ActionLike> likes;            //a list of likes for all users
	List<ProductBasicInf> products;   //result products
	
	TextView txt_title;
	TextView txt_start;
	Button btn_start;
	ListView list_product;
	View view_result;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find);
		
		txt_title = (TextView)findViewById(R.id.txt_find_title);
		txt_start = (TextView)findViewById(R.id.txt_find_start);
		btn_start = (Button)findViewById(R.id.btn_find_start);
		list_product = (ListView)findViewById(R.id.list_find_list);
		view_result = findViewById(R.id.view_find_result);
		
		view_result.setVisibility(View.INVISIBLE);
		
		//Get the user information
		Bundle mybundle = this.getIntent().getExtras();
		if(mybundle != null)
			userId = mybundle.getInt("UserId");
		
		if(userId == -1){         //no user
			Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
			startActivityForResult(intent, RESULT_USERLOGIN);
		}else{
			
		}
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {         //to get the login information
		if(intent == null){
			//TODO: when user login canceled
			return;
		}
		
		Bundle bundle = intent.getExtras();
		switch(requestCode){
		case RESULT_USERLOGIN:
			userId = bundle.getInt("UserId");
			setResult(Activity.RESULT_OK, intent);
			//When login successes
			break;
		}
	}
	
	/*************************          The Algorithm    *******************************/
	/*
	 * Input: a list of likes for all user and current user
	 * Output: a list of ranked product
	 */
	
	public int[] recommentAlgorithm(){
		List<UserLikes> users = new ArrayList<UserLikes>();
		List<Recommend> recommends = new ArrayList<Recommend>();
		
		
		for(int i=0;i<likes.size();i++){
			ActionLike like = likes.get(i);
			if(like.getUserId() == userId)
				continue;
			
			if(users.isEmpty()){
				UserLikes user = new UserLikes();
				
				user.userId = like.getUserId();
				user.productIds.add(like.getProductId());
				users.add(user);
	
			}else{
				boolean flag = false;
				for(int j=0;j<users.size();j++){
					UserLikes thisUser = users.get(j);
					if(thisUser.userId == like.getUserId()){      //this user is already exist
						thisUser.productIds.add(like.getProductId());
						flag = true;             //to mark that the user isfound
						break;
					}
				}
				
				if(flag == false){        //if after loop, the user is still not found
					UserLikes user = new UserLikes();         //just create it
					
					user.userId = like.getUserId();
					user.productIds.add(like.getProductId());
					users.add(user);
				}
			}
			
			
			
		}
		
		
		ActionLike mylike = new ActionLike();
		int myproduct;
		UserLikes user = new UserLikes();
		
		//This step compares every user with the current logged-in user
		//and calculate the duplicated products of every user with the current one
		//saved the result in "count" field of each user
		
		for(int i=0;i<mylikes.size();i++){
			mylike = mylikes.get(i);
			myproduct = mylike.getProductId();
			for(int j=0;j<users.size();j++){
				
				user = users.get(j);
				for(int k=0;k<user.productIds.size();k++){
					int idnow = user.productIds.get(k);
					if (idnow == myproduct){
						user.count ++;
						continue;
					}
					
				}	
				
			}
			
		}
		
		for(int i=0;i<users.size()-1;i++){
			for(int j=0;j<users.size()-i-1;j++){
				UserLikes user_1 = users.get(j);
				int count_1 = user_1.count;
				UserLikes user_2 = users.get(j+1);
				int count_2 = user_2.count;
				if (count_1 < count_2){
					UserLikes temp = user_2;
					users.remove(user_2);
					users.add(j, temp);
	
				}
					
			}
			 
		}
		
		for(int i=0;i<5;i++){
			user = users.get(i);
		  for(int j=0;j<user.productIds.size();j++){
			  int prodt = user.productIds.get(j);
			  
			  boolean isExist = false;
			  for(int k=0;k<mylikes.size();k++){
					mylike = mylikes.get(k);
					myproduct = mylike.getProductId();
					if (prodt == myproduct){            //if the product is not current user like
						isExist=true;
						break;
					}
						
			 }	
			  
			  if(isExist == false){
				  boolean flag = false;
				  for(int l=0; l<recommends.size();l++){        //traverse the current result
					  if(recommends.get(l).productId == prodt){    //if the product already exsits in current result
							recommends.get(l).count ++;               //just increase its count with 1
							flag = true;              //set a flag indecating the operation is finished
							break;
					  }
				  }
				  if(flag == false){        //if no product is found in the result after the loop
						Recommend newrecom = new Recommend(); //create a new one
						newrecom.productId = prodt;     //put the productid and count (1) into it to initialize
						newrecom.count = 1;
						recommends.add(newrecom);    //insert it into the result list
				  }
			  }
			  
			  
			  
		  }
		}
		
		
		for(int i=0;i<recommends.size()-1;i++){
			for(int j=0;j<recommends.size()-i-1;j++){
				int count_1 = recommends.get(j).count;
				int count_2 = recommends.get(j+1).count;
				if (count_1 < count_2){
					Recommend temp = recommends.get(j);
					recommends.remove(temp);
					recommends.add(j, temp);
				}
			}
		}
		int recomm_prodt[] = new int[5];
		for(int i=0;i<recommends.size();i++){
			recomm_prodt[i]= recommends.get(i).productId;
			if(i == 4)
				break;
			
		}
		
		
		return recomm_prodt;
	}
	
	private class UserLikes{
		public int userId;
		public List<Integer> productIds;
		public int count = 0;
		
		public UserLikes(){
			productIds = new ArrayList<Integer>();
		}
	}
	
	private class Recommend{
		public int productId;
		public int count;
	}
	
	
	
	/*****                                                                         ***/
	/*************************  end of the algorithm  ********************************/
	
	

	public SimpleAdapter productAdapter(){                      //load all the product
		ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
		
		for(int i=0;i<products.size();i++){
			ProductBasicInf product = products.get(i);
			
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("name", product.getProductName());
			
			Double p = product.getPrice();
			String price;
			if(p == -1)
				price = "Not Available";
			else
				price = "$" + Double.toString(p);
			
			map.put("price", price);
			String seller = "@" + product.getSeller();
			map.put("seller", seller);
			
			if(product.getBitmap()!=null)
				map.put("image", product.getBitmap());
			else
				map.put("image", R.drawable.no_image);
			
			int score = product.getScore();
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
			
			map.put("id", Integer.toString(product.getProductId()));
			
			if(product.getSour() >= 70)
				map.put("sour", "Sour");
			if(product.getSweet() >= 70)
				map.put("sweet", "Sweet");
			if(product.getBitter() >= 70)
				map.put("bitter", "Bitter");
			if(product.getSpicy() >= 70)
				map.put("spicy", "Spicy");
			if(product.getSalty() >= 70)
				map.put("salty", "Salty");
			
			listItem.add(map);
		}
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(getApplicationContext(), listItem,
				R.layout.listcomp_productlist, new String[]{"name","price","seller","image","score","sour","sweet","bitter","spicy","salty"},
				new int[] {R.id.listproduct_txt_name,R.id.listproduct_txt_price,R.id.listproduct_txt_loc,R.id.listproduct_img_product,R.id.listproduct_txt_rating,
			R.id.listproduct_txt_tagsour,R.id.listproduct_txt_tagsweet,R.id.listproduct_txt_tagbitter,R.id.listproduct_txt_tagspicy,R.id.listproduct_txt_tagsalty});
		
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
	
	public void setListViewHeightBasedOnChildren(ListView listView) {           //extend the view with list
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
	    	  return;
	      }
	      int totalHeight = 0;
	      for (int i = 0; i < listAdapter.getCount(); i++) {
	    	  View listItem = listAdapter.getView(i, null, listView);		    
		      listItem.measure(0, 0);		      
		      totalHeight += listItem.getMeasuredHeight();
	      }
	      ViewGroup.LayoutParams params = listView.getLayoutParams();
	      params.height = totalHeight
	    		  + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

	      listView.setLayoutParams(params);
	}
	
	public void findClick(View v){          //when start button is clicked
		if(userId==-1){
			Toast.makeText(getApplicationContext(), "Please Log in first", Toast.LENGTH_SHORT).show();
			return;
		}
		txt_start.setText("Getting Information...");
		
		GetThread thread = new GetThread(MSG_GET_LIKES);
		thread.start();
	}
	
	public void backClick(View v){
		finish();
	}
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what == MSG_RECV_LIKES){
				txt_start.setText("Calculating...");
				
				//TODO: calculate
				
				
				GetThread thread = new GetThread(MSG_GET_PRODUCT, recommentAlgorithm());
				thread.start();
			}
			else if(msg.what == MSG_RECV_PRODUCT){
				list_product.setAdapter(productAdapter());
				setListViewHeightBasedOnChildren(list_product);
				txt_start.setVisibility(View.INVISIBLE);
				view_result.setVisibility(View.VISIBLE);
			}
		}
	};
	

	public class GetThread extends Thread{
		private int action;
		private int[] productid;
		
		public GetThread(int action){
			this.action = action;
		}
		
		public GetThread(int action, int[] id){
			this.action = action;
			this.productid = id;
		}
		
		@Override
		public void run(){
			if(action == MSG_GET_LIKES){
				RestClientv2 rc = new RestClientv2();
				likes = rc.getAllLike();
				mylikes = rc.getLikeByUser(userId);
				
				Message msg = handler.obtainMessage();
				msg.what = MSG_RECV_LIKES;
				handler.sendMessage(msg);		
			}
			else if(action == MSG_GET_PRODUCT){
				RestClientv2 rc = new RestClientv2();
				products = new ArrayList<ProductBasicInf>();
				for(int i=0; i<productid.length; i++){
					products.addAll(rc.getProductById(productid[i]));
				}
				rc.fillBitmapProducts(products);
				
				Message msg = handler.obtainMessage();
				msg.what = MSG_RECV_PRODUCT;
				handler.sendMessage(msg);
				
			}
		}
	}
}
