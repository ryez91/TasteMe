package com.osnproject.tasteme;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.osnproject.tasteme.HTTP.RestClientv2;
import com.osnproject.tasteme.Model.ActionLike;
import com.osnproject.tasteme.Model.Comment;
import com.osnproject.tasteme.Model.ProductBasicInf;
import com.osnproject.tasteme.Model.User;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;

public class ProductDetailActivity extends Activity{
	public final static int MSG_GET_INITIALIZE = 11;
	public final static int MSG_GET_COMMENTS = 13;
	public final static int MSG_POST_COMMENT = 12;
	public final static int MSG_POST_PRODUCT = 14;
	public final static int MSG_POST_LIKE = 15;
	public final static int MSG_RECV_PRODUCT = 1;
	public final static int MSG_RECV_COMMENTS = 2;
	public final static int MSG_RECV_FAILED = 0;
	public final static int MSG_RECV_COMMENTCREATED = 3;
	public final static int MSG_RECV_LIKE_YES = 4;
	public final static int MSG_RECV_LIKE_NO = 5;
	
	public final static int RESULT_COMMENT = 1;
	public final static int RESULT_USERLOGIN = 2;
	public final static int RESULT_MANAGE = 3;
	
	TextView t_Name;
	TextView t_Price;
	TextView t_Seller;
	TextView t_Description;
	TextView t_Details;
	TextView t_Product_Sour;
	TextView t_Product_Sweet;
	TextView t_Product_Bitter;
	TextView t_Product_Spicy;
	TextView t_Product_Salty;
	TextView t_CommentsLoading;
	ListView list_comments;
	ImageView img_Product;
	Button btn_like;
	Button btn_comment;
	Button btn_manage;
	
	List<ProductBasicInf> products;
	List<Comment> comments;
	public int userId = -1;
	public int groupId = -1;
	public int productId = -1;
	public int priority = 0;
	public int likeCount = 0;
	public boolean isLiked = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		
		t_Name = (TextView)findViewById(R.id.product_txt_name);
		t_Price = (TextView)findViewById(R.id.product_txt_price);
		t_Seller = (TextView)findViewById(R.id.product_txt_where);
		t_Description = (TextView)findViewById(R.id.product_txt_descrip);
		t_Details = (TextView)findViewById(R.id.product_txt_productdetails);
		t_Product_Sour = (TextView)findViewById(R.id.product_txt_sour_prog);
		t_Product_Sweet = (TextView)findViewById(R.id.product_txt_sweet_prog);
		t_Product_Bitter = (TextView)findViewById(R.id.product_txt_bitter_prog);
		t_Product_Spicy = (TextView)findViewById(R.id.product_txt_spicy_prog);
		t_Product_Salty = (TextView)findViewById(R.id.product_txt_salty_prog);
		t_CommentsLoading = (TextView)findViewById(R.id.txt_productdetail_cmtloading);
		list_comments = (ListView)findViewById(R.id.list_productdetail_comment);
		img_Product = (ImageView)findViewById(R.id.product_img_product);
		btn_like = (Button)findViewById(R.id.btn_productdetail_like);
		btn_comment = (Button)findViewById(R.id.btn_productdetail_comment);
		btn_manage = (Button)findViewById(R.id.btn_product_navigate_manage);
		
		btn_manage.setVisibility(View.INVISIBLE);
		Bundle bundle = this.getIntent().getExtras();
		productId = bundle.getInt("productId");
		userId = bundle.getInt("userId");
		groupId = bundle.getInt("groupId");
		if(userId != -1)
			priority = bundle.getInt("priority");
		
		if(priority == User.PRI_ADMIN)
			btn_manage.setVisibility(View.VISIBLE);
		
		t_Name.setText("Loading");
		t_Price.setText("Loading");
		t_Seller.setText("Loading");
		t_Description.setText("Loading");
		t_Details.setText("Loading");
		
		list_comments.setVisibility(View.INVISIBLE);
		t_CommentsLoading.setVisibility(View.VISIBLE);
		
		products = new ArrayList<ProductBasicInf>();
		GetDetailThread thread = new GetDetailThread(MSG_GET_INITIALIZE);
		thread.start();
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(intent==null) return;
		
