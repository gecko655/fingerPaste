package com.tkobayalab.fingerpaste;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class HomeManager {
	private HomeActivity homeActivity;
	
	public HomeManager(HomeActivity homeActivity){
		this.homeActivity = homeActivity;
	}
	
	public void startAddActivity() {
		Intent intent = new Intent(homeActivity,AddActivity.class);
		homeActivity.startActivity(intent);
		homeActivity.finish();
	}
	
	public void startEditActivity(int id) {
		Intent intent = new Intent(homeActivity,EditActivity.class);
		intent.putExtra("ID", id);
		homeActivity.startActivity(intent);
		homeActivity.finish();
	}
	
	public void startConfigureActivity() {
		Intent intent = new Intent(homeActivity,ConfigureActivity.class);
		homeActivity.startActivity(intent);
		homeActivity.finish();
	}
	
	public void deleteItem(int id) {
    	DatabaseManager dm = new DatabaseManager( homeActivity );
    	dm.delete( id );
    	homeActivity.refreshUI();
    	// TODO: TOAST表示
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
	}
	
	public void changeDisplayCondition(int type) {
	}
	
	public void overrideClipboard(int id) {
	}
	
	public void startService() {
		homeActivity.startService( new Intent( homeActivity.getBaseContext(), FPService.class ) );
	}
	
	public void initiateDatabase() {
		GestureLibraryManager.initializeGestures( homeActivity.getResources() );
	}
	
}
