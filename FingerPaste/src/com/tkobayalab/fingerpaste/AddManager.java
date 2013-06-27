package com.tkobayalab.fingerpaste;

import android.content.Intent;
import android.gesture.Gesture;
import android.widget.ImageView;
import android.widget.Toast;

public class AddManager {
	public static final int REQUESTCODE = 0;
	private AddActivity addActivity;
	
	public AddManager(AddActivity addActivity){
		this.addActivity = addActivity;
	}
	
	public void addItem(String text, Gesture gesture){
		if(text.isEmpty()){
			Toast.makeText(addActivity, "文字列が入力されていません", Toast.LENGTH_SHORT).show();
			return;
		}
		if(gesture==null){
			addItem(text);
		}else{
			DatabaseManager dbManager = new DatabaseManager(addActivity);
			dbManager.add(text, gesture);
			Toast.makeText(addActivity, "登録しました", Toast.LENGTH_SHORT).show();
			closeAddActivity();
		}
		//Toast.makeText(addActivity, "addItem() called: "+text, Toast.LENGTH_LONG).show();
	}

	public void addItem(String text){
		DatabaseManager dbManager = new DatabaseManager(addActivity);
		dbManager.add(text);
		Toast.makeText(addActivity, "登録しました", Toast.LENGTH_SHORT).show();
		closeAddActivity();
		//Toast.makeText(addActivity, "addItem() called: "+text, Toast.LENGTH_LONG).show();
	}

	public void closeAddActivity(){
		addActivity.finish();
	}

	public void startGestureInputActivity(){
		addActivity.startActivityForResult(new Intent(addActivity,GestureInputAndReturnIntentActivity.class), AddManager.REQUESTCODE);
	}
	
	public void receiveGesture(Gesture gesture) {
		ImageView imageView=addActivity.imageButton;
		//左右に1割ずつの余白を残す
		imageView.setImageBitmap(gesture.toBitmap((int)(imageView.getWidth()*0.8), (int)(imageView.getHeight()*0.8), 0, 0xffff0000));
	}

	public void removeGesture() {
		ImageView imageView=addActivity.imageButton;
		imageView.setImageResource(android.R.drawable.alert_light_frame);
		addActivity.gesture=null;
	}

	public void fillForm(String text) {
		addActivity.editText.setText(text);
	}

}
