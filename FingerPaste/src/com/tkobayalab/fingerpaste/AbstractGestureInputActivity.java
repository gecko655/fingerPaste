package com.tkobayalab.fingerpaste;

import android.os.Bundle;
import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.view.Menu;

public abstract class AbstractGestureInputActivity extends Activity implements OnGesturePerformedListener {

	public static final int SUCCESS = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_abstract_gesture_input);
		GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestureOverlayView1);
		gestures.addOnGesturePerformedListener(this);
	}

	
	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		onGestureInput(gesture);
	}

	abstract void onGestureInput(Gesture gesture);

}
