package com.tkobayalab.fingerpaste;

import java.util.prefs.Preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;


public class DetectView extends View implements View.OnTouchListener, SharedPreferences.OnSharedPreferenceChangeListener {

	private static final int VIEW_WIDTH = 4;

	private final FPService service;

	private WindowManager.LayoutParams params;
	private Paint paint;
	private Path path;
	
	// ドラッグ中のときtrue
	private boolean isTouching;
	
	private PointF src, dest;

	public DetectView( FPService service ) {
		super((Context)service);

		this.service = service;

		this.params = new WindowManager.LayoutParams(
			VIEW_WIDTH,
			getDetectHeight(),
			WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
			WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
			PixelFormat.TRANSLUCENT);
		this.params.gravity = getGravity();
		
		this.paint = new Paint();
		this.path = new Path();

		this.isTouching = false;

		// DetectViewをOverlayに設置
		WindowManager wm = (WindowManager) service.getSystemService(Context.WINDOW_SERVICE);
		wm.addView( this, params );
		
		setOnTouchListener( this );
		
		// 背景を透明に
		setBackgroundColor( Color.argb( 0, 0, 0, 0 ) );
		
		// スワイプ基点の設定変更されたら即対応する
		SharedPreferences pref = service.getSharedPreferences( "FingerPaste", Activity.MODE_PRIVATE );
		pref.registerOnSharedPreferenceChangeListener( this );
	}

	@Override
	protected void onDraw( Canvas canvas ) {
		if( isTouching ) {
			// Clip Canvas
			path.reset();
			path.addCircle( src.x, src.y, getRadius() + 5, Path.Direction.CCW );
			canvas.clipPath( path );
			
			// Draw boundary
			paint.setStrokeWidth( 1 );
			paint.setColor( Color.argb( 128, 0, 0, 0 ) );
			canvas.drawCircle( src.x, src.y, getRadius(), paint );
			
			// Draw swipe stroke
			paint.setStrokeWidth( 5 );
			if( canAccept() ) {
				paint.setColor( Color.WHITE );
			} else {
				paint.setColor( Color.GRAY );
			}
			canvas.drawCircle( src.x, src.y, 10, paint );
			canvas.drawLine( src.x, src.y, dest.x, dest.y, paint );
		}
	}
	
	private boolean canAccept() {
		// swipeとみなすか否かを返す
		PointF pt1 = src;
		PointF pt2 = dest;
		float dx = Math.abs( pt1.x - pt2.x );
		float dy = Math.abs( pt1.y - pt2.y );
		int radius = getRadius();
		if( dx * dx + dy * dy > radius * radius ) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch( event.getAction() ) {
		case MotionEvent.ACTION_DOWN:
			isTouching = true;
			return onTouchDown( event );
		case MotionEvent.ACTION_MOVE:
			return onTouchMove( event );
		case MotionEvent.ACTION_UP:
			isTouching = false;
			return onTouchUp( event );
		}
		return false;
	}
	private boolean onTouchDown(MotionEvent event) {
		// DetectViewを最大化する
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.MATCH_PARENT;
		updateLayout();
		
		// 背景をちょっと暗くする
		setBackgroundColor( Color.argb( 128, 0, 0, 0 ) );
		
		src = new PointF( event.getRawX(), event.getRawY() );
		dest = src;
		
		invalidate();
		
		return false;
	}

	private boolean onTouchMove(MotionEvent event) {
		dest = new PointF( event.getRawX(), event.getRawY() );
		
		invalidate();
		
		return false;
	}

	private boolean onTouchUp(MotionEvent event) {
		dest = new PointF( event.getRawX(), event.getRawY() );
		if( canAccept() ) {
			service.onSwipe();
		}
		params.width = VIEW_WIDTH;
		params.height = getDetectHeight();
		updateLayout();
		
		//　背景を透明にし直す
		setBackgroundColor( Color.argb( 0, 0, 0, 0 ) );
		
		invalidate();
		
		return true;
	}
	
	private void updateLayout() {
		WindowManager wm = (WindowManager) service.getSystemService(Context.WINDOW_SERVICE);
		wm.updateViewLayout( this, params );
	}
	
	private int getGravity() {
		// Preferenceに対応
		SharedPreferences pref = service.getSharedPreferences( "FingerPaste", Activity.MODE_PRIVATE );
		String origin = pref.getString( "OriginOfSwipe", "null" );
		if( origin.equals( "left_top") ) {
			return Gravity.LEFT | Gravity.TOP;
		} else if( origin.equals( "right_top") ) {
			return Gravity.RIGHT | Gravity.TOP;
		} else if( origin.equals( "left_bottom") ) {
			return Gravity.LEFT | Gravity.BOTTOM;
		} else if( origin.equals( "right_bottom") ) {
			return Gravity.RIGHT | Gravity.BOTTOM;
		}
		return Gravity.RIGHT | Gravity.TOP;
	}

	private int getDetectHeight() {
		WindowManager wm = (WindowManager) service.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point point = new Point();
		display.getSize( point );  
		return point.y / 4;
	}

	private int getRadius() {
		WindowManager wm = (WindowManager) service.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point point = new Point();
		display.getSize( point );  
		return Math.min( point.x, point.y ) / 4 + 1;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key) {
		if(key.equals("OriginOfSwipe")){
			// スワイプ基点の設定変更された時
			params.gravity = getGravity();
			updateLayout();
		}
	}
}