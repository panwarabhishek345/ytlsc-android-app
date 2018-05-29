package com.abhishekpanwar.youtubelivesubscribercount.MainActivity;

import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelDataDetailed;

public interface VolleyCallbackMainActivity {
    void onSuccess(ChannelDataDetailed channelData, String subscriberCount);
    void onError(String errorMessage);
}
