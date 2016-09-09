package com.smsreading.android.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by root on 7/9/16.
 */

public class SmsReciver extends BroadcastReceiver {
    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        Log.v("aaa bundle",bundle.toString());
    }
}
