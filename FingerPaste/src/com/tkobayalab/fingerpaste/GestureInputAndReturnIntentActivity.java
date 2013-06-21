package com.tkobayalab.fingerpaste;

import android.os.Bundle;
import android.content.Intent;
import android.gesture.Gesture;
import android.view.Menu;

public class GestureInputAndReturnIntentActivity extends AbstractGestureInputActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	void onGestureInput(Gesture gesture) {
		Intent intent=new Intent();
		intent.putExtra("Gesture", gesture);
		setResult(SUCCESS,intent);
		finish();
	}

}
