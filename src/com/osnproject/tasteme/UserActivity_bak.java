package com.osnproject.tasteme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.osnproject.tasteme.HTTP.RestClientv2;
import com.osnproject.tasteme.Model.ActionLike;
import com.osnproject.tasteme.Model.Comment;
import com.osnproject.tasteme.Model.Group;
import com.osnproject.tasteme.Model.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;

public class UserActivity_bak extends Activity{
	public static final int RESULT_USERLOGIN = 1;
	public static final int RESULT_GROUPCREATED = 2;
	
	public static final int MSG_GET_USER = 11;
	public static final int MSG_GET_GROUPS = 12;
	public static final int MSG_GET_LIKEBYGROUP = 15;
	public static final int MSG_PUT_JOINGROUP = 13;
	public static final int MSG_GET_GROUP_NEWSBYID = 14;
	public static final int MSG_RECV_USER = 1;
	public static final int MSG_RECV_GROUPS = 2;
	public static final int MSG_RECV_FAILED = 0;
	public static final int MSG_RECV_JOINSUCCESS = 3;
	public static final int MSG_RECV_MYGROUP = 4;
	
	User userMe;
	Group groupMy;
	List<Group> groups;
	List<Comment> comments;
	List<GroupLike> grouplikes;
	List<ActionLike> mylikes;
	
	TextView txt_user_Name;
	TextView txt_group_GroupName;
	TextView txt_group_Groupdescrib;
	TextView txt_user_Description;
	TextView txt_loadingtext;
	Button btn_group_Join;
	Button btn_group_Create;
	Button btn_back;
	Button btn_group_enter;
	Button btn_said;
	Button btn_liked;
	Button btn_me;
	EditText edt_group_search;
	ListView list_groupjoin_recomm;
	ListView list_groupjoin_result;
	ImageView img_user_photo;
	ImageView img_user_groupphoto;
	View view_group_body_nogroup;
	View view_group_body_groupmain;
	View view_group_body_join;
	View view_user_main;
	View view_group_join;
	View cur_view;
	View view_user_loading;
	
