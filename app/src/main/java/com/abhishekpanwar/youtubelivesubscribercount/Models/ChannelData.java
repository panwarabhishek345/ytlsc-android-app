package com.abhishekpanwar.youtubelivesubscribercount.Models;


import java.text.NumberFormat;

/**
 * Created by Abhishek Panwar on 7/27/2017.
 */

public class ChannelData {

    private String channelID;
    private String name;
    private String imageURL;
    private String subscriberCount = "Hidden";



    public ChannelData(){

    }

    public ChannelData(String channelID, String name, String imageURL) {
        this.channelID = channelID;
        this.name = name;
        this.imageURL = imageURL;
    }

    public ChannelData(String channelID, String name, String imageURL, String subscriberCount) {
        this.channelID = channelID;
        this.name = name;
        this.imageURL = imageURL;
        this.subscriberCount = subscriberCount;
    }

    public String getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(String subscriberCount) {

        try {
            this.subscriberCount = NumberFormat.getInstance().format(Integer.parseInt(subscriberCount)) + " subscribers";

        } catch (NumberFormatException e) {
            this.subscriberCount = "";
        }

    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImageURL() {

        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


}