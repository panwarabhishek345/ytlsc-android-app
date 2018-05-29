package com.abhishekpanwar.youtubelivesubscribercount.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.abhishekpanwar.youtubelivesubscribercount.Services.MySubService;

/**
 * Created by Abhishek Panwar on 8/19/2017.
 */

public class ScreenOnOffReceiver extends BroadcastReceiver {

    public final static String SCREEN_ON = "android.intent.action.SCREEN_ON";
    public final static String SCREEN_OFF = "android.intent.action.SCREEN_OFF";


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(SCREEN_ON)) {


            MySubService.subgenOn = true;
            Intent i = new Intent(context,MySubService.class);
            i.putExtra("SCREEN_ON",true);
            context.startService(i);
            Log.d("Message", "SCREEN ON");


        } else if (intent.getAction().equals(SCREEN_OFF)) {
            MySubService.subgenOn = false;
            MySubService.mHandler.removeCallbacksAndMessages(null);
            Log.d("Message", "SCREEN OFF");
        }

    }


}