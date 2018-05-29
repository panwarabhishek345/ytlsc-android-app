package com.abhishekpanwar.youtubelivesubscribercount.ChannelActivity;

import android.content.Context;
import android.util.Log;

import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelData;
import com.abhishekpanwar.youtubelivesubscribercount.R;
import com.abhishekpanwar.youtubelivesubscribercount.Utilities.SingletonRequestQueue;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChannelActivityRepository {

    private ArrayList<ChannelData> channelArrayList;
    private String ClientID;
    private Context context;
    private RequestQueue queue;

    ChannelActivityRepository(Context context){
        this.context = context;
    }

    public void fetchChannels(String query, final VolleyCallbackChannelActivity callback, String nextPage) {

        ClientID = context.getResources().getString(R.string.client_id);

        channelArrayList = new ArrayList<>();
        String npURL;
        if(nextPage.isEmpty())
            npURL = "";
        else
            npURL = "&pageToken=" + nextPage;

        String requestUrl ="https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20"+npURL+"&q="+query+"&type=channel&key=" + ClientID;

        queue = SingletonRequestQueue.getInstance(context).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject fullResponse = new JSONObject(response);
                            JSONArray itemsArray = fullResponse.getJSONArray("items");
                            String nextPageToken = fullResponse.getString("nextPageToken");

                            for (int i=0;i<itemsArray.length();i++){
                                JSONObject itemsObject =itemsArray.getJSONObject(i);
                                JSONObject snippet = itemsObject.getJSONObject("snippet");
                                String channelId = (String) snippet.get("channelId");
                                String channelTitle = (String) snippet.get("channelTitle");

                                JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                                JSONObject Default = thumbnails.getJSONObject("default");
                                String thumbnailURL = (String) Default.get("url");

                                ChannelData tmpChannelData = new ChannelData(channelId,channelTitle,thumbnailURL);
                                channelArrayList.add(tmpChannelData);
                            }
                            callback.onSuccess(channelArrayList,nextPageToken);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Error: " + "Not Found");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error",error.getMessage());
                callback.onError("Error occured: " + "No Network Connection");
            }
        });
        queue.add(stringRequest);

    }

    public void getSubscribers(final VolleyCallbackChannelActivity callback){

        String ids = "";

        for (int i=0;i<channelArrayList.size();i++){
            if(i== channelArrayList.size()-1){
                ids = ids + channelArrayList.get(i).getChannelID() + "%2C";
            }
            else{
                ids = ids + channelArrayList.get(i).getChannelID() + "%2C";
            }

        }

        String requestUrl1 ="https://www.googleapis.com/youtube/v3/channels?part=snippet%2Cstatistics&id="+ids+"&key=" + ClientID;
        StringRequest stringRequest1 = new StringRequest(Request.Method.GET, requestUrl1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject fullResponse = new JSONObject(response);
                            JSONArray itemsArray = fullResponse.getJSONArray("items");

                            for (int i=0;i<itemsArray.length();i++){
                                JSONObject itemsObject =itemsArray.getJSONObject(i);
                                String id = itemsObject.getString("id");
                                JSONObject statistics = itemsObject.getJSONObject("statistics");
                                if(!statistics.getBoolean("hiddenSubscriberCount")){
                                    String subscriberCount = (String) statistics.get("subscriberCount");
                                    for(int j=0;j<channelArrayList.size();j++){
                                        if(channelArrayList.get(j).getChannelID().equals(id))
                                            channelArrayList.get(j).setSubscriberCount(subscriberCount);
                                    }
                                }

                            }

                            callback.onSuccess(channelArrayList,"");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Error: " + "Not Found");
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError("Error occured: " + "No Network Connection");
            }
        });

        queue.add(stringRequest1);
    }



}
