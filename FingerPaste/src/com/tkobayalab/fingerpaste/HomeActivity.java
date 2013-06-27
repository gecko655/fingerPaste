package com.tkobayalab.fingerpaste;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class HomeActivity extends Activity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
	
	private HomeManager homeManager;
	private ItemAdapter adapter;
	private Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		
		setContentView( R.layout.activity_home );
		
		homeManager = new HomeManager( this );

		adapter = new ItemAdapter( this );
        ListView lv = (ListView)findViewById( R.id.listView );
        lv.setAdapter( adapter );
        lv.setOnItemClickListener( this );
        lv.setOnItemLongClickListener( this );
        lv.setChoiceMode( ListView.CHOICE_MODE_NONE );        
        menu = null;
        
        // TODO: 初回起動時の処理
        // if( ) {
        // homeManager.initiateDatabase();
        // }
        
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		homeManager.startService();
    	refreshUI();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("HOME", "onCreateOptionsMenu");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		this.menu = menu;
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
        Log.d("HOME", "onOptionsItemSelected");
		int position = adapter.getSelectedItemPosition();
		switch( item.getItemId() ) {
		case R.id.action_overwrite:
			if( position != AdapterView.INVALID_POSITION ) {
				homeManager.overwriteClipboard( adapter.getItem( position ).getId() );
			}
			return true;
		case R.id.action_add:
			homeManager.startAddActivity();
			return true;
		case R.id.action_edit:
			if( position != AdapterView.INVALID_POSITION ) {
				homeManager.startEditActivity( adapter.getItem( position ).getId() );
			}
			return true;
		case R.id.action_settings:
			homeManager.startConfigureActivity();
			return true;
		case R.id.action_delete:
			if( position != AdapterView.INVALID_POSITION ) {
				homeManager.deleteItem( adapter.getItem( position ).getId() );
			}
			return true;
		case R.id.action_delete_all:
			homeManager.deleteAllItems();
			return true;
		// TODO: ソートボタン、書き換えボタン
		case R.id.action_sort_1_1:
			return true;
		case R.id.action_sort_1_2:
			return true;
		case R.id.action_sort_2_1:
			return true;
		case R.id.action_sort_2_2:
			return true;
		case R.id.action_filter_1:
			return true;
		case R.id.action_filter_2:
			return true;
		}
		return true;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("HOME", "onItemLongClick"+position);
		if( position != AdapterView.INVALID_POSITION ) {
			adapter.setSelectedItemPosition( position );
			homeManager.overwriteClipboard( adapter.getItem( position ).getId() );
		}
		refreshButtons();
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("HOME", "onItemClick"+position);
		if( position != AdapterView.INVALID_POSITION ) {
			view.setSelected( true );
			adapter.setSelectedItemPosition( position );
		}
		refreshButtons();
	}

	private void refreshButtons() {
		if( menu == null ) {
			return;
		}
		MenuItem item1 = menu.findItem( R.id.action_edit );
		MenuItem item2 = menu.findItem( R.id.action_delete );
		int position = adapter.getSelectedItemPosition();
		if( position != AdapterView.INVALID_POSITION ) {
			item1.setEnabled( true );
			item2.setEnabled( true );
		} else {
			item1.setEnabled( false );
			item2.setEnabled( false );
		}
		
	}

	private void refreshListView() {
		adapter.refresh();
	}
	
	public void refreshUI() {
		// 必ずリストビューを更新してからボタンを更新すること
		refreshListView();
		refreshButtons();
	}

}
