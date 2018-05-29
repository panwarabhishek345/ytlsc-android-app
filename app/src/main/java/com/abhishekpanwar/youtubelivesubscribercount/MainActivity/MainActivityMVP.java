package com.abhishekpanwar.youtubelivesubscribercount.MainActivity;

import android.content.Context;

import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelData;
import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelDataDetailed;

public interface MainActivityMVP {
    interface View{
        void setSwitchFavorite(boolean checked);
        void showSimpleProgressBar(boolean show);
        void setData(ChannelDataDetailed channelData);
        void showContentLayout(boolean show);
        void setSubscriberCount(String subscriberCount);
        void showToastMessage(String message);
        boolean isDataLoaded();
        void showTapReloadText(boolean show);
        void stopRepeatingTask();
    }

    interface Presenter{
        void setView(MainActivityMVP.View mainActivityView, Context context,String ChannelID);
        void addChannel(ChannelData channel);
        void deleteChannel(ChannelData channel);
        void getDataFirst();
        void getData();
        void setChecked(String ChannelID);
        boolean checkExists(String ChannelID);
    }
}
