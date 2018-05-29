package com.abhishekpanwar.youtubelivesubscribercount.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Abhishek Panwar on 2/23/2018.
 */

public class NetworkChecker {

    Context context;

    public NetworkChecker(Context context){
        this.context = context;

    }

    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm != null ? cm.getActiveNetworkInfo() : null) != null;
    }
}
