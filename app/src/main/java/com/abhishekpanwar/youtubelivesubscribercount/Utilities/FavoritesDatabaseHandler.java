package com.abhishekpanwar.youtubelivesubscribercount.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelData;

import java.util.ArrayList;

/**
 * Created by Abhishek Panwar on 8/4/2017.
 */

public class FavoritesDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "FavoriteChannelsManager";

    private static final String TABLE_CHANNELS = "channels";

    private static final String KEY_ID = "id";
    private static final String KEY_CHANNEL_ID = "ChannelID";
    private static final String KEY_CHANNEL_NAME = "ChannelName";
    private static final String KEY_CHANNEL_LOGO = "ChannelLogo";
    private static final String KEY_CHANNEL_SUBS = "ChannelSubs";



    public FavoritesDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CHANNELS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CHANNEL_ID + " TEXT," + KEY_CHANNEL_NAME + " TEXT," + KEY_CHANNEL_LOGO + " TEXT," + KEY_CHANNEL_SUBS + " TEXT" + ");";

        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHANNELS);

        // Create tables again
        onCreate(db);
    }

    public void addChannel(ChannelData channel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CHANNEL_ID, channel.getChannelID());
        values.put(KEY_CHANNEL_NAME, channel.getName());
        values.put(KEY_CHANNEL_LOGO, channel.getImageURL());
        values.put(KEY_CHANNEL_SUBS, channel.getSubscriberCount());

        db.insert(TABLE_CHANNELS, null, values);
        db.close();
    }


    public ArrayList<ChannelData> getAllChannels() {
        ArrayList<ChannelData> channelList = new ArrayList<ChannelData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CHANNELS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChannelData channel = new ChannelData();

                channel.setChannelID(cursor.getString(1));
                channel.setName(cursor.getString(2));
                channel.setImageURL(cursor.getString(3));
                channel.setSubscriberCount(cursor.getString(4));
                // Adding contact to list
                channelList.add(channel);
            } while (cursor.moveToNext());
        }

        cursor.close();
        // return contact list
        return channelList;
    }

    public void deleteChannel(ChannelData channel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CHANNELS, KEY_CHANNEL_ID + " = " + "'" + channel.getChannelID() + "'",null);
        db.close();
    }

    public  boolean exists(String tmpID) {

        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "Select * from " + TABLE_CHANNELS + " where " + KEY_CHANNEL_ID + " = " + "'" + tmpID + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