		Bundle bundle = intent.getExtras();
		switch(requestCode){
		case RESULT_COMMENT:                              //after writing comment
			int score = bundle.getInt("score");
			String text = bundle.getString("text");
			
			GetDetailThread thread = new GetDetailThread(MSG_POST_COMMENT, score, text);
			thread.start();
			break;
			
		case RESULT_USERLOGIN:                      //after login
			userId = bundle.getInt("UserId");        //get the variables
			if(userId != -1)
				priority = bundle.getInt("priority");
			groupId = bundle.getInt("GroupId");
			
			if(priority == User.PRI_ADMIN)
				btn_manage.setVisibility(View.VISIBLE);
			
			Intent intent2 = new Intent();       //save the result
			Bundle bundle2 = new Bundle();
			bundle2.putInt("UserId", userId);
			intent2.putExtras(bundle2);
			setResult(Activity.RESULT_OK, intent2);
			
			GetDetailThread thread2 = new GetDetailThread(MSG_GET_INITIALIZE);
			thread2.start();
			
			break;
			
		case RESULT_MANAGE:
			int sour = bundle.getInt("sour");
			int sweet = bundle.getInt("sweet");
			int bitter = bundle.getInt("bitter");
			int spicy = bundle.getInt("spicy");
			int salty = bundle.getInt("salty");
			String barcode = bundle.getString("barcode");
			
			ProductBasicInf product = products.get(0);
			product.setSour(sour);
			product.setSweet(sweet);
			product.setBitter(bitter);
			product.setSpicy(spicy);
			product.setSalty(salty);
			product.setBarcode(barcode);
			GetDetailThread thread3 = new GetDetailThread(MSG_POST_PRODUCT, product);
			thread3.start();
		}
	}
	
	public void fillBasicInf(){                   //fill in the product's information
		ProductBasicInf product = products.get(0);
		t_Name.setText(product.getProductName().replace("\r\n", ""));
		t_Price.setText("Price: $"+Double.toString(product.getPrice()));
		t_Seller.setText("@"+product.getSeller());
		t_Description.setText("Description:\r\n" + product.getDescription().replace("\r\n", ""));
		
		String detail = product.getDetails().replace("\r\n", "").replace("\\n", "\r\n");
		if(detail == null || detail.equals(""))
			t_Details.setText("Details: No details");
		else
			t_Details.setText("Details:\r\n" + detail);
		
		if(product.getBitmap()!=null)
			img_Product.setImageBitmap(product.getBitmap());
		
		int sour = (int)product.getSour();
		int sweet = (int)product.getSweet();
		int bitter = (int)product.getBitter();
		int spicy = (int)product.getSpicy();
		int salty = (int)product.getSalty();
		
		loadProperty(sour, t_Product_Sour);
		loadProperty(sweet, t_Product_Sweet);
		loadProperty(bitter, t_Product_Bitter);
		loadProperty(spicy, t_Product_Spicy);
		loadProperty(salty, t_Product_Salty);
		
		btn_like.setText(btn_like.getText().toString() + " (" + Integer.toString(likeCount) + ")");
		
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
	      
	
	public void fillComments(){                    //fill the comment list
		if(comments.isEmpty()){
			t_CommentsLoading.setText("No comment. You can be the first one!");
			return;
		}
		
		ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
		
		for(int i=0;i<comments.size();i++){
			Comment comment = comments.get(i);
			
			HashMap<String,Object> map = new HashMap<String,Object>();
			
			String username = comment.getUserName();
			if(username.length()>10)
				username = username.substring(0, 9) + "..";
			map.put("username", username);

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
				R.layout.listcomp_product_comments, new String[]{"username","text","date","image","score"},
				new int[] {R.id.txt_productcommentlist_username,R.id.txt_productcommentlist_text,R.id.txt_productcommentlist_date,R.id.img_productcommentlist_user,R.id.img_productcommentlist_rate});
		
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
		setListViewHeightBasedOnChildren(list_comments);
		list_comments.setVisibility(View.VISIBLE);
		t_CommentsLoading.setVisibility(View.INVISIBLE);
	}
	
	public void loadProperty(int value, TextView view){           //load property of the product
		if(value <= 10){
			if(value > 3)
				view.setWidth(value*3);
			else
				view.setWidth(9);
			view.setText("");
		}
		else{
			view.setWidth(value*3);
			view.setText(Integer.toString(value));
		}
	}
	
	public void closeClick(View v){
		finish();
	}
	
	public void actionClick(View v){                    //comment or like button
		if(userId == -1){          //no user
			Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
			startActivityForResult(intent, RESULT_USERLOGIN);
			return;
		}
		
		if(v == btn_comment){
			Intent intent = new Intent(getApplicationContext(), CreateCommentActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("ProductName", products.get(0).getProductName());
			intent.putExtras(bundle);
			startActivityForResult(intent, RESULT_COMMENT);
		}
		else if(v == btn_like){
			if(isLiked == true){
				Toast.makeText(getApplicationContext(), "You have alreay liked me", Toast.LENGTH_SHORT).show();
				return;
			}
			
			GetDetailThread thread = new GetDetailThread(MSG_POST_LIKE);
			thread.start();
			Toast.makeText(getApplicationContext(), "Thank you! ^_^", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void manageClick(View v){         //manage
		Intent intent = new Intent(getApplicationContext(), ProductManageActivity.class);
		Bundle bundle = new Bundle();
		bundle.putDouble("productId", products.get(0).getProductId());
		bundle.putDouble("sour", products.get(0).getSour());
		bundle.putDouble("sweet", products.get(0).getSweet());
		bundle.putDouble("bitter", products.get(0).getBitter());
		bundle.putDouble("spicy", products.get(0).getSpicy());
		bundle.putDouble("salty", products.get(0).getSalty());
		bundle.putString("productName", products.get(0).getProductName());
		intent.putExtra("image", scaleDownBitmap(products.get(0).getBitmap(),150, getApplicationContext()));
		intent.putExtras(bundle);
		startActivityForResult(intent, RESULT_MANAGE);
	}
	
	public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

		 final float densityMultiplier = context.getResources().getDisplayMetrics().density;        

		 int h= (int) (newHeight*densityMultiplier);
		 int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

		 photo=Bitmap.createScaledBitmap(photo, w, h, true);

		 return photo;
	 }
	
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_RECV_PRODUCT:
				fillBasicInf();
				break;
			case MSG_RECV_COMMENTS:
				fillComments();
				break;
			case MSG_RECV_COMMENTCREATED:
				Toast.makeText(getApplicationContext(), "Comment Sent", Toast.LENGTH_SHORT).show();
				GetDetailThread thread = new GetDetailThread(MSG_GET_COMMENTS);
				thread.start();
				break;
			case MSG_RECV_LIKE_YES:
				isLiked = true;
				btn_like.setText("Liked");
				break;
			}
		}
	};
	
	public class GetDetailThread extends Thread{
		private int action;
		private ProductBasicInf product;
		private int score;
		private String text;
		
		public GetDetailThread(int action){
			this.action = action;
		}
		
		public GetDetailThread(int action, int score, String text){
			this.action = action;
			this.score = score;
			this.text = text;
		}
		
		public GetDetailThread(int action, ProductBasicInf product){
			this.product = product;
			this.action = action;
		}

		@Override
		public void run(){
			RestClientv2 rc = new RestClientv2();
			
			switch(action){
			case MSG_GET_INITIALIZE:
				products = rc.getProductById(productId);              //get products and comments
				comments = rc.getCommentByProduct(productId);
				likeCount = rc.getLikeByProduct(productId).size();        //get the total like
				
				if(userId != -1){
					List<ActionLike> like = rc.getLikeByUserProduct(userId, productId);       //get whether user like it or not
					Message msg5 = handler.obtainMessage();
					if(like != null && !like.isEmpty())
						msg5.what = MSG_RECV_LIKE_YES;
					else
						msg5.what = MSG_RECV_LIKE_NO;
					handler.sendMessage(msg5);
				}
				
				Message msg = handler.obtainMessage();
				if(products == null || products.isEmpty()){                    //get the product information
					msg.what = MSG_RECV_FAILED;
					handler.sendMessage(msg);
					return;
				}
				rc.fillBitmapProducts(products);
				msg.what = MSG_RECV_PRODUCT;
				handler.sendMessage(msg);
				
				if(comments == null){
					msg.what = MSG_RECV_FAILED;
					handler.sendMessage(msg);
					return;
				}
				
				rc.fillBitmapComments(comments, Comment.IMAGEFILL_USER);
				msg = handler.obtainMessage();
				msg.what = MSG_RECV_COMMENTS;
				handler.sendMessage(msg);
				
				
				
				break;
				
			case MSG_GET_COMMENTS:
				comments = rc.getCommentByProduct(productId);
				Message msg3 = handler.obtainMessage();
				if(comments == null){
					msg3.what = MSG_RECV_FAILED;
					handler.sendMessage(msg3);
					return;
				}
				
				rc.fillBitmapComments(comments, Comment.IMAGEFILL_USER);
				msg3 = handler.obtainMessage();
				msg3.what = MSG_RECV_COMMENTS;
				handler.sendMessage(msg3);
				break;
				
			case MSG_POST_COMMENT:
				Comment comment = new Comment();
				
				Calendar c = Calendar.getInstance();
				String date = c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.DATE);
				
				comment.packageComment(userId, products.get(0).getProductId(), text, date, score);
				rc.createNewComment(comment);
				
				Message msg2 = handler.obtainMessage();
				msg2.what = MSG_RECV_COMMENTCREATED;
				handler.sendMessage(msg2);
				break;
				
			case MSG_POST_PRODUCT:
				rc.updateProduct(product);
				
				Message msg4 = handler.obtainMessage();
				msg4.what = MSG_RECV_PRODUCT;
				handler.sendMessage(msg4);
				break;
				
			case MSG_POST_LIKE:
				ActionLike like = new ActionLike();
				like.setUserId(userId);
				like.setGroupId(groupId);
				like.setProductId(productId);
				rc.createNewLike(like);
				
				Message msg5 = handler.obtainMessage();
				msg5.what = MSG_RECV_LIKE_YES;
				handler.sendMessage(msg5);
				break;
			}
			
		}
	}
}
