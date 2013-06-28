package com.tkobayalab.fingerpaste;

import android.app.AlertDialog;
import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ConfigureDialogPreference extends DialogPreference {
	private int id;

	public ConfigureDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		String alphabet = getKey();
		if(alphabet.equals("ReservedGestureAlpha")){
			id = ConfigureActivity.alpha;
		}else if(alphabet.equals("ReservedGestureBeta")){
			id = ConfigureActivity.beta;
		}else if(alphabet.equals("ReservedGestureGamma")){
			id = ConfigureActivity.gamma;
		}
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
		if(positiveResult){
			((ConfigureActivity)getContext()).startGestureActivity(id);
		}
	}

}
