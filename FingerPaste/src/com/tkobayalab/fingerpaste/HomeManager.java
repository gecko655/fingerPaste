package com.tkobayalab.fingerpaste;

import android.content.Intent;

public class HomeManager {
	private HomeActivity homeActivity;
	
	public HomeManager(HomeActivity homeActivity){
		this.homeActivity = homeActivity;
	}
	
	public void startAddActivity() {
	}
	
	public void startEditActivity(int id) {
	}
	
	public void startConfigureActivity() {
	}
	
	public void deleteItem(int id) {
	}
	
	public void deleteAllItems() {
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
