package com.tkobayalab.fingerpaste;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FPService extends Service {
	public FPService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
