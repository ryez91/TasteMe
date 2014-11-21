package com.osnproject.tasteme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class CreateCommentActivity extends Activity{
	Button btn_close;
	Button btn_submit;
	TextView txt_title;
	RatingBar rat_bar;
	EditText edt_comment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_comment);
		
		btn_close = (Button)findViewById(R.id.btn_comment_navigate_close);
		btn_submit = (Button)findViewById(R.id.btn_comment_navigate_submit);
		txt_title = (TextView)findViewById(R.id.txt_comment_productname);
		rat_bar = (RatingBar)findViewById(R.id.rat_createcmt);
		edt_comment = (EditText)findViewById(R.id.edt_comment);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String productName = bundle.getString("ProductName");
		if(productName.length()>15)
			productName = productName.substring(0, 15) + "..";
		
		txt_title.setText(productName);
		
	}
	
	public void actionClick(View v){
		if(v == btn_close){
			finish();
		}
		else{
			float rating = rat_bar.getRating();
			int score = (int)(rating * 20);
			String text = edt_comment.getText().toString();
			
			Bundle bundle = new Bundle();
			bundle.putInt("score", score);
			bundle.putString("text", text);
			
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
	}

}
