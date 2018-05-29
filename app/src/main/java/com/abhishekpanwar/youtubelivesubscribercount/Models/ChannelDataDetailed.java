package com.abhishekpanwar.youtubelivesubscribercount.Models;

public class ChannelDataDetailed {
    private String ChannelViews;
    private String ChannelVideos;
    private String ChannelArt;

    public String getChannelViews() {
        return ChannelViews;
    }

    public void setChannelViews(String channelViews) {
        ChannelViews = channelViews;
    }

    public String getChannelVideos() {
        return ChannelVideos;
    }

    public void setChannelVideos(String channelVideos) {
        ChannelVideos = channelVideos;
    }

    public String getChannelArt() {
        return ChannelArt;
    }

    public void setChannelArt(String channelArt) {
        ChannelArt = channelArt;
    }

    public String getChannelLogo() {
        return ChannelLogo;
    }

    public void setChannelLogo(String channelLogo) {
        ChannelLogo = channelLogo;
    }

    public String getSubscriberCount() {
        return SubscriberCount;
    }

    public void setSubscriberCount(String subscriberCount) {
        SubscriberCount = subscriberCount;
    }

    private String ChannelLogo;
    private String SubscriberCount;
}
