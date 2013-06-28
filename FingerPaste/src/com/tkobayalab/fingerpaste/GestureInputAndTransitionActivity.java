package com.tkobayalab.fingerpaste;

import android.os.Bundle;
import android.gesture.Gesture;
import android.view.Menu;

public class GestureInputAndTransitionActivity extends AbstractGestureInputActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	void onGestureInput(Gesture gesture) {
		GestureInputAndTransitionManager gITManager = new GestureInputAndTransitionManager(this);
		gITManager.recognize(gesture);
	}

}
