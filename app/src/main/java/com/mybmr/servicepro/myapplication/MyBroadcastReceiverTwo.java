package com.mybmr.servicepro.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by vishal on 10/8/20.
 */

public class MyBroadcastReceiverTwo extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final Intent i = new Intent(context, InventoryBackgroundService.class);
        context.startService(i);

    }
}
