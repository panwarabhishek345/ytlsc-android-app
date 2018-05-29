package com.abhishekpanwar.youtubelivesubscribercount.ChannelActivity;

import android.content.Context;

import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelData;

import java.util.ArrayList;


public interface ChannelActivityMVP {

    interface View{
        void updateData(ArrayList<ChannelData> channelArrayList);
        void showSearchResult(ArrayList<ChannelData> channelArrayList);
        void showNoDataText(boolean show);
        void showNoSearchResultText(boolean show);
        void showProgressBar(boolean show);
        void setMoreDataAvailable(boolean moreDataAvailable);
        void showErrorMessage(String message);

    }

    interface Presenter{
        void setView(ChannelActivityMVP.View channelActivityView, Context context);
        void getFavorites();
        void getChannels(String query,boolean loadMore);
        void resetSearch();

    }
}
