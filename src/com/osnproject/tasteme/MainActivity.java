package com.osnproject.tasteme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.osnproject.tasteme.HTTP.RestClientv2;
import com.osnproject.tasteme.Model.MD5;
import com.osnproject.tasteme.Model.ProductBasicInf;
import com.osnproject.tasteme.Model.User;
import com.osnproject.tasteme.barcode.IntentIntegrator;
import com.osnproject.tasteme.barcode.IntentResult;
import com.osnproject.tasteme.UserActivity.GetUserThread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker.OnScrollListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static final int RESULT_USERLOGIN = 1;
	public static final int RESULT_BARCODE = 0x0000c0de;
	
	public static final int MSG_RECV_PRODUCTALL = 1;
	public static final int MSG_RECV_PRODUCT_BY_CATE = 2;
	public static final int MSG_RECV_PRODUCT_BY_SUBCATE = 3;
	public static final int MSG_RECV_PRODUCT_BY_CATE_MORE = 4;
	public static final int MSG_RECV_PRODUCT_BY_SUBCATE_MORE = 5;
	public static final int MSG_RECV_PRODUCT_NOMORE = 6;
	public static final int MSG_RECV_USER = 7;
	public static final int MSG_RECV_SEARCHPRODUCT = 8;
	public static final int MSG_RECV_SEARCH_NOTFOUND = 9;
	public static final int MSG_RECV_FAILED = 0;
	public static final int MSG_GET_PRODUCTALL = 11;
	public static final int MSG_GET_BYCATEGORY = 12;
	public static final int MSG_GET_BYSUBCATE = 13;
	public static final int MSG_GET_BYCATEGORY_MORE = 14;
	public static final int MSG_GET_BYSUBCATE_MORE = 15;
	public static final int MSG_GET_USER = 16;
	public static final int MSG_GET_SEARCHBYNAME = 17;
	public static final int MSG_GET_SEARCHBYBARCODE = 18;
	
	public int listSizeDefault = 10;
	public boolean isGetting = false;
	
	View view_main_category;
	View view_main_subcategory;
	View view_main_product;
	View view_head;
	View view_navigator;
	ListView list_Subcategory;
	ListView list_subcateProduct;
	ListView list_productmenu;
	ListView list_trymore;
	Button btn_Sweets;
	Button btn_Snacks;
	Button btn_Bakery;
	Button btn_Dairy;
	Button btn_Drinks;
	Button btn_Cooked;
	ImageView img_Subcate_title;
	ImageView img_Loading_Cate;
	ImageView img_Loading_Product;
	ImageView img_Search;
	ImageView img_Barcode;
	TextView txt_ProductMenu_Cate;
	EditText edt_Search;
	Button btn_navigate_back;
	
	List<ProductBasicInf> products;
	User cur_User = null;
	
	int subMenuLevel=0;
	int category_Selected = 0;
	int subcate_Selected = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		view_main_category = findViewById(R.id.view_main_maincategory);
		view_main_subcategory = findViewById(R.id.view_main_subcategory);
		view_main_product = findViewById(R.id.view_main_productlist);
		view_head = findViewById(R.id.view_main_head);
		view_navigator = findViewById(R.id.view_navigator);
		list_Subcategory = (ListView)findViewById(R.id.list_subcategory);
		list_subcateProduct = (ListView)findViewById(R.id.list_subcate_product);
		list_productmenu = (ListView)findViewById(R.id.list_productmenu);
		list_trymore = (ListView)findViewById(R.id.list_trymore);
		btn_Sweets = (Button)findViewById(R.id.btn_Sweets);
		btn_Snacks =(Button)findViewById(R.id.btn_Snacks);
		btn_Bakery=(Button)findViewById(R.id.btn_Bakery);
		btn_Dairy =(Button)findViewById(R.id.btn_Dairy);
		btn_Drinks =(Button)findViewById(R.id.btn_Drinks);
		btn_Cooked=(Button)findViewById(R.id.btn_Cooked);
		img_Subcate_title = (ImageView)findViewById(R.id.img_subcate_title);
		img_Loading_Cate = (ImageView)findViewById(R.id.img_Loading);
		img_Loading_Product = (ImageView)findViewById(R.id.img_product_loading);
		img_Search = (ImageView)findViewById(R.id.img_search);
		img_Barcode = (ImageView)findViewById(R.id.img_barcode);
		txt_ProductMenu_Cate = (TextView)findViewById(R.id.txt_productmenu_title);
		edt_Search = (EditText)findViewById(R.id.edt_search);
		btn_navigate_back = (Button)findViewById(R.id.btn_navigate_back);
		
		img_Search.setOnClickListener(headClicker);
		img_Barcode.setOnClickListener(headClicker);
		btn_Sweets.setOnClickListener(mainMenuClicker);
		btn_Snacks.setOnClickListener(mainMenuClicker);
		btn_Bakery.setOnClickListener(mainMenuClicker);
		btn_Dairy.setOnClickListener(mainMenuClicker);
		btn_Drinks.setOnClickListener(mainMenuClicker);
		btn_Cooked.setOnClickListener(mainMenuClicker);
		view_main_category.setVisibility(View.VISIBLE);
		view_main_subcategory.setVisibility(View.INVISIBLE);
		view_main_product.setVisibility(View.INVISIBLE);
		view_navigator.setVisibility(View.INVISIBLE);
		
		list_trymore.setAdapter(trymoreAdapter());
		list_trymore.setOnItemClickListener(tryMoreListener);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	
		list_Subcategory.setOnItemClickListener(subcateListListener);
		list_subcateProduct.setOnScrollListener(scrollListener);
		list_productmenu.setOnScrollListener(scrollListener);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {        //to get the return of user information
		if(intent == null) return;
		
		Bundle bundle = intent.getExtras();
		
		if(bundle == null) return;
		switch(requestCode){
		case RESULT_USERLOGIN:
			int userId = bundle.getInt("UserId");
			
			if(userId == -1) return;
			
			if(cur_User == null || cur_User.getUserId() != userId){
				cur_User = new User();
				cur_User.setUserId(userId);
				
				GetDataThread thread = new GetDataThread(MSG_GET_USER);
				thread.start();
			}
			break;
			
		case RESULT_BARCODE:
			IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		    if (result != null) {
		      String contents = result.getContents();
		      if (contents != null) {
		        String str_result = result.toString();
		        String code = str_result.split("\n")[1];
		        code = code.substring(10);
		        GetDataThread thread = new GetDataThread(MSG_GET_SEARCHBYBARCODE, code);
				thread.start();
		      }
		    }
		    break;
		}
		
	}
	
	/**************  Menu Operations  ****************/
	
	public void goBack(){                   //go back one step of the menu
		if(subMenuLevel==1){
			changeView(view_main_subcategory,view_main_category,false);
			subMenuLevel--;
		}
		else if(subMenuLevel==2){
			changeView(view_main_product, view_main_subcategory, false);
			subMenuLevel--;
		}else if(subMenuLevel==3){
			changeView(view_main_product, view_main_category, false);
			subMenuLevel=0;
		}else
			finish();
	}
	
	public AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {          //to detect if the list is over
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			if(scrollState == OnScrollListener.SCROLL_STATE_IDLE){
				if (view.getLastVisiblePosition() == (view.getCount() - 1)) {            //when the list reaches its bottom
					if(isGetting == true)
						return;
					
					if(view == list_subcateProduct){
						GetDataThread thread = new GetDataThread(MSG_GET_BYCATEGORY_MORE, category_Selected);
						int index = list_subcateProduct.getAdapter().getCount();          //count next index
						if(index % 10 == 0)
							index = index / 10;
						else
							index = index / 10 + 1;
						thread.setIndex(index);
						thread.start();
						
						Toast.makeText(getApplicationContext(), "Getting More...", Toast.LENGTH_SHORT).show();
						isGetting = true;
					}else if(view == list_productmenu){
						GetDataThread thread = new GetDataThread(MSG_GET_BYSUBCATE_MORE, category_Selected, subcate_Selected);
						int index = list_productmenu.getAdapter().getCount();
						if(index % 10 == 0)
							index = index / 10;
						else
							index = index / 10 + 1;
						
						thread.setIndex(index);
						thread.start();
						
						Toast.makeText(getApplicationContext(), "Getting More...", Toast.LENGTH_SHORT).show();
						isGetting = true;
					}
				}
			}
		}
		
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {}
	};
	
	public View.OnClickListener headClicker = new View.OnClickListener() {          //search and barcode button is clicked
		
		@Override
		public void onClick(View v) {
			if(v == img_Search){
				String txt = edt_Search.getText().toString();
				if(txt.contains(" ")){
					Toast.makeText(getApplicationContext(), "Please do not contain space", Toast.LENGTH_SHORT).show();
					return;
				}
				if(txt != null){
					GetDataThread thread = new GetDataThread(MSG_GET_SEARCHBYNAME, txt);
					thread.start();
				}
			}
			else if(v == img_Barcode){
				startScan();
			}
		}
	};
	
	public void startScan(){               //barcode scan
		IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
	      integrator.addExtra("SCAN_WIDTH", 800);
	      integrator.addExtra("SCAN_HEIGHT", 200);
	      integrator.addExtra("RESULT_DISPLAY_DURATION_MS", 3000L);
	      integrator.addExtra("PROMPT_MESSAGE", "Please Scan a Product");
	      integrator.initiateScan(IntentIntegrator.PRODUCT_CODE_TYPES);
	}
	
	public View.OnClickListener mainMenuClicker = new View.OnClickListener() {          //if a icon in main menu is clicked
		
		@Override
		public void onClick(View v) {
			
			list_subcateProduct.setAdapter(null);
			img_Loading_Cate.setVisibility(View.VISIBLE);
			changeView(view_main_category,view_main_subcategory,true);
			
			if(v == btn_Sweets){
				img_Subcate_title.setImageDrawable(getResources().getDrawable(R.drawable.sweets_big));
				list_Subcategory.setAdapter(listSweets());
				subMenuLevel++;
				category_Selected = 1;
			}
			if(v==btn_Snacks){
				img_Subcate_title.setImageDrawable(getResources().getDrawable(R.drawable.snacks_big));
				list_Subcategory.setAdapter(listSnacks());
				subMenuLevel++;
				category_Selected = 2;
			}
			if(v==btn_Bakery){
				img_Subcate_title.setImageDrawable(getResources().getDrawable(R.drawable.bakery_big));
				list_Subcategory.setAdapter(listBakery());
				subMenuLevel++;
				category_Selected = 3;
			}
			if(v==btn_Dairy){
				img_Subcate_title.setImageDrawable(getResources().getDrawable(R.drawable.dairy_big));
				list_Subcategory.setAdapter(listDairy());
				subMenuLevel++;
				category_Selected = 4;
			}
			if(v==btn_Drinks){
				img_Subcate_title.setImageDrawable(getResources().getDrawable(R.drawable.drinks_big));
				list_Subcategory.setAdapter(listDrinks());
				subMenuLevel++;
				category_Selected = 5;
			}
			if(v==btn_Cooked){
				img_Subcate_title.setImageDrawable(getResources().getDrawable(R.drawable.cooked_big));
				list_Subcategory.setAdapter(listCooked());
				subMenuLevel++;
				category_Selected = 6;
			}
			
			GetDataThread thread = new GetDataThread(MSG_GET_BYCATEGORY, category_Selected);
			thread.start();
		}
	};

	public AdapterView.OnItemClickListener productListListener = new AdapterView.OnItemClickListener() {             //a product in a list is clicked

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			HashMap<String, Object> item = (HashMap<String, Object>)arg0.getItemAtPosition(arg2); 
			String id=(String)item.get("id");
			int productId = Integer.valueOf(id);
			
			Bundle bundle = new Bundle();
			bundle.putInt("productId", productId);
			if(cur_User == null)
				bundle.putInt("userId", -1);
			else{
				bundle.putInt("userId", cur_User.getUserId());
				bundle.putInt("priority", cur_User.getPriority());
				bundle.putInt("groupId", cur_User.getGroupId());
			}
			
			Intent intent = new Intent(getApplicationContext(),ProductDetailActivity.class);
			intent.putExtras(bundle);
			startActivityForResult(intent, RESULT_USERLOGIN);
			
		}
		
	};
	
	public AdapterView.OnItemClickListener subcateListListener = new AdapterView.OnItemClickListener() {              //a sub-category is selected

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			HashMap<String, Object> item = (HashMap<String, Object>)arg0.getItemAtPosition(arg2); 
			String title=(String)item.get("title");
			
			img_Loading_Product.setVisibility(View.VISIBLE);
			list_productmenu.setAdapter(null);
			
			subcate_Selected = arg2 + 1;
			String str_Cate = null;
			switch(category_Selected){
			case 1:
				str_Cate = "Sweets >> ";
				break;
			case 2:
				str_Cate = "Snacks >> ";
				break;
			case 3:
				str_Cate = "Bakery >> ";
				break;
			case 4:
				str_Cate = "Dairy  >> ";
				break;
			case 5:
				str_Cate = "Drinks  >> ";
				break;
			case 6:
				str_Cate = "Cooked >> ";
				break;
			}
			str_Cate += title;
			txt_ProductMenu_Cate.setText(str_Cate);
			
			img_Loading_Product.setVisibility(View.VISIBLE);
			changeView(view_main_subcategory, view_main_product, true);
			subMenuLevel++; 
			
			GetDataThread thread = new GetDataThread(MSG_GET_BYSUBCATE, category_Selected, subcate_Selected);
			thread.start();
		}
		
	};
	
	public AdapterView.OnItemClickListener tryMoreListener = new AdapterView.OnItemClickListener() {           //the Try More list is clicked

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch(arg2){
			case 0:                 // Me and My groups
				startUserActivity();
				break;
			case 1:                 // Find Button
				startFindActivity();
				break;
			case 2:                //Create Proudct
				Intent intent = new Intent(getApplicationContext(), CreateProductActivity.class);
				startActivity(intent);
			}
			
		}
		
	};
	
	public void changeView(View pre, View post, boolean isForward){               //animation of changing to other menu
		if(isForward==true){
			Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.amin_cate_maincatehide);
			pre.startAnimation(animation);
			pre.setVisibility(View.INVISIBLE);
			
			animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.amin_cate_subcateshow);
			post.startAnimation(animation);
			post.setVisibility(View.VISIBLE);
			
			if(subMenuLevel == 0){
				animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha_show);
				view_navigator.startAnimation(animation);
				view_navigator.setVisibility(View.VISIBLE);
				
				
				animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha_hide);
				view_head.startAnimation(animation);
				view_head.setVisibility(View.INVISIBLE);
			}
		}
		else{
			Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_cate_subcatehide);
			pre.startAnimation(animation);
			pre.setVisibility(View.INVISIBLE);
			
			animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.amin_cate_maincateshow);
			post.startAnimation(animation);
			post.setVisibility(View.VISIBLE);
			
			if(subMenuLevel == 1||subMenuLevel == 3){
				animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha_show);
				view_head.startAnimation(animation);
				view_head.setVisibility(View.VISIBLE);
				
				
				animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_alpha_hide);
				view_navigator.startAnimation(animation);
				view_navigator.setVisibility(View.INVISIBLE);
			}
		}
	}

	public SimpleAdapter trymoreAdapter(){                                          //list of Try More
		ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("img", R.drawable.main_group);
		map.put("title", "Me & Them");
		map.put("detail", "Change your information and see what your group members are doing");
		listItem.add(map);
		
		map = new HashMap<String,Object>();
		map.put("img", R.drawable.main_find);
		map.put("title", "Let's Find");
		map.put("detail", "Let's guess and find what you might be interested in");
		listItem.add(map);
		
		map = new HashMap<String,Object>();
		map.put("img", R.drawable.product_create);
		map.put("title", "Create Product");
		map.put("detail", "Do you find something delicious?");
		listItem.add(map);
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(getApplicationContext(), listItem,
				R.layout.listcomp_trymore, new String[]{"img","title","detail"},
				new int[] {R.id.img_list_trymore, R.id.txt_listTry_title, R.id.txt_listTry_detail});
		
		return listItemAdapter;
	}

	public SimpleAdapter listSweets(){                    //sub-category of sweets
		ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("title", "Cakes");
		listItem.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title", "Ice Cream");
		listItem.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title", "Chocolate");
		listItem.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title", "Pudding");
		listItem.add(map);
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(getApplicationContext(), listItem,
				R.layout.listcomp_subcategory, new String[]{"title"},
				new int[] {R.id.txt_list_subcategoy});
		
		return listItemAdapter;
	}
	
	public SimpleAdapter listSnacks(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("title", "Nuts");
		listItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "Glazed Fruit");
		listItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "Candies");
		listItem.add(map);
		SimpleAdapter listItemAdapter = new SimpleAdapter(getApplicationContext(), listItem, 
				R.layout.listcomp_subcategory, new String[]{"title"}, new int[] {R.id.txt_list_subcategoy});
		
		return listItemAdapter;
		
	}
	
	public SimpleAdapter listBakery(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("title", "Bread");
		listItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "Chips");
		listItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "Biscuits");
		listItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "Grains");
		listItem.add(map);
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(getApplicationContext(), listItem, 
				R.layout.listcomp_subcategory, new String[]{"title"}, new int[] {R.id.txt_list_subcategoy});
		
		return listItemAdapter;
		
	}
	
	public SimpleAdapter listDairy(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("title", "Yoghurt");
		listItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "Cheese");
		listItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "Soy Milk");
		listItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "Milk");
		listItem.add(map);
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(getApplicationContext(), listItem, 
				R.layout.listcomp_subcategory, new String[]{"title"}, new int[] {R.id.txt_list_subcategoy});
		
		return listItemAdapter;
		
	}
	
	public SimpleAdapter listDrinks(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("title", "Wine&Tea");
		listItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "Soda");
		listItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "Coffee");
		listItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "Juice");
		listItem.add(map);
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(getApplicationContext(), listItem, 
				R.layout.listcomp_subcategory, new String[]{"title"}, new int[] {R.id.txt_list_subcategoy});
		
		return listItemAdapter;
		
	}
	
	public SimpleAdapter listCooked(){
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("title", "Frozen Food");
		listItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "Instant Food");
		listItem.add(map);
		
		map = new HashMap<String, Object>();
		map.put("title", "Canned Food");
		listItem.add(map);
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(getApplicationContext(), listItem, 
				R.layout.listcomp_subcategory, new String[]{"title"}, new int[] {R.id.txt_list_subcategoy});
		
		return listItemAdapter;
		
	}
	
	public void backClick(View v){                     //if the back button is clicked
		goBack();
	}
	
	
	
	/************** Product List Algorithm  ****************/
	public double getScore(User user, ProductBasicInf product){       //get the score of every product
		double user_sour = user.getSour();
		double user_sweet = user.getSweet();
		double user_bitter = user.getBitter();
		double user_spicy = user.getSpicy();
		double user_salty = user.getSalty();
		
		double product_sour = product.getSour();
		double product_sweet = product.getSweet();
		double product_bitter = product.getBitter();
		double product_spicy = product.getSpicy();
		double product_salty = product.getSalty();
		
		if (user_sour==0){
			user_sour = user_sour + 0.1;
		}
		else if (user_sweet==0){
			user_sweet = user_sweet + 0.1;
		}
		else if (user_bitter==0){
			user_bitter = user_bitter + 0.1;
		}
		else if (user_spicy==0){
			user_spicy = user_spicy + 0.1;
		}
		else if (user_salty==0){
			user_salty = user_salty + 0.1;
		}
		
		double ratio_a = user_sour/(user_sour+user_sweet+user_bitter+user_spicy+user_salty);
		double ratio_b = user_sweet/(user_sour+user_sweet+user_bitter+user_spicy+user_salty);
		double ratio_c = user_bitter/(user_sour+user_sweet+user_bitter+user_spicy+user_salty);
		double ratio_d = user_spicy/(user_sour+user_sweet+user_bitter+user_spicy+user_salty);
		double ratio_e = user_salty/(user_sour+user_sweet+user_bitter+user_spicy+user_salty);
	
		double score_sour=0;
		double score_sweet=0;
		double score_bitter=0;
		double score_spicy=0;
		double score_salty=0;
		
		double score = 0;
		
	
		
		
		score_sour = ratio_a * Math.abs(product_sour-user_sour);
		score_sweet = ratio_b * Math.abs(product_sweet - user_sweet);
		score_bitter = ratio_c * Math.abs(product_bitter - user_bitter);
		score_spicy = ratio_d * Math.abs(product_spicy-user_spicy);
		score_salty = ratio_e * Math.abs(product_salty-user_salty); 
		
		
       if (product_sour>user_sour&&product_sour-user_sour>30){
		score_sour=score_sour+30;}
       
       if (product_sweet>user_sweet&&product_sweet-user_sweet>30){	
   		score_sweet=score_sweet+30;}
       
       if (product_bitter>user_bitter&&product_bitter-user_bitter>30){
   		score_bitter=score_bitter+30;}
       
       if (product_spicy>user_spicy&&product_spicy-user_spicy>30){
   		score_spicy=score_spicy+30;}
       
       if (product_salty>user_salty&&product_salty-user_salty>30){
   		score_salty=score_salty+30;}
       
	    score= score_sour+score_sweet+score_bitter+score_spicy+score_salty;
		
		
		
		
		return score;
	}
	
	public List<ProductBasicInf> sortProduct(List<ProductBasicInf> productRankings){         //sort the list by score
		for(int i=0;i<productRankings.size();i++)
			productRankings.get(i).setRankingScore(getScore(cur_User, productRankings.get(i)));
		
		List<ProductBasicInf> product_temp = new ArrayList<ProductBasicInf>();
		int size = productRankings.size();
		for(int i=0;i<size;i++){
			double max_num = 100;
			int max_indx = 0;
			
			for(int j=0; j<size-i; j++){
				if(productRankings.get(j).getRankingScore() < max_num){
					max_num = productRankings.get(j).getRankingScore();
					max_indx = j;
				}
			}
			
			product_temp.add(productRankings.remove(max_indx));
		}
		
		return product_temp;
	}
	
	/**************  User and Group  *******************/
	private void startUserActivity(){
		Intent intent = new Intent(getApplicationContext(), UserActivity.class);
		Bundle bundle = new Bundle();
		if(cur_User != null)
			bundle.putInt("UserId", cur_User.getUserId());
		else
			bundle.putInt("UserId", -1);
		
		intent.putExtras(bundle);
		startActivityForResult(intent, RESULT_USERLOGIN);
	}
	
	private void startFindActivity(){
		Intent intent = new Intent(getApplicationContext(), UserFindActivity.class);
		Bundle bundle = new Bundle();
		if(cur_User != null)
			bundle.putInt("UserId", cur_User.getUserId());
		else
			bundle.putInt("UserId", -1);
		
		intent.putExtras(bundle);
		startActivityForResult(intent, RESULT_USERLOGIN);
	}
	
	/*************  Thread and Connection  ***************/
	public Handler threadHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case MSG_RECV_PRODUCTALL:
			case MSG_RECV_PRODUCT_BY_CATE:
			case MSG_RECV_PRODUCT_BY_CATE_MORE:
				list_subcateProduct.setAdapter(productAdapter());
				list_subcateProduct.setOnItemClickListener(productListListener);
				img_Loading_Cate.setVisibility(View.INVISIBLE);
				
				if(msg.what == MSG_RECV_PRODUCT_BY_CATE_MORE){              //roll to the last 10 line
					list_subcateProduct.setSelection(list_subcateProduct.getAdapter().getCount()-10); 
					isGetting = false;
				}
				break;
			case MSG_RECV_PRODUCT_BY_SUBCATE:
			case MSG_RECV_PRODUCT_BY_SUBCATE_MORE:
				list_productmenu.setAdapter(productAdapter());
				list_productmenu.setOnItemClickListener(productListListener);
				img_Loading_Product.setVisibility(View.INVISIBLE);
				
				if(msg.what == MSG_RECV_PRODUCT_BY_SUBCATE_MORE){
					list_productmenu.setSelection(list_productmenu.getAdapter().getCount()-10); 
					isGetting = false;
				}
				break;
			case MSG_RECV_FAILED:
				Toast.makeText(getApplicationContext(), "Data Receive Failed", Toast.LENGTH_SHORT).show();
				break;
			case MSG_RECV_PRODUCT_NOMORE:
				Toast.makeText(getApplicationContext(), "No More Product", Toast.LENGTH_SHORT).show();
				isGetting = false;
				break;
			case MSG_RECV_USER:
				//TODO: when receive a user
				break;
			case MSG_RECV_SEARCHPRODUCT:
			case MSG_RECV_SEARCH_NOTFOUND:
				txt_ProductMenu_Cate.setText("Search Result");
				changeView(view_main_category, view_main_product, true);
				subMenuLevel=3; 
				list_productmenu.setAdapter(productAdapter());
				list_productmenu.setOnItemClickListener(productListListener);
				img_Loading_Product.setVisibility(View.INVISIBLE);
				if(msg.what == MSG_RECV_SEARCH_NOTFOUND){
					txt_ProductMenu_Cate.setText("Search Result: Product Not Found");
				}
				break;
				
			}
		}
	};
	
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
	
	public class GetDataThread extends Thread{
		private int mode;
		private int arg0;
		private int arg1;
		private int index;
		private String searchInf;
		
		public GetDataThread(int mode, int arg0, int arg1){
			this.mode = mode;
			this.arg0 = arg0;
			this.arg1 = arg1;
		}
		
		public GetDataThread(int mode, int arg0){
			this.mode = mode;
			this.arg0 = arg0;
		}
		
		public GetDataThread(int mode){
			this.mode = mode;
		}
		
		public GetDataThread(int mode,String name){
			this.mode = mode;
			searchInf = name;
		}
		
		public void setIndex(int index){
			this.index = index;
		}
		
		@Override
		public void run(){
			if(mode == MSG_GET_PRODUCTALL){
				RestClientv2 rc = new RestClientv2();
				products = rc.getAllProduct();
				
				Message msg = threadHandler.obtainMessage();
				msg.what = MSG_RECV_PRODUCTALL;
				threadHandler.sendMessage(msg);
			}
			else if(mode == MSG_GET_BYCATEGORY){
				RestClientv2 rc = new RestClientv2();
				products = rc.getProductByCategory(arg0, index, listSizeDefault);

				if(products != null){
					if(cur_User != null)       //sort
						products = sortProduct(products);
					
					rc.fillBitmapProducts(products);
				}
				
				Message msg = threadHandler.obtainMessage();
				if(products == null)
					msg.what = MSG_RECV_FAILED;
				else
					msg.what = MSG_RECV_PRODUCT_BY_CATE;
				
				threadHandler.sendMessage(msg);
			}
			else if(mode == MSG_GET_BYSUBCATE){
				RestClientv2 rc = new RestClientv2();
				products = rc.getProductBySubCategory(arg0, arg1, index, listSizeDefault);
				
				
				if(products != null){
					if(cur_User != null)       //sort
						products = sortProduct(products);
					rc.fillBitmapProducts(products);
				}
				
				Message msg = threadHandler.obtainMessage();
				
				if(products == null)
					msg.what = MSG_RECV_FAILED;
				else
					msg.what = MSG_RECV_PRODUCT_BY_SUBCATE;
				
				threadHandler.sendMessage(msg);
			}
			
			else if(mode == MSG_GET_BYCATEGORY_MORE){
				RestClientv2 rc = new RestClientv2();
				List<ProductBasicInf> products_More = rc.getProductByCategory(arg0, index, listSizeDefault);
				if(products_More != null)
					rc.fillBitmapProducts(products_More);
				
				Message msg = threadHandler.obtainMessage();
				if(products_More == null)
					msg.what = MSG_RECV_FAILED;
				else if(products_More.isEmpty())
					msg.what = MSG_RECV_PRODUCT_NOMORE;
				else
					msg.what = MSG_RECV_PRODUCT_BY_CATE_MORE;
				
				products.addAll(products_More);
				threadHandler.sendMessage(msg);
			}
			
			else if(mode == MSG_GET_BYSUBCATE_MORE){
				RestClientv2 rc = new RestClientv2();
				List<ProductBasicInf> products_More = rc.getProductBySubCategory(arg0, arg1, index, listSizeDefault);
				if(products_More != null)
					rc.fillBitmapProducts(products_More);
				
				Message msg = threadHandler.obtainMessage();
				
				if(products_More == null)
					msg.what = MSG_RECV_FAILED;
				else if(products_More.isEmpty())
					msg.what = MSG_RECV_PRODUCT_NOMORE;
				else
					msg.what = MSG_RECV_PRODUCT_BY_SUBCATE_MORE;
				
				products.addAll(products_More);
				threadHandler.sendMessage(msg);
			}
			
			else if(mode == MSG_GET_USER){
				RestClientv2 rc = new RestClientv2();
				List<User> users = rc.getUserById(cur_User.getUserId());
				
				Message msg = threadHandler.obtainMessage();
				
				if(users == null || users.isEmpty())
					msg.what = MSG_RECV_FAILED;
				else{
					msg.what = MSG_RECV_USER;
					cur_User = users.get(0);
				}
				
				threadHandler.sendMessage(msg);
			}
			else if(mode == MSG_GET_SEARCHBYNAME){
				RestClientv2 rc = new RestClientv2();
				products = rc.getProductByName(searchInf);
				if(products != null)
					rc.fillBitmapProducts(products);
				
				Message msg = threadHandler.obtainMessage();
				if(products == null)
					msg.what = MSG_RECV_FAILED;
				else if(products.isEmpty())
					msg.what = MSG_RECV_SEARCH_NOTFOUND;
				else
					msg.what = MSG_RECV_SEARCHPRODUCT;
				
				threadHandler.sendMessage(msg);
			}
			else if(mode == MSG_GET_SEARCHBYBARCODE){
				RestClientv2 rc = new RestClientv2();
				products = rc.getProductByBarcode(searchInf);
				if(products != null)
					rc.fillBitmapProducts(products);
				
				Message msg = threadHandler.obtainMessage();
				if(products == null)
					msg.what = MSG_RECV_FAILED;
				else if(products.isEmpty())
					msg.what = MSG_RECV_SEARCH_NOTFOUND;
				else
					msg.what = MSG_RECV_SEARCHPRODUCT;
				
				threadHandler.sendMessage(msg);
			}
		}
		
	}
}
