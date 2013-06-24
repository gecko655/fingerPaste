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
		// FLAG_ACTIVITY_NEW_TASKはサービスからアクティビティを呼び出すときに必ず必要
		// FLAG_ACTIVITY_NO_ANIMATIONは遷移のアニメーションがうざいから必要
		Intent intent = new Intent( this, GestureInputAndTransitionActivity.class );
		intent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION |Intent.FLAG_ACTIVITY_MULTIPLE_TASK );
		startActivity( intent );
	}
}
