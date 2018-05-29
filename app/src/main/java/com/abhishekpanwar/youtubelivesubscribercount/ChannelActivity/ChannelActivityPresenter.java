package com.abhishekpanwar.youtubelivesubscribercount.ChannelActivity;

import android.content.Context;
import android.support.annotation.NonNull;

import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelData;
import com.abhishekpanwar.youtubelivesubscribercount.Utilities.FavoritesDatabaseHandler;

import java.util.ArrayList;

public class ChannelActivityPresenter implements ChannelActivityMVP.Presenter {

    private ChannelActivityMVP.View mChannelActivityView;
    private ChannelActivityRepository mChannelActivityRepository;
    private FavoritesDatabaseHandler db;
    private boolean searchClosed = false;
    private ArrayList<ChannelData> favoritesList;
    private String pageToken;

    @Override
    public void setView(@NonNull ChannelActivityMVP.View channelActivityView,@NonNull Context context) {
        mChannelActivityView = channelActivityView;
        db = new FavoritesDatabaseHandler(context);
        mChannelActivityRepository = new ChannelActivityRepository(context);

    }

    @Override
    public void getFavorites() {

        resetSearch();
        searchClosed = true;
        favoritesList = db.getAllChannels();
        mChannelActivityView.updateData(favoritesList);

        if (favoritesList.size()>0)
            mChannelActivityView.showNoDataText(false);
        else
            mChannelActivityView.showNoDataText(true);

    }

    @Override
    public void getChannels(final String query, final boolean loadMore) {

        //Case when the search is executed for the first time

        if (!loadMore){
            mChannelActivityView.showProgressBar(true);
        }

        mChannelActivityView.showNoDataText(false);
        searchClosed = false;

        mChannelActivityRepository.fetchChannels(query, new VolleyCallbackChannelActivity() {
            @Override
            public void onSuccess(ArrayList<ChannelData> channelArrayList,String nextPageToken) {
                if (channelArrayList.size()>0){
                    if (!searchClosed){
                        pageToken = nextPageToken;
                        mChannelActivityRepository.getSubscribers(new VolleyCallbackChannelActivity() {
                            @Override
                            public void onSuccess(ArrayList<ChannelData> channelArrayList,String redundant) {
                                mChannelActivityView.showProgressBar(false);
                                if (!searchClosed)
                                    mChannelActivityView.showSearchResult(channelArrayList);

                            }

                            @Override
                            public void onError(String errorMessage) {
                                mChannelActivityView.showProgressBar(false);
                                mChannelActivityView.showErrorMessage(errorMessage);
                                mChannelActivityView.setMoreDataAvailable(false);
                                if (!loadMore)
                                    mChannelActivityView.showNoSearchResultText(true);
                            }
                        });
                    }

                }
                else{
                    mChannelActivityView.showProgressBar(false);
                    if (!loadMore)
                        mChannelActivityView.showNoSearchResultText(true);
                    else
                        mChannelActivityView.setMoreDataAvailable(false);
                }
            }

            @Override
            public void onError(String errorMessage) {
                mChannelActivityView.showProgressBar(false);
                mChannelActivityView.showErrorMessage(errorMessage);
                mChannelActivityView.setMoreDataAvailable(false);
                if (!loadMore)
                mChannelActivityView.showNoSearchResultText(true);
            }
        },pageToken);

    }

    @Override
    public void resetSearch() {
        pageToken = "";
        mChannelActivityView.setMoreDataAvailable(true);
    }


}
