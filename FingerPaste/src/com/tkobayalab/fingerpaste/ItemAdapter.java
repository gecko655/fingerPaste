package com.tkobayalab.fingerpaste;

import java.util.ArrayList;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.util.Log;

public class ItemAdapter extends ArrayAdapter<Item> {
	private Context context;
    private int selectedItemPosition;
    
	public ItemAdapter(Context context) {
		super(context,0);
		this.context = context;
		this.selectedItemPosition = AdapterView.INVALID_POSITION;
	}
	
	public void setSelectedItemPosition( int position ) {
		selectedItemPosition = position;
	}
	
	public int getSelectedItemPosition() {
		return selectedItemPosition;
	}

    public void refresh() {
    	DatabaseManager dm = new DatabaseManager( context );
    	
    	// Preferenceを取得
		SharedPreferences pref = context.getSharedPreferences( "FingerPaste", Activity.MODE_PRIVATE );
		final int sort1 = pref.getInt( "Sort1", SortType.TYPE_SORT1_DATE );
		final int sort2 = pref.getInt( "Sort2", SortType.TYPE_SORT2_DECS );
		final int display = pref.getInt( "Display", DisplayType.TYPE_DISPLAY_ALL );
    	
    	// ArrayListをクリア
    	clear();
		
    	// ArrayListに要素を詰める
    	int[] typeOfItems = dm.getElementsId();
    	if( typeOfItems != null ) {
	    	for( int id = 0; id < DatabaseManager.MAX_ITEM; id++ ) {
	    		String text;
	    		Bitmap img;
	    		long date;
	    		// Preferenceの「表示切替」を見てaddするかそうでないかを決める
	    		if( display != DisplayType.TYPE_DISPLAY_GESTURE && typeOfItems[id] == DatabaseManager.TYPE_NO_GESTURE ) {
		    		text = dm.getText( id );
		    		date = dm.getUpdateTime( id );
		    		add( new Item( id, text, null, date ) );
	    		} else if( typeOfItems[id] == DatabaseManager.TYPE_HAS_GESTURE ) {
		    		text = dm.getText( id );
		    		date = dm.getUpdateTime( id );
		    		img = Bitmap.createScaledBitmap( dm.getGestureImage( id ), 60, 60, false );
		    		add( new Item( id, text, img, date ) );
	    		}
	    	}
    	}
    	
    	// ArrayListをソートする
    	sort( new Comparator<Item>() {
			@Override
			public int compare( Item i1, Item i2 ) {
				// Preferenceの「ソート」を見てソート順を決める
				if( sort1 == SortType.TYPE_SORT1_ALPHABET ) {
					if( sort2 == SortType.TYPE_SORT2_ACS ) {
						return + i1.getText().compareTo( i2.getText() );
					} else if( sort2 == SortType.TYPE_SORT2_DECS ) {
						return - i1.getText().compareTo( i2.getText() );
					}
				} else if( sort1 == SortType.TYPE_SORT1_DATE ) {
					if( sort2 == SortType.TYPE_SORT2_ACS ) {
						return + Long.signum( i1.getDate() - i2.getDate() );
					} else if( sort2 == SortType.TYPE_SORT2_DECS ) {
						return - Long.signum( i1.getDate() - i2.getDate() );
					}
				}
				return 0;
			}
    	});

    	// 選択情報を初期化する
    	// 削除後に選択情報が残っていると、それを参照しようとした時に実体がなくてNULLPOを吐くため
    	setSelectedItemPosition( AdapterView.INVALID_POSITION );
    	
    	notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
        	// Rowの設定
        	// TODO: フォントサイズによってTextViewのサイズを変える
        	TextView tv = new TextView( context );
        	tv.setCompoundDrawablePadding( 12 );
        	tv.setPadding( 6, 0, 6, 0 );
        	tv.setHeight( 60 );
        	tv.setMinimumHeight( 60 );
        	tv.setEllipsize( TextUtils.TruncateAt.MARQUEE );
        	tv.setTextSize( TypedValue.COMPLEX_UNIT_PX, 40 );
        	tv.setFocusable( false );
        	tv.setFocusableInTouchMode( false );
        	{
        		StateListDrawable sld = new StateListDrawable();
	            // TODO: テーマによって選択時のハイライト色を変える
	        	sld.addState( new int[] { android.R.attr.state_selected },
	        			context.getResources().getDrawable(android.R.color.holo_blue_light) );
	        	tv.setBackgroundDrawable( sld );
        	}
        	convertView = tv;
        }

        final Item item = getItem(position);
        final TextView label = (TextView) convertView;

        label.setTag( item );
        label.setText( item.getText() );
        
        Bitmap bmp = item.getBitmap();
        if( bmp != null ) {
        	label.setCompoundDrawablesWithIntrinsicBounds( new BitmapDrawable(context.getResources(),bmp),null, null, null );
        } else {
        	// TODO: NO IMAGEの画像
        	//label.setCompoundDrawablesWithIntrinsicBounds( new BitmapDrawable(context.getResources(),bmp),null, null, null );
        }
        
        return convertView;
    }
}
