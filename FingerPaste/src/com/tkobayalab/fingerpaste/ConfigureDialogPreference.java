package com.tkobayalab.fingerpaste;

import android.app.AlertDialog;
import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ConfigureDialogPreference extends DialogPreference {

	public ConfigureDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
	}
	@Override
	protected void onPrepareDialogBuilder(AlertDialog.Builder builder){
		final LayoutInflater inflater = (LayoutInflater)
                getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.dialog_configure_gesture, null, false);
        builder.setView(layout);
        ImageView imageView = (ImageView) layout.findViewWithTag("imageView");
        DatabaseManager dbManager = new DatabaseManager(getContext());
        
        //画面サイズを取得
        Display display = ((Activity)this.getContext()).getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        
        imageView.setImageBitmap(dbManager.getGestureImage(dbManager.alphaID,(int)(p.x*0.8),(int)(p.x*0.8),8,0xffff0000));
	}
	@Override
	protected void onDialogClosed(boolean positiveResult){
	}

}
