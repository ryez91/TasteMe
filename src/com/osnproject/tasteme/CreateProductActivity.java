package com.osnproject.tasteme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.osnproject.tasteme.HTTP.RestClientv2;
import com.osnproject.tasteme.Model.ProductBasicInf;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class CreateProductActivity extends Activity{
	
	ListView list_left;
	ListView list_right;
	Button btn_close;
	Button btn_submit;
	EditText edt_name;
	EditText edt_price;
	EditText edt_seller;
	EditText edt_describ;
	EditText edt_detail;
	TextView txt_title;
	
	int maincate=0;
	int subcate=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_product);
		
		list_left = (ListView)findViewById(R.id.list_productcreate_left);
		list_right = (ListView)findViewById(R.id.list_productcreate_right);
		btn_close = (Button)findViewById(R.id.btn_comment_navigate_close);
		btn_submit = (Button)findViewById(R.id.btn_comment_navigate_submit);
		txt_title = (TextView)findViewById(R.id.txt_comment_productname);
		edt_name = (EditText)findViewById(R.id.edt_productcreate_name);
		edt_price = (EditText)findViewById(R.id.edt_productcreate_price);
		edt_seller = (EditText)findViewById(R.id.edt_productcreate_seller);
		edt_describ = (EditText)findViewById(R.id.edt_productcreate_describ);
		edt_detail = (EditText)findViewById(R.id.edt_productcreate_detail);
		
		list_left.setOnItemClickListener(leftClick);
        list_right.setOnItemClickListener(rightClick);
		fillLists();
		
		txt_title.setText("Create Product");
	}
	
	public void submit(){            //submit the product
		ProductBasicInf productNew = new ProductBasicInf();
		
		String productName = edt_name.getText().toString();           //product name
		if(productName.length() < 5){
			showMsg("Product Name is too short");
			return;
		}
		productNew.setProductName(productName);
		
		String description = edt_describ.getText().toString();         //product description
		if(description.length() < 5){
			showMsg("Please give more description");
			return;
		}
		productNew.setDescription(description);
		
		if(maincate == 0){                                           //category
			showMsg("Please select a category");
			return;
		}
		productNew.setCategory(maincate);
		
		if(subcate == 0){											//subcate
			showMsg("Please select a sub-category");
			return;
		}
		productNew.setSubcategory(subcate);
		
		String price = edt_price.getText().toString();              //price
		if(price.length() > 0)
			productNew.setPrice(Double.valueOf(price));
		
		productNew.setSeller(edt_seller.getText().toString());
		productNew.setDetails(edt_detail.getText().toString());
		
		CreateThread thread = new CreateThread(productNew);
		thread.start();
	}
	
	public void showMsg(String msg){
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	public void actionClick(View v){                //close or submit button is clicked
		if(v == btn_close){
			finish();
		}else if(v == btn_submit){
			txt_title.setText("Submitting...");
			submit();
		}
	}
	
	public void fillLists(){      //fill up all the list
		
		ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("title", "Sweets");
		listItem.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title", "Bakery");
		listItem.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title", "Snacks");
		listItem.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title", "Dairy");
		listItem.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title", "Drinks");
		listItem.add(map);
		
		map = new HashMap<String,Object>();
		map.put("title", "Cooked");
		listItem.add(map);
		
		SimpleAdapter listItemAdapter = new SimpleAdapter(getApplicationContext(), listItem,
				R.layout.listcomp_createproduct, new String[]{"title"},
				new int[] {R.id.txt_list_createproduct});
		
        
        list_left.setAdapter(listItemAdapter);
        setListViewHeightBasedOnChildren(list_left);
	}
	
	private void getData(int maincate){       //get the subcategory
		ArrayList<HashMap<String,Object>> listItem = new ArrayList<HashMap<String,Object>>();
		HashMap<String,Object> map;
		
		switch(maincate){
		case 0:
			map = new HashMap<String,Object>();
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
	        break;
		case 1:
			map = new HashMap<String, Object>();
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
	        break;
		case 2:
			map = new HashMap<String, Object>();
			map.put("title", "Nuts");
			listItem.add(map);
			
			map = new HashMap<String, Object>();
			map.put("title", "Glazed Fruit");
			listItem.add(map);
			
			map = new HashMap<String, Object>();
			map.put("title", "Candies");
			listItem.add(map);
	        break;
		case 3:
			map = new HashMap<String, Object>();
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
	        break;
		case 4:
			map = new HashMap<String, Object>();
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
	        break;
		case 5:
			map = new HashMap<String, Object>();
			map.put("title", "Frozen Food");
			listItem.add(map);
			
			map = new HashMap<String, Object>();
			map.put("title", "Instant Food");
			listItem.add(map);
			
			map = new HashMap<String, Object>();
			map.put("title", "Canned Food");
			listItem.add(map);
	        break;
		}
		SimpleAdapter listItemAdapter = new SimpleAdapter(getApplicationContext(), listItem,
				R.layout.listcomp_createproduct, new String[]{"title"},
				new int[] {R.id.txt_list_createproduct});
		list_right.setAdapter(listItemAdapter);
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
	
	AdapterView.OnItemClickListener leftClick = new AdapterView.OnItemClickListener() {          //when an item in the list is clicked

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			maincate = arg2 + 1;
			getData(arg2);
			
		}
	};
	
	AdapterView.OnItemClickListener rightClick = new AdapterView.OnItemClickListener() {          //when an item in the list is clicked

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

		subcate = arg2 + 1;
			
		}
	};
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			showMsg("Product Created");
			finish();
		}
	};
	
	public class CreateThread extends Thread{
		ProductBasicInf product;
		
		public CreateThread(ProductBasicInf product){
			this.product = product;
		}
		
		@Override
		public void run(){
			RestClientv2 rc = new RestClientv2();
			rc.createNewProduct(product);
			
			handler.sendEmptyMessage(0);
		}
	}

}
