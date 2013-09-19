package com.syedjafri.motionsensortests;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * BroadcastReceiver for enabling admin
 * 
 * @author Syed Jafri
 *
 */
public class AdminLockReceiver extends BroadcastReceiver {
	public AdminLockReceiver() {
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("AdminLockReceiver", "In AdminLockReceiver.onReceive()");
		
	}
}
