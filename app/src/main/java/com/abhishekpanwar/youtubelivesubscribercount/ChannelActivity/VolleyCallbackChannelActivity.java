package com.abhishekpanwar.youtubelivesubscribercount.ChannelActivity;

import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelData;

import java.util.ArrayList;

public interface VolleyCallbackChannelActivity {
    void onSuccess(ArrayList<ChannelData> channelArrayList,String nextPageToken);
    void onError(String errorMessage);
}
