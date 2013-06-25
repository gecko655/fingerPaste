package com.tkobayalab.fingerpaste;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;

public class ConfigureActivity extends Activity{
	
	private static final String PREF_KEY = "FingerPaste";

	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_configure);
		
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();	
		pref = getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
	}
	
	public static class PrefsFragment extends PreferenceFragment{
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.layout.activity_configure);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configure, menu);
		return true;
	}

}
