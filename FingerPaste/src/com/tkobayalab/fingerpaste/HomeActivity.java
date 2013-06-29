package com.tkobayalab.fingerpaste;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class HomeActivity extends Activity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

	private static final String PREF_KEY = "FingerPaste";

	private HomeManager homeManager;
	private ItemAdapter adapter;
	private Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate( savedInstanceState );
		
		setContentView( R.layout.activity_home );
		
		// HomeManagerを作成
		homeManager = new HomeManager( this );

		// ListViewの設定
		adapter = new ItemAdapter( this );
        ListView lv = (ListView)findViewById( R.id.listView );
        lv.setAdapter( adapter );
        lv.setOnItemClickListener( this );
        lv.setOnItemLongClickListener( this );
        lv.setChoiceMode( ListView.CHOICE_MODE_NONE );
        
        menu = null;
        
        // 初回起動時の処理
        SharedPreferences pref = getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
		boolean bFirst = pref.getBoolean( "First", true );
		Log.d("myTest", bFirst ? "true" : "false");
		if( bFirst == true ) {
			// データベースの初期化
			homeManager.initiateDatabase();
			
			// 初回起動フラグを折る
			SharedPreferences.Editor editor = pref.edit();
			editor.putBoolean( "First", false );
			editor.commit();
        }
        
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		homeManager.startService();
		
		// ソート順の初期化
        SharedPreferences pref = getSharedPreferences(PREF_KEY, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt( "Sort1", SortType.TYPE_SORT1_DATE );
		editor.putInt( "Sort2", SortType.TYPE_SORT2_DECS );
		editor.commit();

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
		case R.id.action_sort_1_1:
			homeManager.changeSortCondition( 0 );
			return true;
		case R.id.action_sort_1_2:
			homeManager.changeSortCondition( 1 );
			return true;
		case R.id.action_sort_2_1:
			homeManager.changeSortCondition( 2 );
			return true;
		case R.id.action_sort_2_2:
			homeManager.changeSortCondition( 3 );
			return true;
		case R.id.action_filter_1:
			homeManager.changeDisplayCondition( 0 );
			return true;
		case R.id.action_filter_2:
			homeManager.changeDisplayCondition( 1 );
			return true;
		}
		return true;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		// 選択情報を更新する＋クリップボードを書き換える
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
		// 選択情報を更新する
        Log.d("HOME", "onItemClick"+position);
		if( position != AdapterView.INVALID_POSITION ) {
			view.setSelected( true );
			adapter.setSelectedItemPosition( position );
		}
		refreshButtons();
	}

	private void refreshButtons() {
		// アクションバーのボタンのEnable/Disableを更新する
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
		// リストビューの表示をデータベースと同期させる
		// 選択情報は初期化される
		adapter.refresh();
	}
	
	public void refreshUI() {
		// 必ずリストビューを更新してからボタンを更新すること
		refreshListView();
		refreshButtons();
	}

}
