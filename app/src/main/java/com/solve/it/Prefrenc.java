package com.solve.it;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public  class Prefrenc extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}

}
