package com.solve.it;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TehilimLeazkaraActivity extends Activity {
	StringBuilder sb;
	String[] kyt;
	Button ok;
	Button elMale;
	Button kadishY;
	Button kadishD;
	Button entButton;
	SharedPreferences prefs;
	Intent view;
	EditText name;
	int selection=0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs=PreferenceManager.getDefaultSharedPreferences(TehilimLeazkaraActivity.this);
		setContentView(R.layout.main);
		setWidgets();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		selection = Integer.parseInt(prefs.getString("listpref", "0"));
	}

	public void sendTextToView(String str) {
		//attach to bundle
		//font
		String fontType = prefs.getString("fontlistpref", "0");
		//fontSize
		String fontSize = prefs.getString("fontSize", "26");
		try {
			if (Integer.parseInt(fontSize) == 0
					|| Integer.parseInt(fontSize) > 50
					|| Integer.parseInt(fontSize) < 1) {
				fontSize = "26";
				Toast.makeText(this, "גדול הפונט שגוי 26 נבחר כבררת מחדל",
						Toast.LENGTH_LONG).show();
			}

		} catch (NumberFormatException e) {
			Toast.makeText(this, "גדול הפונט שגוי 26 נבחר כבררת מחדל",
					Toast.LENGTH_LONG).show();
		}
		// use full screen
		boolean fullScreen = prefs.getBoolean("fullScreenOn", true);
		// colors
		boolean textBlack = prefs.getBoolean("blackText", true);
		// show nikud
		boolean showNikud = prefs.getBoolean("isFontEnabled", true);
		//fix right to left
		boolean rightToLeft = prefs.getBoolean("right_to_left", true);

		if(!showNikud){
			str=removeNikud(str);
		}
		view = new Intent("com.solve.it.THILIM");

		view.putExtra("fontType", fontType);
		view.putExtra("fontSize", fontSize);
		view.putExtra("fullScreen", fullScreen);
		view.putExtra("textBlack", textBlack);
		view.putExtra("rightToLeft", rightToLeft);
		//text to show
		view.putExtra("txt", str);
		//start view
		startActivity(view);
	}
	
	private void setArray() {
		sb = new StringBuilder(300);
		sb.append("פרק ל\"ג" + "\n"
				+ getResources().getString(R.string.tehilimLg) + "\n\n");
		sb.append("פרק ט\"ז" + "\n"
				+ getResources().getString(R.string.tehilimTz) + "\n\n");
		sb.append("פרק י\"ז" + "\n"
				+ getResources().getString(R.string.tehilimYz) + "\n\n");
		sb.append("פרק ע\"ב" + "\n"
				+ getResources().getString(R.string.tehilimAb) + "\n\n");
		sb.append("פרק צ\"א" + "\n"
				+ getResources().getString(R.string.tehilimTza) + "\n\n");
		sb.append("פרק ק\"ד" + "\n"
				+ getResources().getString(R.string.tehilimKd) + "\n\n");
		sb.append("פרק ק\"ל" + "\n"
				+ getResources().getString(R.string.tehilimKl) + "\n\n");
		}
	
	private String removeNikud(String sb2) {
		return sb2.replaceAll("[ְְֱֲִֵֶַָֹֹֻּׁׂ]","");
	}
	

	private void setWidgets() {
		//for nusach
		selection = Integer.parseInt(prefs.getString("listpref", "0"));
		ok = (Button) findViewById(R.id.okBbutton);
		name = (EditText) findViewById(R.id.name);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setArray();
				String nametoProsess =new String( name.getText().toString()
						.replace(" ", "")
						+ "נשמה");
				getTehilim(nametoProsess);
				sendTextToView(sb.toString());
			}
		});

		entButton=(Button) findViewById(R.id.entranceButton);
		entButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String string;
				switch (selection) {
				case 0:
					string=getResources().getString(R.string.enterAshkenaz);
					break;

				default:
					string=getResources().getString(R.string.enterSfardi);
					break;
				}
				sendTextToView(string);
			}
		});
		elMale=(Button)findViewById(R.id.elMaleRachamim);
		elMale.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//there is text send it to dialog
				if (name.getText().length()>0){
					StringBuilder sb = new StringBuilder();
					sb.append(getResources().getString(R.string.elMaleBeginGeneric));
					sb.append(name.getText().toString());
					if(name.getText().toString().contains(" בן "))
						sb.append(getResources().getString(R.string.elMaleMan));
					else if (name.getText().toString().contains(" בת "))
						sb.append(getResources().getString(R.string.elMaleWoman));
					else{
						sb.delete(0, sb.length());
						sb.append(getResources().getString(R.string.elMaleGeneric));
					}
					sendTextToView(sb.toString());
				}
				else{
					view = new Intent("com.solve.it.ELMALENAME");
					view.putExtra("name", "");
					startActivityForResult(view, 0);
				}
				
			}
		});
		kadishY=(Button) findViewById(R.id.kadishYatomButton);
		
		kadishY.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String str;
				switch (selection) {
				case 1:
					str = getResources().getString(R.string.kadishYatomSfard);
					break;
				case 2:
					str = getResources().getString(R.string.kadishYatomEdot);
					break;
				case 3:
					str = getResources().getString(R.string.kadishyatomTeiman);
					break;
				case 0:
				default:
					str = getResources().getString(R.string.kadishYatomAshkenaz);
					break;
				}
				if (str != null) {
					sendTextToView(str);
				}
				
			}
		});
		kadishD=(Button) findViewById(R.id.kadishDerabananButton);
		kadishD.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String str;
				switch (selection) {
				case 1:
					str = getResources().getString(R.string.kadishDSfard);
					break;
				case 2:
					str = getResources().getString(R.string.kadishDEdot);
					break;
				case 3:
					str = getResources().getString(R.string.kadishDTeiman);
					break;
				case 0:
				default:
					str = getResources().getString(R.string.kadishDAshkenaz);
					break;
				}
				if (str != null) {
					sendTextToView(str);
				}
			}
		});
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		StringBuilder sb = new StringBuilder();
		//user typed name
		if ((resultCode==1) ||(data==null)){
			sendTextToView(getResources().getString(R.string.elMaleGeneric));
		}
		else if ((resultCode==0)&& (data!=null)) {
			sb.append(getResources().getString(R.string.elMaleBeginGeneric));
			sb.append(data.getStringExtra("txt"));
			if(data.getBooleanExtra("boy", true))
				sb.append(getResources().getString(R.string.elMaleMan));
			
			else
				sb.append(getResources().getString(R.string.elMaleWoman));
			sendTextToView(sb.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.first_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id._about:
			Intent i= new Intent("com.solve.it.ABOUT");
			startActivity(i);
			return true;
		case R.id._settings:
			Intent j = new Intent("com.solve.it.PREFS");
			j.putExtra("run", true);
			startActivity(j);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	private void getTehilim(String name) {
		kyt = getResources().getStringArray(R.array.kytn);
		for (int i = 0; i < name.length(); i++)
			sb.append(getKyt(name.charAt(i)));
	}

	private String getKyt(char letter) {
		if (letter == 'א')
			return "א" + "\n" + kyt[0] + "\n\n";
		if (letter == 'ב')
			return "ב" + "\n" + kyt[1] + "\n\n";
		if (letter == 'ג')
			return "ג" + "\n" + kyt[2] + "\n\n";
		if (letter == 'ד')
			return "ד" + "\n" + kyt[3] + "\n\n";
		if (letter == 'ה')
			return "ה" + "\n" + kyt[4] + "\n\n";
		if (letter == 'ו')
			return "ו" + "\n" + kyt[5] + "\n\n";
		if (letter == 'ז')
			return "ז" + "\n" + kyt[6] + "\n\n";
		if (letter == 'ח')
			return "ח" + "\n" + kyt[7] + "\n\n";
		if (letter == 'ט')
			return "ט" + "\n" + kyt[8] + "\n\n";
		if (letter == 'י')
			return "י" + "\n" + kyt[9] + "\n\n";
		if (letter == 'כ')
			return "כ" + "\n" + kyt[10] + "\n\n";
		if (letter == 'ך')
			return "ך" + "\n" + kyt[10] + "\n\n";
		if (letter == 'ל')
			return "ל" + "\n" + kyt[11] + "\n\n";
		if (letter == 'מ')
			return "מ" + "\n" + kyt[12] + "\n\n";
		if (letter == 'ם')
			return "מ" + "\n" + kyt[12] + "\n\n";
		if (letter == 'נ')
			return "נ" + "\n" + kyt[13] + "\n\n";
		if (letter == 'ן')
			return "ן" + "\n" + kyt[13] + "\n\n";
		if (letter == 'ס')
			return "ס" + "\n" + kyt[14] + "\n\n";
		if (letter == 'ע')
			return "ע" + "\n" + kyt[15] + "\n\n";
		if (letter == 'פ')
			return "פ" + "\n" + kyt[16] + "\n\n";
		if (letter == 'ף')
			return "ף" + "\n" + kyt[16] + "\n\n";
		if (letter == 'צ')
			return "צ" + "\n" + kyt[17] + "\n\n";
		if (letter == 'ץ')
			return "ץ" + "\n" + kyt[17] + "\n\n";
		if (letter == 'ק')
			return "ק" + "\n" + kyt[18] + "\n\n";
		if (letter == 'ר')
			return "ר" + "\n" + kyt[19] + "\n\n";
		if (letter == 'ש')
			return "ש" + "\n" + kyt[20] + "\n\n";
		if (letter == 'ת')
			return "ת" + "\n" + kyt[21] + "\n\n";
		return null;
	}

}