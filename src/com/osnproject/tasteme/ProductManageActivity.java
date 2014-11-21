package com.osnproject.tasteme;

import com.osnproject.tasteme.barcode.IntentIntegrator;
import com.osnproject.tasteme.barcode.IntentResult;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class ProductManageActivity extends Activity{
	String name;
	String barcode;
	
	TextView txt_title;
	ImageView img_icon;
	Button btn_finish;
	Button btn_barcode;
	
	SeekBar seek_Sour;
	SeekBar seek_Sweet;
	SeekBar seek_Bitter;
	SeekBar seek_Spicy;
	SeekBar seek_Salty;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.productdetail_manage);
		
		Bundle bundle = getIntent().getExtras();
		name = bundle.getString("productName");
		Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("image");
		int sour = (int)bundle.getDouble("sour");
		int sweet = (int)bundle.getDouble("sweet");
		int bitter = (int)bundle.getDouble("bitter");
		int spicy = (int)bundle.getDouble("spicy");
		int salty = (int)bundle.getDouble("salty");
		
		txt_title = (TextView)findViewById(R.id.txt_productmanage_title);
		img_icon = (ImageView)findViewById(R.id.img_productmanage);
		seek_Sour = (SeekBar)findViewById(R.id.seek_productmanage_sour);
		seek_Sweet = (SeekBar)findViewById(R.id.seek_productmanage_sweet);
		seek_Bitter = (SeekBar)findViewById(R.id.seek_productmanage_bitter);
		seek_Spicy = (SeekBar)findViewById(R.id.seek_productmanage_spicy);
		seek_Salty = (SeekBar)findViewById(R.id.seek_productmanage_salty);
		btn_finish = (Button)findViewById(R.id.btn_productmanage_finish);
		btn_barcode = (Button)findViewById(R.id.btn_productmanage_barcode);
		
		seek_Sour.setProgress(sour);
		seek_Sweet.setProgress(sweet);
		seek_Bitter.setProgress(bitter);
		seek_Spicy.setProgress(spicy);
		seek_Salty.setProgress(salty);
		
		img_icon.setImageBitmap(bitmap);
		txt_title.setText(name);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
	    if (result != null) {
	      String contents = result.getContents();
	      if (contents != null) {
	        String str_result = result.toString();
	        String code = str_result.split("\n")[1];
	        code = code.substring(10);
	        barcode = code;
	        btn_barcode.setText(code);
	      }
	    }
	  }
	
	public void startScan(){
		IntentIntegrator integrator = new IntentIntegrator(ProductManageActivity.this);
		integrator.addExtra("SCAN_WIDTH", 800);
	    integrator.addExtra("SCAN_HEIGHT", 200);
	    integrator.addExtra("RESULT_DISPLAY_DURATION_MS", 3000L);
	    integrator.addExtra("PROMPT_MESSAGE", "Please Scan a Product");
	    integrator.initiateScan(IntentIntegrator.PRODUCT_CODE_TYPES);
	}
	
	public void manageClick(View v){
		if(v == btn_finish){
			Bundle bundle = new Bundle();
			int sour = seek_Sour.getProgress();
			int sweet = seek_Sweet.getProgress();
			int bitter = seek_Bitter.getProgress();
			int spicy = seek_Spicy.getProgress();
			int salty = seek_Salty.getProgress();
			
			bundle.putInt("sour", sour);
			bundle.putInt("sweet", sweet);
			bundle.putInt("bitter", bitter);
			bundle.putInt("spicy", spicy);
			bundle.putInt("salty", salty);
			if(barcode != null)
				bundle.putString("barcode", barcode);
			
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(Activity.RESULT_OK,intent);
			finish();
		}
		else if(v == btn_barcode){
			startScan();
		}
		
	}
	
	
}
