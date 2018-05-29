package com.abhishekpanwar.youtubelivesubscribercount.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.abhishekpanwar.youtubelivesubscribercount.Services.MySubService;

/**
 * Created by Abhishek Panwar on 10/2/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isConnected() || mobile.isConnected()) {
            // Do something

            if(MySubService.mHandler != null)
            MySubService.subgenOn = true;
            Intent i = new Intent(context,MySubService.class);
            i.putExtra("NETWORK_ON",true);
            context.startService(i);

            Log.d("Network Available ", "Flag No 1");
        }
        else {

            if( MySubService.subgenOn){
                MySubService.subgenOn = false;
                MySubService.mHandler.removeCallbacksAndMessages(null);
            }
            Log.d("Network UnAvailable ", "Flag No 2");
        }

    }
}
