package com.abhishekpanwar.youtubelivesubscribercount.MainActivity;

import android.content.Context;

import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelData;
import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelDataDetailed;
import com.abhishekpanwar.youtubelivesubscribercount.Utilities.FavoritesDatabaseHandler;

public class MainActivityPresenter implements MainActivityMVP.Presenter {

    private FavoritesDatabaseHandler db;
    private MainActivityMVP.View mMainActivityView;
    private MainActivityRepository mMainActivityRepository;
    private String ChannelID;
    private Context context;


    @Override
    public void setView(MainActivityMVP.View mainActivityView, Context context, String ChannelID) {
        mMainActivityView = mainActivityView;
        mMainActivityRepository = new MainActivityRepository(context);
        db = new FavoritesDatabaseHandler(context);
        mMainActivityView.setSwitchFavorite(db.exists(ChannelID));
        this.ChannelID = ChannelID;
        this.context = context;
    }

    @Override
    public void addChannel(ChannelData channel) {
        db.addChannel(channel);
    }

    @Override
    public void deleteChannel(ChannelData channel) {
        db.deleteChannel(channel);
    }

    @Override
    public void getDataFirst() {

        if (!mMainActivityView.isDataLoaded()){
            mMainActivityView.showSimpleProgressBar(true);
            mMainActivityView.showTapReloadText(false);
        }

        mMainActivityRepository.getDataFirst(ChannelID, new VolleyCallbackMainActivity() {
            @Override
            public void onSuccess(ChannelDataDetailed channelData, String subscriberCount) {
                mMainActivityView.setData(channelData);
                mMainActivityView.showSimpleProgressBar(false);
                mMainActivityView.showContentLayout(true);
                mMainActivityView.showTapReloadText(false);
            }

            @Override
            public void onError(String errorMessage) {
                onErrorOccurred(errorMessage);
            }
        });
    }

    @Override
    public void getData() {


        try {
            mMainActivityRepository.getData(ChannelID, new VolleyCallbackMainActivity() {
                @Override
                public void onSuccess(ChannelDataDetailed channelData, String subscriberCount) {
                    mMainActivityView.setSubscriberCount(subscriberCount);
                }

                @Override
                public void onError(String errorMessage) {
                    onErrorOccurred(errorMessage);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            getDataFirst();
        }
    }

    @Override
    public void setChecked(String ChannelID) {
        if (db.exists(ChannelID))
            mMainActivityView.setSwitchFavorite(true);
        else
            mMainActivityView.setSwitchFavorite(false);
    }

    public boolean checkExists(String ChannelID) {
        return db != null && ChannelID != null && db.exists(ChannelID);
    }

    private void onErrorOccurred(String errorMessage){
        mMainActivityView.showSimpleProgressBar(false);
        mMainActivityView.showToastMessage(errorMessage);
        mMainActivityView.stopRepeatingTask();
        mMainActivityView.showContentLayout(false);
        mMainActivityView.showTapReloadText(true);
    }

}
