package com.tkobayalab.fingerpaste;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

public class HomeManager {
	private HomeActivity homeActivity;
	
	public HomeManager(HomeActivity homeActivity){
		this.homeActivity = homeActivity;
	}
	
	public void startAddActivity() {
		Intent intent = new Intent(homeActivity,AddActivity.class);
		homeActivity.startActivity(intent);
	}
	
	public void startEditActivity(int id) {
		Intent intent = new Intent(homeActivity,EditActivity.class);
		intent.putExtra("ID", id);
		homeActivity.startActivity(intent);
	}
	
	public void startConfigureActivity() {
		Intent intent = new Intent(homeActivity,ConfigureActivity.class);
		homeActivity.startActivity(intent);
	}
	
	public void deleteItem(int id) {
    	DatabaseManager dm = new DatabaseManager( homeActivity );
		String text = dm.getText( id );
    	dm.delete( id );
    	homeActivity.refreshUI();
		if( text != null ) {
	    	// TOAST表示
			Toast.makeText( homeActivity, "アイテム\n"+text +"\nを削除しました。", Toast.LENGTH_SHORT ).show();
		}
	}
	
	public void deleteAllItems() {
		// Create Dialog
		// TODO: 文字列のリソース化
        AlertDialog.Builder alertDlg = new AlertDialog.Builder( homeActivity );
        alertDlg.setTitle("確認");
        alertDlg.setMessage("すべてのアイテムを削除しますか？");
        alertDlg.setPositiveButton(
            R.string.OK,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                	DatabaseManager dm = new DatabaseManager( homeActivity );
                	dm.deleteAllItem();
                	homeActivity.refreshUI();
        	    	// TOAST表示
        			Toast.makeText( homeActivity, "すべてのアイテムを削除しました。", Toast.LENGTH_SHORT ).show();
                }
            });
        alertDlg.setNegativeButton(
            R.string.cancel,
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });

        // Show Dialog
        alertDlg.create().show();
	}
	
	public void changeSortCondition(int type) {
		// プレファレンスを変更する
		SharedPreferences pref = homeActivity.getSharedPreferences( "FingerPaste", Activity.MODE_PRIVATE );
		int sort1 = pref.getInt( "Sort1", SortType.TYPE_SORT1_DATE );
		int sort2 = pref.getInt( "Sort2", SortType.TYPE_SORT2_DECS );
		if( type == 0 ) {
			sort1 = SortType.TYPE_SORT1_DATE;
		} else if( type == 1 ) {
			sort1 = SortType.TYPE_SORT1_ALPHABET;
		} else if( type == 2 ) {
			sort2 = SortType.TYPE_SORT2_ACS;
		} else if( type == 3 ) {
			sort2 = SortType.TYPE_SORT2_DECS;
		}
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt( "Sort1", sort1 );
		editor.putInt( "Sort2", sort2 );
		editor.commit();
		
    	homeActivity.refreshUI();
	}
	
	public void changeDisplayCondition(int type) {
		// プレファレンスを変更する
		SharedPreferences pref = homeActivity.getSharedPreferences( "FingerPaste", Activity.MODE_PRIVATE );
		int display = pref.getInt( "Display", DisplayType.TYPE_DISPLAY_ALL );
		if( type == 0 ) {
			display = DisplayType.TYPE_DISPLAY_ALL;
		} else if( type == 1 ) {
			display = DisplayType.TYPE_DISPLAY_GESTURE;
		}
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt( "Display", display );
		editor.commit();
		
    	homeActivity.refreshUI();
	}
	
	public void overwriteClipboard(int id) {
		ClipboardOperator co = new ClipboardOperator( homeActivity );
		DatabaseManager dm = new DatabaseManager( homeActivity );
		String text = dm.getText( id );
		if( text != null ) {
			co.setText( text );
	    	// TOAST表示
			Toast.makeText( homeActivity, "クリップボードに\n"+text+"\nをコピーしました。", Toast.LENGTH_SHORT ).show();
		}
	}
	
	public void startService() {
		homeActivity.startService( new Intent( homeActivity.getBaseContext(), FPService.class ) );
	}
	
	public void initiateDatabase() {
		DatabaseManager dm = new DatabaseManager( homeActivity );
		dm.initiateDatabase( homeActivity.getResources() );
	}
	
}
