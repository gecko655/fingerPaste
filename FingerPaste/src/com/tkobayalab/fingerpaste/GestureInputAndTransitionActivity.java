package com.tkobayalab.fingerpaste;

import android.os.Bundle;
import android.gesture.Gesture;
import android.view.Menu;

public class GestureInputAndTransitionActivity extends AbstractGestureInputActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_input_and_transition);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.gesture_input_and_transition, menu);
		return true;
	}

	@Override
	void onGestureInput(Gesture gesture) {
		// TODO Auto-generated method stub
		
	}

}
