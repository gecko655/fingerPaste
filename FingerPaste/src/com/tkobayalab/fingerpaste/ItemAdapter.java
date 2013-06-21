package com.tkobayalab.fingerpaste;

import java.util.ArrayList;
import java.util.Comparator;

import android.content.Context;
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

public class ItemAdapter extends ArrayAdapter<Item> {
	private Context context;
    private final ArrayList<Item> items;
    private int selectedItemPosition;
    private final StateListDrawable sld;
    
	public ItemAdapter(Context context) {
		super(context,0);
		this.context = context;
		this.items = new ArrayList<Item>();
		this.selectedItemPosition = AdapterView.INVALID_POSITION;
		
    	this.sld = new StateListDrawable();
        // TODO: テーマによって選択時のハイライト色を変える
    	this.sld.addState( new int[] { android.R.attr.state_selected },
    			context.getResources().getDrawable(android.R.color.holo_blue_light) );
	}
	
	public void setSelectedItemPosition( int position ) {
		selectedItemPosition = position;
	}
	
	public int getSelectedItemPosition() {
		return selectedItemPosition;
	}

    public void refresh() {
    	DatabaseManager dm = new DatabaseManager( context );
    	
		items.clear();
		
    	int[] ids = dm.getElementsId();
    	for( int id : ids ) {
    		Bitmap img = dm.getGestureImage( id );
    		if( img != null ) {
    			img = Bitmap.createScaledBitmap( img, 60, 60, false );
    		}
    		String text = dm.getText( id );
    		// TODO: Preferenceの「表示切替」を見てaddするかそうでないかを決める
    		// TODO: Dateの実装
    		add( new Item( id, text, img ) );
    	}
    	
    	sort( new Comparator<Item>() {
			@Override
			public int compare( Item i1, Item i2 ) {
				// TODO: Preferenceの「ソート」を見てソート順を決める
				switch( 0 ) {
				case 0:
					return + i1.getText().compareTo( i2.getText() );
				default:
					return - i1.getText().compareTo( i2.getText() );
				}
			}
    	});
    	
    	notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
        	TextView tv = new TextView( context );
        	tv.setCompoundDrawablePadding( 12 );
        	tv.setPadding( 6, 0, 6, 0 );
        	tv.setHeight( 60 );
        	tv.setMinimumHeight( 60 );
        	tv.setEllipsize( TextUtils.TruncateAt.MARQUEE );
        	tv.setTextSize( TypedValue.COMPLEX_UNIT_PX, 40 );
        	tv.setFocusable( false );
        	tv.setFocusableInTouchMode( false );
        	tv.setBackgroundDrawable( sld );
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
