package com.tkobayalab.fingerpaste;

import android.content.Intent;
import android.gesture.Gesture;
import android.util.Log;

public class GestureInputAndTransitionManager {
	private Gesture gesture;
	private GestureInputAndTransitionActivity gITActivity;
	
	public GestureInputAndTransitionManager(GestureInputAndTransitionActivity gestureInputAndTransitionActivity){
		gITActivity = gestureInputAndTransitionActivity;
	}
	
	public void recognize(Gesture gesture){
		DatabaseManager dbManager = new DatabaseManager(gITActivity);
		int id = dbManager.getIdOfMaxScore(gesture);
		
		if(dbManager.isAlpha(id)){
			startHomeActivity();
		}else if(dbManager.isBeta(id)){
			startAddActivity();
		}else if(dbManager.isGamma(id)){
			addItem();
			Log.d("myTest", "gamma!");
		}else{
			changeClipBoard(id);
		}
		
	}

	private void startHomeActivity(){
		gITActivity.startActivity(new Intent(gITActivity,HomeActivity.class));
		gITActivity.finish();
	}

	private void startAddActivity(){
		ClipboardOperator co = new ClipboardOperator(gITActivity);
		Intent intent = new Intent(gITActivity,AddActivity.class);
		intent.putExtra("text", co.getText());
		gITActivity.startActivity(intent);
		gITActivity.finish();
	}

	private void addItem(){
		ClipboardOperator co = new ClipboardOperator(gITActivity);
		DatabaseManager dbManager = new DatabaseManager(gITActivity);
		dbManager.add(co.getText());
		gITActivity.finish();
		
	}

	private void changeClipBoard(int id){
		ClipboardOperator co = new ClipboardOperator(gITActivity);
		DatabaseManager dbManager = new DatabaseManager(gITActivity);
		co.setText(dbManager.getText(id));
		gITActivity.finish();
	}

}
