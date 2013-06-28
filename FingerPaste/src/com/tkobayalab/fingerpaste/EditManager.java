package com.tkobayalab.fingerpaste;

import android.content.Intent;
import android.gesture.Gesture;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class EditManager {
	public static final int REQUESTCODE = 124;
	private EditActivity editActivity;
	private int id;
	
	public EditManager(EditActivity editActivity){
		this.editActivity = editActivity;
	}
	
	public void editItem(String text, Gesture gesture){
		if(text.isEmpty()){
			Toast.makeText(editActivity, "文字列が入力されていません", Toast.LENGTH_SHORT).show();
			return;
		}
		if(gesture==null){
			editItem(text);
		}else{
			DatabaseManager dbManager = new DatabaseManager(editActivity);
			if(dbManager.hasSimilarItem(gesture)){
				String similar = dbManager.getText(dbManager.getIdOfMaxScore(gesture));
				Toast.makeText(editActivity, "警告：\n登録されたジェスチャーは、すでに登録されているジェスチャー\n「"+similar+"」\nに似ています", Toast.LENGTH_LONG).show();
			}
			dbManager.edit(id,text, gesture);
			Toast.makeText(editActivity, "登録しました", Toast.LENGTH_SHORT).show();
			closeEditActivity();
		}
	}

	public void editItem(String text){
			DatabaseManager dbManager = new DatabaseManager(editActivity);
			dbManager.edit(id,text);
			Toast.makeText(editActivity, "登録しました", Toast.LENGTH_SHORT).show();
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
		
		ImageView imageView=editActivity.imageButton;
		//左右に1割ずつの余白を残す
		imageView.setImageBitmap(dbManager.getGestureImage(id,(int)(imageView.getWidth()*0.8), (int)(imageView.getHeight()*0.8), 8, 0xffff0000));
	}

	public void receiveGesture(Gesture gesture) {
		ImageView imageView=editActivity.imageButton;
		//左右に1割ずつの余白を残す
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
