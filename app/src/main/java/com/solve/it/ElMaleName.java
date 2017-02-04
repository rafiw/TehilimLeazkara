package com.solve.it;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class ElMaleName extends Activity{
	
	Button okButto;
	Button genericButton;
	ToggleButton tgButton;
	EditText name;
	EditText parentName ;
	String str;
	Intent data;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.el_male_layout);
		okButto=(Button) findViewById(R.id.okButtonElMale);
		genericButton =(Button) findViewById(R.id.genericButtonElMale);
		tgButton = (ToggleButton) findViewById(R.id.sonGirltoggleButton);
		name =(EditText) findViewById(R.id.deceseNameElMale);
		parentName=(EditText) findViewById(R.id.parentNameElMale);
		data= new Intent();
		
		String nameOfDec=getIntent().getStringExtra("name");
		if (nameOfDec.length()>0) {
			name.setText(nameOfDec);
			name.setHint("");
		}
	
		okButto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String boy=" בן ";
				Boolean isBoy=true;
				if (!tgButton.isChecked()){
					boy=" בת ";
					isBoy=false;
				}
				str=" "+name.getText().toString()+boy+ parentName.getText().toString()+" ";
				data.putExtra("txt", str);
				data.putExtra("boy", isBoy);
				setResult(0,data );
				finish();
			}
		});
		
		genericButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				str="";
				setResult(1,data );
				finish();
			}
		});
	}
}
