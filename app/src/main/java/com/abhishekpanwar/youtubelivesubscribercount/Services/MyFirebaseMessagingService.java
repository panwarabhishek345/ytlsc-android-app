package com.abhishekpanwar.youtubelivesubscribercount.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.abhishekpanwar.youtubelivesubscribercount.MainActivity.MainActivity;
import com.abhishekpanwar.youtubelivesubscribercount.R;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Abhishek Panwar on 7/29/2017.
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        //The message which i send will have keys named [message, image, AnotherActivity] and corresponding values.
        //You can change as per the requirement.

        //message will contain the Push Message
        String message = remoteMessage.getData().get("Message");
        //imageUri will contain URL of the image to be displayed with Notification
        String imageUri = remoteMessage.getData().get("Image");
        String ChannelID = remoteMessage.getData().get("ChannelID");
        String ChannelName = remoteMessage.getData().get("ChannelName");

        //If the key AnotherActivity has  value as True then when the user taps on notification, in the app AnotherActivity will be opened.
        //If the key AnotherActivity has  value as False then when the user taps on notification, in the app MainActivity will be opened.

        //To get a Bitmap image from the URL received
        bitmap = getBitmapfromUrl(imageUri);

        sendNotification(message, bitmap,ChannelID,ChannelName);

    }


    private void sendNotification(String messageBody, Bitmap image,String ChannelID, String ChannelName) {
    /*    Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("ChannelID",ChannelID);
        intent.putExtra("ChannelName", ChannelName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent, //0 is Request Code
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(image)//Notification icon image
                .setContentTitle(messageBody)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image))//Notification with Image
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());*/ /* ID of notification */



        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String formattedDate = df.format(c.getTime());

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
        RemoteViews contentViewHeadsup = new RemoteViews(getPackageName(), R.layout.headsup_notification_layout);

        //contentView.setImageViewResource(R.id.image_news, R.drawable.landscape);
        contentView.setImageViewBitmap(R.id.image_news,image);
        contentView.setTextViewText(R.id.tv_news_time, formattedDate);
        contentView.setTextViewText(R.id.tv_news_title, messageBody);

        contentViewHeadsup.setTextViewText(R.id.tv_title_heads_up,messageBody);
        contentViewHeadsup.setTextViewText(R.id.tv_time_heads_up,formattedDate);


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("ChannelID",ChannelID);
        intent.putExtra("ChannelName", ChannelName);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.logo_silhouette)
                .setCustomHeadsUpContentView(contentViewHeadsup)
                .setCustomBigContentView(contentView)
                .setContent(contentViewHeadsup)
                .setContentTitle("YTLSC")
                .setColor(Color.parseColor("#E01F13"))
                .setContentText(messageBody)
                .setSound(defaultSoundUri)
                .setContentIntent(contentIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }
}
