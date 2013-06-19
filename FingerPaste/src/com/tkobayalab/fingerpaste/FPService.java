package com.tkobayalab.fingerpaste;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.WindowManager;

public class FPService extends Service {
	private DetectView view;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		view = new DetectView( this );
		
		return START_STICKY;
	}
  
	@Override
	public void onDestroy() {
		super.onDestroy();
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);	
		if( wm != null && view != null ) {
			wm.removeView( view );
		}
	}
	
	public void onSwipe() {
		// FLAG_ACTIVITY_NEW_TASK�̓T�[�r�X����A�N�e�B�r�e�B���Ăяo���Ƃ��ɕK���K�v
		// FLAG_ACTIVITY_NO_ANIMATION�͑J�ڂ̃A�j���[�V����������������K�v
		Intent intent = new Intent( this, GestureInputAndTransitionActivity.class );
		intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION  );
		startActivity( intent );
	}
}