	int userId = -1;
	public int groupClicked = -1;
	public boolean isLoading = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_main);

		txt_user_Name = (TextView)findViewById(R.id.user_txt_name);
		txt_group_GroupName = (TextView)findViewById(R.id.txt_group_groupname);
		txt_user_Description = (TextView)findViewById(R.id.user_txt_describ);
		txt_group_Groupdescrib = (TextView)findViewById(R.id.txt_group_describ);
		txt_loadingtext = (TextView)findViewById(R.id.txt_user_loadingcontext);
		btn_group_Join = (Button)findViewById(R.id.btn_group_join);
		btn_group_Create = (Button)findViewById(R.id.btn_group_create);
		btn_back = (Button)findViewById(R.id.btn_user_navigate_back);
		edt_group_search = (EditText)findViewById(R.id.edt_groupjoin_search);
		view_group_body_nogroup = findViewById(R.id.view_group_nogroup);
		view_group_body_groupmain = findViewById(R.id.view_group_main);
		view_user_main = findViewById(R.id.view_user_main);
		view_group_join = findViewById(R.id.view_group_join);
		list_groupjoin_recomm = (ListView)findViewById(R.id.list_groupjoin_recomm);
		list_groupjoin_result = (ListView)findViewById(R.id.list_groupjoin_result);
		img_user_photo = (ImageView)findViewById(R.id.img_user_photo);
		img_user_groupphoto = (ImageView)findViewById(R.id.img_group_groupicon);
		view_user_loading = findViewById(R.id.view_user_loading);
		btn_said = (Button)findViewById(R.id.user_btn_said);
		btn_liked = (Button)findViewById(R.id.user_btn_like);
		btn_me = (Button)findViewById(R.id.user_btn_me);
		
		cur_view = view_user_main;
		view_group_join.setVisibility(View.INVISIBLE);
		
		grouplikes = new ArrayList<GroupLike>();
		
		Bundle mybundle = this.getIntent().getExtras();
		
		if(mybundle != null)
			userId = mybundle.getInt("UserId");
		
		if(userId == -1){         //no user
			Intent intent = new Intent(getApplicationContext(), UserLoginActivity.class);
			startActivityForResult(intent, RESULT_USERLOGIN);
		}else{
			showLoading(true, "User Information");
			
			GetUserThread thread = new GetUserThread(userId, MSG_GET_USER);
			thread.start();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount()==0){
			goBack();
			return false;
		}
		return false;
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if(intent == null){
			showLoading(false, null);
			txt_user_Name.setText("Not Logged In");
			txt_user_Description.setText("Please Log in first");
			
			txt_group_GroupName.setText("Join a Group!");
			txt_group_Groupdescrib.setText("You haven't joined any group.\r\nLog in to see details.");
			view_group_body_groupmain.setVisibility(View.INVISIBLE);
			view_group_body_nogroup.setVisibility(View.INVISIBLE);
			
			return;
		}
		
		Bundle bundle = intent.getExtras();
		switch(requestCode){
		case RESULT_USERLOGIN:
			userId = bundle.getInt("UserId");
			
			showLoading(true, "User Information");
			
			GetUserThread thread = new GetUserThread(userId, MSG_GET_USER);
			thread.start();
			break;
		case RESULT_GROUPCREATED:
			showLoading(true, "User Information");
			
			GetUserThread thread2 = new GetUserThread(userMe.getUserId(), MSG_GET_USER);
			thread2.start();
			break;
		default:
			finish();
		}
		
	}
	
	public void saveBundle(int userId, String userName){              //save the userId to bundle to pass to lower activity
		Bundle bundle = new Bundle();
		bundle.putInt("UserId", userId);
		bundle.putString("Username", userName);
		Intent intent = new Intent();
		intent.putExtras(bundle);
		setResult(Activity.RESULT_OK, intent);
	}
	
	public void userClick(View v){                 //said, liked, me button
		if(userMe == null){
			Toast.makeText(getApplicationContext(), "Please Log in", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(v == btn_said){
			Bundle bundle = new Bundle();
			bundle.putInt("userid", userMe.getUserId());
			bundle.putInt("priority", userMe.getPriority());
			bundle.putInt("groupid", userMe.getGroupId());
			bundle.putString("username", userMe.getUserName());
			bundle.putString("description", userMe.getDescription());
			Intent intent = new Intent(getApplicationContext(), MyCommentsActivity.class);
			intent.putExtras(bundle);
			if(userMe.getUserImage()!=null)
				intent.putExtra("image", scaleDownBitmap(userMe.getUserImage(), 100, getApplicationContext()));
			startActivity(intent);
		}
		
		else if(v == btn_liked){
			Bundle bundle = new Bundle();
			bundle.putInt("userid", userMe.getUserId());
			bundle.putInt("priority", userMe.getPriority());
			bundle.putInt("groupid", userMe.getGroupId());
			bundle.putString("username", userMe.getUserName());
			bundle.putString("description", userMe.getDescription());
			Intent intent = new Intent(getApplicationContext(), MyLikedActivity.class);
			intent.putExtras(bundle);
			if(userMe.getUserImage()!=null)
				intent.putExtra("image", scaleDownBitmap(userMe.getUserImage(), 100, getApplicationContext()));
			startActivity(intent);
		}
		
		else if(v == btn_me){
			Bundle bundle = new Bundle();
			bundle.putInt("userid", userMe.getUserId());
			Intent intent = new Intent(getApplicationContext(), UserChangeActivity.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, RESULT_USERLOGIN);
		}
	}

	
	 public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

		 final float densityMultiplier = context.getResources().getDisplayMetrics().density;        

		 int h= (int) (newHeight*densityMultiplier);
		 int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

		 photo=Bitmap.createScaledBitmap(photo, w, h, true);

		 return photo;
	 }
	
	/**************  Menus and Views  ********************/
	public void showLoading(boolean isShow, String text){
		if(isShow == true && isLoading == false){
			Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha_show);
			view_user_loading.startAnimation(animation);
			view_user_loading.setVisibility(View.VISIBLE);
			view_user_main.setVisibility(View.INVISIBLE);
			isLoading = true;
		}
		else if(isShow == false && isLoading == true){
			view_user_main.setVisibility(View.VISIBLE);
			Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha_hide);
			view_user_loading.startAnimation(animation);
			view_user_loading.setVisibility(View.INVISIBLE);
			isLoading = false;
		}
		
		txt_loadingtext.setText(text);
	}
	
	public void backClick(View v){         //on back menu clicked
		goBack();
	}
	
	public void goBack(){
		if(cur_view == view_group_join){
			changeView(view_group_join, view_user_main, false);
			btn_back.setText("Close");
		}
		else if(cur_view == view_user_main){
			finish();
		}
	}
	
	public void groupClick(View v){        //join, create, enter groups
		if(v == btn_group_Join){
			showLoading(true, "Group List");
			changeView(view_user_main, view_group_join, true);
			btn_back.setText("Back");
			initJoinView();
		}
		else if(v == btn_group_Create){
			Bundle bundle = new Bundle();
			bundle.putInt("userid", userMe.getUserId());
			Intent intent = new Intent(getApplicationContext(), CreateGroupActivity.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, RESULT_GROUPCREATED);
		}
		else if(v == img_user_groupphoto){
			Bundle bundle = new Bundle();
			bundle.putInt("groupId", userMe.getGroupId());
			bundle.putInt("userId", userMe.getUserId());
			Intent intent = new Intent(getApplicationContext(), GroupActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}
	
	public void changeView(View pre, View post, boolean isForward){               //animation of changing to other menu
		cur_view = post;
		if(isForward==true){
			Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_group_tolefthide);
			pre.startAnimation(animation);
			pre.setVisibility(View.INVISIBLE);
			
			animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_group_fromrightshow);
			post.startAnimation(animation);
			post.setVisibility(View.VISIBLE);
		}
		else{
			Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_group_torighthide);
			pre.startAnimation(animation);
			pre.setVisibility(View.INVISIBLE);
			
			animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_group_fromleftshow);
			post.startAnimation(animation);
			post.setVisibility(View.VISIBLE);
		}
	}
	
	public void initView(){                   //initial the UI
		txt_user_Name.setText(userMe.getUserName());
		
		String desc = userMe.getDescription();
		if(desc.equals("null"))
			txt_user_Description.setText("I'm too lazy to write a description");
		else
			txt_user_Description.setText(desc);
		
		Bitmap icon = userMe.getUserImage();
		if(icon != null)
			img_user_photo.setImageBitmap(icon);
		
		
		// About group
		int groupId = userMe.getGroupId();
		if(groupId == -1){
			txt_group_GroupName.setText("Join a Group!");
			txt_group_Groupdescrib.setText("You haven't joined any group.\r\nWhy not have a try?");
			view_group_body_groupmain.setVisibility(View.INVISIBLE);
			view_group_body_nogroup.setVisibility(View.VISIBLE);
			
			showLoading(false, null);
		}
		else{
			
			view_group_body_nogroup.setVisibility(View.INVISIBLE);
			txt_group_GroupName.setText(userMe.getGroupName());
			txt_group_Groupdescrib.setText(userMe.getGroupDescrib());
			
			showLoading(true, "Group Information");
			GetUserThread thread = new GetUserThread(groupId, MSG_GET_GROUP_NEWSBYID);
			thread.start();
		}
		
		
			
	}
	
	
	
	
	/**************  groups  **********************/
	public void initJoinView(){
		GetUserThread thread = new GetUserThread(MSG_GET_GROUPS);
		thread.start();
	}
	
	public void fillLists(){                              //fill the group list
		SimpleAdapter myAdapter = groupResultAdapter(null);
		List<Group> groupshow = new ArrayList<Group>();
		groupshow.add(getRecommend(groups));
		SimpleAdapter recommAdapter = groupResultAdapter(groupshow);
		list_groupjoin_recomm.setAdapter(recommAdapter);
		list_groupjoin_result.setAdapter(myAdapter);
		list_groupjoin_recomm.setOnItemClickListener(groupClicker);
		list_groupjoin_result.setOnItemClickListener(groupClicker);
		showLoading(false, null);
	}
	
	public SimpleAdapter groupResultAdapter(List<Group> groups){                      //load all the groups
		if(groups == null)
			groups = this.groups;
		
		ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
		
		for(int i=0;i<groups.size();i++){
			Group group = groups.get(i);
			
			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("name", group.getGroupName());
			map.put("describ", group.getDescription());
			
			String member = "Total Members: "+ Integer.toString(group.getMember());
			map.put("member", member);
			
			if(group.getImage() != null)
				map.put("image", group.getImage());
			else
				map.put("image", R.drawable.groupicon);
			
			map.put("groupid", group.getGroupId());
			listItem.add(map);
		}
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(getApplicationContext(), listItem,
				R.layout.listcomp_group, new String[]{"name","describ","member","image"},
				new int[] {R.id.txt_listgroup_name,R.id.txt_listgroup_content,R.id.txt_listgroup_member,R.id.img_listgroup_icon});
		
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
	
	public Group getRecommend(List<Group> groups){           //TODO: the algorithm to get the recommended group
		return groups.get(0);
	}
	
	private class GroupLike{
		public int groupId;
		public List<Integer> productId;
		private List<Integer> userNum;
		
		public GroupLike(){
			productId = new ArrayList<Integer>();
			userNum = new ArrayList<Integer>();
		}
	}
	
	
	
	public Group findGroupById(int id){
		for(int i=0; i<groups.size(); i++){
			if(groups.get(i).getGroupId() == id)
				return groups.get(i);
		}
		return null;
			
	}
	
	public AdapterView.OnItemClickListener groupClicker = new AdapterView.OnItemClickListener() {            //a group is selected

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
			HashMap<String, Object> item = (HashMap<String, Object>)arg0.getItemAtPosition(arg2); 
			int groupId = (Integer)item.get("groupid");
			groupClicked = groupId;
			String groupName = (String)item.get("name");
			
			Group group = findGroupById(groupId);
			
			LayoutInflater factory=LayoutInflater.from(getApplicationContext());         //inflate the menu view
			final View dialogview = factory.inflate(R.layout.dialog_groupinfo, null);
			
			TextView txt_Member = (TextView)dialogview.findViewById(R.id.txt_dlg_group_member);
			TextView txt_Creator = (TextView)dialogview.findViewById(R.id.txt_dlg_group_creator);
			TextView txt_Prefer = (TextView)dialogview.findViewById(R.id.txt_dlg_group_prefer);
			TextView txt_Describ = (TextView)dialogview.findViewById(R.id.txt_dlg_group_describ);
			ImageView img_Icon = (ImageView)dialogview.findViewById(R.id.img_dlg_groupicon);
			
			txt_Member.setText(Integer.toString(group.getMember()));
			txt_Creator.setText(group.getCreatorName());
			
			String maxPref = "Sour food";
			int maxValue = group.getSour();
			if(group.getSweet() > maxValue){
				maxPref = "Sweet food";
				maxValue = group.getSweet();
			}
			if(group.getBitter() > maxValue){
				maxPref = "Bitter food";
				maxValue = group.getBitter();
			}
			if(group.getSpicy() > maxValue){
				maxPref = "Spicy food";
				maxValue = group.getSpicy();
			}
			if(group.getSalty() > maxValue){
				maxPref = "Salty food";
				maxValue = group.getSalty();
			}
			txt_Prefer.setText(maxPref);
			
			txt_Describ.setText(group.getDescription());
			
			img_Icon.setImageBitmap(group.getImage());
			
			AlertDialog.Builder createDialog=new AlertDialog.Builder(UserActivity_bak.this);  //create the dialog
			createDialog.setTitle(group.getGroupName());
			createDialog.setView(dialogview);
			createDialog.setPositiveButton("Join", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					joinGroup();
				}
			});
			createDialog.setNegativeButton("Cancel", null);
			createDialog.show();
		
		}
	};
	
	public void joinGroup(){           //to post Join group msg
		GetUserThread thread = new GetUserThread(MSG_PUT_JOINGROUP);
		thread.start();
	}
	
	public void initGroups(){                     //initial when user is already in a group
		if(groupMy.getImage() != null)
			img_user_groupphoto.setImageBitmap(groupMy.getImage());
		
		ListView list_group_newsfeed = (ListView)findViewById(R.id.list_group_newsfeed);
		list_group_newsfeed.setAdapter(newsFeedAdapter());
		view_group_body_groupmain.setVisibility(View.VISIBLE);
		setListViewHeightBasedOnChildren(list_group_newsfeed);
		ScrollView scrollview = (ScrollView)findViewById(R.id.view_usermain_scroll);
		if(scrollview != null) 
			scrollview.scrollTo(10, 10);
		showLoading(false, null);
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
	
	
	
	
	/**************  Threads and Connections  ****************/
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_RECV_USER:
				saveBundle(userMe.getUserId(), userMe.getUserName());
				initView();
				break;
			case MSG_RECV_GROUPS:
				fillLists();
				break;
			case MSG_RECV_JOINSUCCESS:
				GetUserThread thread = new GetUserThread(userId, MSG_GET_USER);
				thread.start();
				goBack();
				break;
			case MSG_RECV_MYGROUP:
				initGroups();
				break;
			}
		}
	};
	
	public class GetUserThread extends Thread{
		int id;
		int action;
		
		public GetUserThread(int userId, int action){
			this.id = userId;
			this.action = action;
		}
		
		public GetUserThread(int action){
			this.action = action;
		}
		
		@Override
		public void run(){
			RestClientv2 rc = new RestClientv2();
			
			switch(action){
			case MSG_GET_USER:
				List<User> users = rc.getUserById(id);
				rc.fillBitmapUsers(users);
				
				if(users != null && !users.isEmpty()){
					userMe = users.get(0);
					
					Message msg = handler.obtainMessage();
					msg.what = MSG_RECV_USER;
					handler.sendMessage(msg);
				}
				break;
				
			case MSG_GET_GROUPS:
				groups = rc.getAllGroup();
				rc.fillBitmapGroups(groups);
				
				Message msg = handler.obtainMessage();
				if(groups != null)
					msg.what = MSG_RECV_GROUPS;
				else
					msg.what = MSG_RECV_FAILED;
				
				handler.sendMessage(msg);	
				
				for(int i=0; i<groups.size(); i++){
					int groupid = groups.get(i).getGroupId();
					List<ActionLike> likes = rc.getLikeByGroup(groupid);
					mylikes = rc.getLikeByUser(userId);
					Message msg4 = handler.obtainMessage();
					
					if(likes == null){
						msg4.what = MSG_RECV_FAILED;
						handler.sendMessage(msg4);
						return;
					}
					
					GroupLike grouplike = new GroupLike();
					grouplike.groupId = id;
					
					for(int j=0;j<likes.size();j++){
						ActionLike like = likes.get(j);
						if(grouplike.productId.isEmpty()){
							int productid = like.getProductId();
							int count = 1;
							grouplike.productId.add(productid);
							grouplike.userNum.add(count);
							continue;
						}
						
						if(like.getProductId() < grouplike.productId.get(grouplike.productId.size()-1))
							continue;
						else if(like.getProductId() == grouplike.productId.get(grouplike.productId.size()-1)){
							Integer count = grouplike.userNum.get(grouplike.productId.size()-1);
							count ++;
							continue;
						}else
							break;
					}
					
					grouplikes.add(grouplike);
				}
				
				
				break;
				
			case MSG_PUT_JOINGROUP:
				if(userMe == null)
					return;
				
				if(groupClicked == -1)
					return;
				
				userMe.setGroupId(groupClicked);
				rc.updateUser(userMe);
				
				Message msg2 = handler.obtainMessage();
				msg2.what = MSG_RECV_JOINSUCCESS;
				handler.sendMessage(msg2);
				break;
				
			case MSG_GET_GROUP_NEWSBYID:
				List<Group> groupsGot = rc.getGroupById(id);
				
				Message msg3 = handler.obtainMessage();
				
				if(groupsGot == null || groupsGot.isEmpty())
					msg3.what = MSG_RECV_FAILED;
				else{
					rc.fillBitmapGroups(groupsGot);
					groupMy = groupsGot.get(0);
					
					int groupid = groupMy.getGroupId();               //get comments feed
					comments = rc.getCommentByGroup(groupid, 3);
					if(comments == null)
						msg3.what = MSG_RECV_FAILED;
					else{
						rc.fillBitmapComments(comments, Comment.IMAGEFILL_USER);
						msg3.what = MSG_RECV_MYGROUP;
					}
				}
				
				handler.sendMessage(msg3);
				break;
				
			case MSG_GET_LIKEBYGROUP:
				
			}
		}
	}
}
