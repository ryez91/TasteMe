package com.osnproject.tasteme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.osnproject.tasteme.HTTP.RestClientv2;
import com.osnproject.tasteme.Model.ActionLike;
import com.osnproject.tasteme.Model.Comment;
import com.osnproject.tasteme.Model.ProductBasicInf;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class GroupActivity extends Activity{
	public static final int MSG_RECV_TOPPRODUCT = 11;
	public static final int MSG_RECV_FAILED = 10;
	public static final int MSG_RECV_NOTENOUGHLIKE = 12;
	public static final int MSG_RECV_COMMENTS = 13;
	
	public static final int MSG_GET_TOPPRODUCT = 1;
	public static final int MSG_GET_GROUPNEWS = 2;
	
	List<ProductBasicInf> topProducts;
	List<ActionLike> likes;
	List<Comment> comments;
	
	ImageView img_top_1;
	ImageView img_top_2;
	ImageView img_top_3;
	ListView list_news;
	View view_loading;
	TextView txt_loading;
	
	public int groupId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_main);
		
		Bundle bundle = getIntent().getExtras();
		groupId = bundle.getInt("groupId");
		
		img_top_1 = (ImageView)findViewById(R.id.img_latest_product1);
		img_top_2 = (ImageView)findViewById(R.id.img_latest_product2);
		img_top_3 = (ImageView)findViewById(R.id.img_latest_product3);
		list_news = (ListView)findViewById(R.id.listview_new_comments);
		view_loading = findViewById(R.id.view_group_loading);
		txt_loading = (TextView)findViewById(R.id.txt_user_loadingcontext);
		
		topProducts = new ArrayList<ProductBasicInf>();
		
		txt_loading.setText("Loading Top Products");
		
		GroupThread thread = new GroupThread(MSG_GET_TOPPRODUCT);
		thread.start();
		GroupThread thread2 = new GroupThread(MSG_GET_GROUPNEWS);
		thread2.start();
	}
	
	public SimpleAdapter newsFeedAdapter(){                      //load all the news feed
		if(comments == null)
			return null;
		
		ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
		
		for(int i=0;i<comments.size();i++){
			Comment comment = comments.get(i);
			
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			String username = comment.getUserName();
			if(username.length()>10)
				username = username.substring(0, 9) + "..";
			map.put("username", username);
			
			String productname = comment.getProductName();
			if(productname.length()>25)
				productname = productname.substring(0,25) + "...";
			map.put("productname", productname);
			map.put("text", comment.getText());
			
			map.put("date", comment.getDate().substring(0, 10));
			
			if(comment.getBitmap() != null)
				map.put("image", comment.getBitmap());
			else
				map.put("image", R.drawable.add_user);
			
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
				R.layout.listcomp_group_comments, new String[]{"username","productname","text","date","image","score"},
				new int[] {R.id.txt_groupcmt_username,R.id.txt_groupcmt_prodName,R.id.txt_groupcmt_text,R.id.txt_groupcmt_date,R.id.img_groupcmt_user,R.id.img_groupcmt_rate});
		
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
	
	private class ProductCount{                     //to count the likes of products
		public int productId;
		public int count;
		
		public ProductCount(int productId, int count){
			this.productId = productId;
			this.count = count;
		}
	}
	
	public void backClick(View v){
		finish();
	}
	
	public void imgClick(View v)
	{
		
	}
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what == MSG_RECV_TOPPRODUCT){
				img_top_1.setImageBitmap(topProducts.get(0).getBitmap());
				img_top_2.setImageBitmap(topProducts.get(1).getBitmap());
				img_top_3.setImageBitmap(topProducts.get(2).getBitmap());
				view_loading.setVisibility(View.INVISIBLE);
			}
			else if(msg.what == MSG_RECV_NOTENOUGHLIKE){
				view_loading.setVisibility(View.INVISIBLE);
				if(topProducts.get(0)==null){
					img_top_1.setVisibility(View.INVISIBLE);
					img_top_2.setVisibility(View.INVISIBLE);
					img_top_3.setVisibility(View.INVISIBLE);
					return;
				}
				img_top_1.setImageBitmap(topProducts.get(0).getBitmap());
				
				if(topProducts.get(1)==null){
					img_top_2.setVisibility(View.INVISIBLE);
					img_top_3.setVisibility(View.INVISIBLE);
					return;
				}
				img_top_2.setImageBitmap(topProducts.get(0).getBitmap());
				
				if(topProducts.get(2)==null){
					img_top_3.setVisibility(View.INVISIBLE);
					return;
				}
				img_top_3.setImageBitmap(topProducts.get(0).getBitmap());
			}
			else if(msg.what == MSG_RECV_COMMENTS){
				list_news.setAdapter(newsFeedAdapter());
			}
		}
	};
	
	public class GroupThread extends Thread{
		int action;
		
		public GroupThread(int action){
			this.action = action;
		}	
		
		public List<ProductCount> sortProduct(){
			List<ProductCount> result = new ArrayList<ProductCount>();
			
			int max = 1;
			for(int i=0;i<likes.size();i++){
				ActionLike like = likes.get(i);
				int productid = like.getProductId();
				boolean flag = false;
				
				for(int j=0;j<result.size();j++){
					if(result.get(j).productId == productid){
						ProductCount cur_product = new ProductCount(productid,  result.get(j).count+1);
						if(cur_product.count >= max){
							result.add(0, cur_product);
							result.remove(j+1);
							flag = true;
						}
						else{
							result.add(cur_product);
							flag = true;
						}
					}
				}
				
				if(flag == false){
					ProductCount newProduct = new ProductCount(productid, 1);
					result.add(newProduct);
				}
			}
			
			return result;
		}
		
		@Override
		public void run(){
			if(action == MSG_GET_TOPPRODUCT){
				RestClientv2 rc = new RestClientv2();
				likes = rc.getLikeByGroup(groupId);
				
				Message msg = handler.obtainMessage();
				if(likes == null || likes.isEmpty()){
					msg.what = MSG_RECV_FAILED;
					handler.sendMessage(msg);
					return;
				}
				else if(likes.size()<3){
					msg.what = MSG_RECV_NOTENOUGHLIKE;
					for(int i=0;i<likes.size();i++){
						ProductBasicInf product = rc.getProductById(likes.get(i).getProductId()).get(0);
						topProducts.add(product);
					}

					rc.fillBitmapProducts(topProducts);
					handler.sendMessage(msg);
					return;
				}
				
				List<ProductCount> productCount = sortProduct();
				
				for(int i=0;i<3;i++){
					ProductBasicInf product = rc.getProductById(productCount.get(i).productId).get(0);
					topProducts.add(product);
				}
				
				rc.fillBitmapProducts(topProducts);

				msg.what = MSG_RECV_TOPPRODUCT;
				handler.sendMessage(msg);
			}
			else if(action == MSG_GET_GROUPNEWS){
				RestClientv2 rc = new RestClientv2();
				comments = rc.getCommentByGroup(groupId, 10);
				
				Message msg = handler.obtainMessage(); 
				if(comments == null)
					msg.what = MSG_RECV_FAILED;
				else{
					msg.what = MSG_RECV_COMMENTS;
					rc.fillBitmapComments(comments, Comment.IMAGEFILL_USER);
				}
					
				handler.sendMessage(msg);
			}
		}
	}
}
