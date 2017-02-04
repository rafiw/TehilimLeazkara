package com.solve.it;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class Viewthilim extends Activity {
	float textsize;
	TextView tv;
	String text;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		//full screen
		if(getIntent().getExtras().getBoolean("fullScreen",true)){
			requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
		if(getIntent().getExtras().getBoolean("textBlack"))
			setContentView(R.layout.view_thilim_new_white);
		else
			setContentView(R.layout.view_thilim_new);
        tv= (TextView) findViewById(R.id.thilimtext);
		setSettings();
        text=getIntent().getExtras().getString("txt");
        boolean rightToLeft=getIntent().getExtras().getBoolean("rightToLeft");
        if (rightToLeft)
        	tv.setGravity(Gravity.RIGHT);
        else
        	tv.setGravity(Gravity.LEFT);
        tv.setText(text,TextView.BufferType.SPANNABLE);
	}
	
	private void setSettings() {
        //change font
		String fontName=getIntent().getExtras().getString("fontType");
		if(fontName.contains("ttf")){
			Typeface hebFont=Typeface.createFromAsset(getAssets(),fontName);
			tv.setTypeface(hebFont);
		}
		//font size
		String fontSize= getIntent().getExtras().getString("fontSize");
		try
		{
			tv.setTextSize(Float.parseFloat(fontSize));
		}catch (NumberFormatException e) {
			Toast.makeText(this, "לא בחרתם מספר שלם בהגדרות",Toast.LENGTH_LONG ).show();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPreferences.edit();
	    editor.putFloat("size", textsize);
	    editor.commit();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
		textsize=sharedPreferences.getFloat("size", 26);
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.zoomIn:
	            zoomIn();
	            return true;
	        case R.id.zoomOut:
	            zoomOut();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	private void zoomOut() {
		if(textsize>2)
			textsize-=1;
		tv.setTextSize(textsize);
	}

	private void zoomIn() {
		textsize+=1;
		tv.setTextSize(textsize);
	}


}
