package com.tkobayalab.fingerpaste;

import android.content.Intent;
import android.gesture.Gesture;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

public class EditManager {
	public static final int REQUESTCODE = 124;
	private EditActivity editActivity;
	private int id;
	
	public EditManager(EditActivity editActivity){
		this.editActivity = editActivity;
	}
	
	public void editItem(String text, Gesture gesture){
		if(gesture==null){
			editItem(text);
		}else{
			DatabaseManager dbManager = new DatabaseManager(editActivity);
			dbManager.delete(id);
			dbManager.add(text, gesture);
			closeEditActivity();
		}
	}

	public void editItem(String text){
			DatabaseManager dbManager = new DatabaseManager(editActivity);
			dbManager.delete(id);
			dbManager.add(text);
			closeEditActivity();
	}

	public void closeEditActivity(){
		editActivity.finish();
	}

	public void startGestureInputActivity(){
		editActivity.startActivityForResult(new Intent(editActivity,GestureInputAndReturnIntentActivity.class), REQUESTCODE);
	}
	
	public void fillForm(int id){
		DatabaseManager dbManager = new DatabaseManager(editActivity);
		editActivity.editText.setText(dbManager.getText(id));
//		ImageButton imageButton=editActivity.imageButton;
		
		//editActivity.imageButton.setImageBitmap(dbManager.getGestureImage(id, (int)(imageView.getWidth()*0.8), (int)(imageView.getHeight()*0.8), 8, 0xffff0000));
//		editActivity.imageButton.setImageBitmap(dbManager.getGestureImage(id, (int)(imageButton.getWidth()*0.8), (int)(imageButton.getHeight()*0.8), 8, 0xffff0000));
		//editActivity.imageButton.setImageBitmap(dbManager.getGestureImage(id, 300,300, 8, 0xffff0000));
		ImageView imageView=editActivity.imageButton;
		imageView.setImageBitmap(dbManager.getGestureImage(id,(int)(imageView.getWidth()*0.8), (int)(imageView.getHeight()*0.8), 8, 0xffff0000));
	}

	public void receiveGesture(Gesture gesture) {
		ImageView imageView=editActivity.imageButton;
		//左右に1割ずつの余白を残す
		Log.d("test",""+imageView.getWidth());
		imageView.setImageBitmap(gesture.toBitmap((int)(imageView.getWidth()*0.8), (int)(imageView.getHeight()*0.8), 8, 0xffff0000));
	}
	
	public void removeGesture(){
		ImageView imageView=editActivity.imageButton;
		imageView.setImageResource(android.R.drawable.alert_light_frame);
		editActivity.gesture=null;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	
	

}
