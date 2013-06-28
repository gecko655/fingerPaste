package com.tkobayalab.fingerpaste;

import android.content.Intent;
import android.gesture.Gesture;
import android.util.Log;
import android.widget.Toast;

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
		}else{
			changeClipBoard(id);
		}
		
	}

	private void startHomeActivity(){
		gITActivity.startActivity(new Intent(gITActivity,HomeActivity.class));
		gITActivity.finish();
	}

	private void startAddActivity(){
		DatabaseManager dbManager = new DatabaseManager(gITActivity);
		if(dbManager.canAddItem()){
			Toast.makeText(gITActivity, 
					"登録されているアイテム数が"+dbManager.MAX_ITEM+"を超えているため登録出来ません。ホーム画面へ遷移します。",
					Toast.LENGTH_LONG).show();
			gITActivity.finish();
			return;
		}
		ClipboardOperator co = new ClipboardOperator(gITActivity);
		String text = co.getText();
		if(text==null||text.equals("")){
			Toast.makeText(gITActivity, "クリップボードに文字列が入っていません。", Toast.LENGTH_SHORT).show();
			gITActivity.finish();
			return;
		}
		Intent intent = new Intent(gITActivity,AddActivity.class);
		intent.putExtra("text", text);
		gITActivity.startActivity(intent);
		gITActivity.finish();
	}

	private void addItem(){
		DatabaseManager dbManager = new DatabaseManager(gITActivity);
		if(dbManager.canAddItem()){
			Toast.makeText(gITActivity, 
					"登録されているアイテム数が"+dbManager.MAX_ITEM+"を超えているため登録出来ません。ホーム画面へ遷移します。",
					Toast.LENGTH_LONG).show();
			gITActivity.finish();
			return;
		}
		ClipboardOperator co = new ClipboardOperator(gITActivity);
		String text = co.getText();
		if(text==null||text.equals("")){
			Toast.makeText(gITActivity, "クリップボードに文字列が入っていません。", Toast.LENGTH_SHORT).show();
			gITActivity.finish();
			return;
		}
		dbManager.add(text);
		Toast.makeText(gITActivity, text +"\nを登録しました。", Toast.LENGTH_SHORT).show();
		gITActivity.finish();
		
	}

	private void changeClipBoard(int id){
		ClipboardOperator co = new ClipboardOperator(gITActivity);
		DatabaseManager dbManager = new DatabaseManager(gITActivity);
		String text = dbManager.getText(id);
		co.setText(text);
		Toast.makeText(gITActivity, "クリップボードに\n"+text+"\nをコピーしました。", Toast.LENGTH_SHORT).show();
		gITActivity.finish();
	}

}
