package com.abhishekpanwar.youtubelivesubscribercount.MainActivity;

import android.content.Context;

import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelDataDetailed;
import com.abhishekpanwar.youtubelivesubscribercount.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivityRepository {
    private Context context;
    private String ClientID;
    private RequestQueue queue;

    public MainActivityRepository(Context context) {
        this.context = context;
    }

    void getDataFirst(String ChannelID, final VolleyCallbackMainActivity callback){

        ClientID = context.getResources().getString(R.string.client_id);

        queue = Volley.newRequestQueue(context);
        String requestUrl ="https://www.googleapis.com/youtube/v3/channels?part=snippet%2Cstatistics%2CbrandingSettings&id=" + ChannelID +"&key=" + ClientID;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject fullResponse = new JSONObject(response);
                            JSONArray itemsArray = fullResponse.getJSONArray("items");
                            JSONObject itemsObject =itemsArray.getJSONObject(0);
                            JSONObject snippet = itemsObject.getJSONObject("snippet");
                            JSONObject brandingSettings = itemsObject.getJSONObject("brandingSettings");
                            JSONObject statistics = itemsObject.getJSONObject("statistics");

                            ChannelDataDetailed channelData = new ChannelDataDetailed();

                            channelData.setChannelViews((String) statistics.get("viewCount"));
                            channelData.setChannelVideos((String) statistics.get("videoCount"));
                            JSONObject image = brandingSettings.getJSONObject("image");
                            channelData.setChannelArt((String) image.get("bannerMobileImageUrl"));

                            JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                            JSONObject Default = thumbnails.getJSONObject("default");
                            channelData.setChannelLogo((String) Default.get("url"));

                            callback.onSuccess(channelData,"");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Error occured: " + "No Network Connection");

            }
        });
        queue.add(stringRequest);

    }

    void getData(String ChannelID, final VolleyCallbackMainActivity callback){

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
                            callback.onSuccess(null,(String) statistics.get("subscriberCount"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Error occured: " + "No Network Connection");
            }
        });

        queue.add(stringRequest);
    }
}
