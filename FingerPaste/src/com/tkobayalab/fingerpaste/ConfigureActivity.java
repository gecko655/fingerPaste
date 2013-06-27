package com.tkobayalab.fingerpaste;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources.Theme;
import android.gesture.Gesture;
import android.util.Log;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.Toast;

public class ConfigureActivity extends Activity implements OnPreferenceClickListener, OnSharedPreferenceChangeListener {
	
	private static final String PREF_KEY = "FingerPaste";

	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_configure);
		
        //Preference画面を表示する
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();	
        
        
		pref = getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
		pref.registerOnSharedPreferenceChangeListener(this);
	}
	
	public static class PrefsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState){
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.layout.activity_configure);
	        PreferenceManager prefManager = getPreferenceManager();
	        prefManager.setSharedPreferencesName(PREF_KEY);
			PreferenceCategory prefCategory  = (PreferenceCategory) this.findPreference("ReservedGesture");
			for(int i=0;i<prefCategory.getPreferenceCount();i++){
				prefCategory.getPreference(i).setOnPreferenceClickListener((ConfigureActivity)getActivity());
			}

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.configure, menu);
		return true;
	}

	public boolean onPreferenceClick(Preference preference) {
		if(preference.getKey().equals("ReservedGestureAlpha")){
			Log.d("test","alpha");
			startActivityForResult(new Intent(this, GestureInputAndReturnIntentActivity.class), 0);
		}else if(preference.getKey().equals("ReservedGestureBeta")){
			Log.d("test","beta");
			startActivityForResult(new Intent(this, GestureInputAndReturnIntentActivity.class), 1);//TODO HardCoded
		}else if(preference.getKey().equals("ReservedGestureGamma")){
			Log.d("test","gamma");
			startActivityForResult(new Intent(this, GestureInputAndReturnIntentActivity.class), 2);//TODO HardCoded
		}
		return false;
	}
	@Override
	public void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==AbstractGestureInputActivity.SUCCESS){
			Gesture gesture = ((Gesture)data.getExtras().getParcelable("Gesture"));
			DatabaseManager dbManager = new DatabaseManager(this);
			if(requestCode==0){
				dbManager.changeAlpha(gesture);
			}else if(requestCode==1){
				dbManager.changeBeta(gesture);
			}else if(requestCode==2){
				dbManager.changeGamma(gesture);
			}
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if(key.equals("Theme")){
			if(sharedPreferences.getString("Theme", "error").equals("holo_dark")){
				Log.d("test","dark");
				//getApplicationContext().getTheme().applyStyle(android.R.style.Theme_Holo, true);
				setTheme(android.R.style.Theme_Holo);
		        //getFragmentManager().beginTransaction().replace(android.R.id.content,
		                //new PrefsFragment()).commit();	
			}else if(sharedPreferences.getString("Theme", "error").equals("holo_light")){
				Log.d("test","light");
				//Theme theme = getApplicationContext().getTheme();
				//theme.applyStyle(android.R.style.Theme_Holo_Light, true);
				setTheme(android.R.style.Theme_Holo_Light);
		        //getFragmentManager().beginTransaction().replace(android.R.id.content,
		                //new PrefsFragment()).commit();	
			}
		}
		
	}
}
