package com.abhishekpanwar.youtubelivesubscribercount.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.abhishekpanwar.youtubelivesubscribercount.Receivers.ScreenOnOffReceiver;
import com.abhishekpanwar.youtubelivesubscribercount.SubscriberCount;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by anildeshpande on 17/07/16.
 */
public class MySubService extends Service {
    String ChannelID = "";

    Context context = this;
    public static boolean subgenOn = false;
    private String subscriberCount;
    private String channelLogo;
    private String channelName;


    String ClientID = "CLIENT ID";
    ScreenOnOffReceiver receiver;
    public static Handler mHandler;




    class MyServiceBinder extends Binder{
        public MySubService getService(){
            return MySubService.this;
        }
    }

    private IBinder mBinder=new MyServiceBinder();



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
            return mBinder;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Destroyed","Service Destroyed");
        stopSubGenerator();
        unregisterReceiver(receiver);
    }



    public  Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                getData();
            } finally {

                mHandler.postDelayed(mStatusChecker, 5000);
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent.getBooleanExtra("SCREEN_ON",false)||intent.getBooleanExtra("NETWORK_ON",false)){
            startSubGenerator();
        }
        else {
            receiver = new ScreenOnOffReceiver();
            registerReceiver(receiver,new IntentFilter(Intent.ACTION_SCREEN_ON));
            registerReceiver(receiver,new IntentFilter(Intent.ACTION_SCREEN_OFF));

            mHandler = new Handler();

          //  ClientID = context.getResources().getString(R.string.client_id);

            ChannelID = intent.getStringExtra("CHANNELID");
            channelLogo = intent.getStringExtra("CHANNELLOGO");
            channelName = intent.getStringExtra("CHANNELNAME");

            if(ChannelID != null)
            Log.d("SERVICE",ChannelID);

            subgenOn =true;
            startSubGenerator();
        }


        return START_REDELIVER_INTENT;
    }

    public void startSubGenerator(){
        if (subgenOn)
        mStatusChecker.run();
    }

    private void stopSubGenerator()
    {
        subgenOn  =false;
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    void getData(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String requestUrl ="https://www.googleapis.com/youtube/v3/channels?part=snippet%2Cstatistics&id=" + ChannelID +"&key=" + ClientID;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject fullResponse = new JSONObject(response);
                            JSONArray itemsArray = fullResponse.getJSONArray("items");
                            JSONObject itemsObject =itemsArray.getJSONObject(0);
                            JSONObject statistics = itemsObject.getJSONObject("statistics");
                            subscriberCount = (String) statistics.get("subscriberCount");

                            Intent i = new Intent(context, SubscriberCount.class);
                            i.setAction(SubscriberCount.ACTION_APPWIDGET_UPDATE);
                            i.putExtra("SubCount",subscriberCount);
                            i.putExtra("CHANNELLOGO",channelLogo);
                            i.putExtra("CHANNELNAME",channelName);

                            context.sendBroadcast(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("SUB COUNT",subscriberCount + "");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("SUB COUNT","That didnt work!");
            }
        });

        queue.add(stringRequest);
    }
}