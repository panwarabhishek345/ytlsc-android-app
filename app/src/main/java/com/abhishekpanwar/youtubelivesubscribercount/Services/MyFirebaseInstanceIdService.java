package com.abhishekpanwar.youtubelivesubscribercount.Services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Abhishek Panwar on 8/5/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh(){
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN", token);
    }
}
