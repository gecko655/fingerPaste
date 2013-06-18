package com.tkobayalab.fingerpaste;

import android.os.Bundle;
import android.app.Activity;
import android.gesture.Gesture;
import android.view.Menu;

public abstract class AbstractGestureInputActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abstract_gesture_input);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.abstract_gesture_input, menu);
		return true;
	}
	
	abstract void onGestureInput(Gesture gesture);

}
