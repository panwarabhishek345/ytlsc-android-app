package com.abhishekpanwar.youtubelivesubscribercount;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.abhishekpanwar.youtubelivesubscribercount.ChannelActivity.ChannelActivity;
import com.abhishekpanwar.youtubelivesubscribercount.MainActivity.MainActivity;
import com.abhishekpanwar.youtubelivesubscribercount.Services.MySubService;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * Implementation of App Widget functionality.
 */
public class SubscriberCount extends AppWidgetProvider {
    public  static  int counter = 0;

    public final static String ACTION_APPWIDGET_UPDATE = "android.appwidget.action.ACTION_DATA_UPDATED";
    public final static String APPWIDGET_CONFIGURE = "android.appwidget.action.APPWIDGET_CONFIGURE";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.subscriber_count);
        views.setTextViewText(R.id.appwidget_text, context.getResources().getString(R.string.app_name));
        views.setTextViewText(R.id.appwidget_text_name, context.getResources().getString(R.string.app_name));

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();

        if (ACTION_APPWIDGET_UPDATE.equals(action)) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.subscriber_count);
            String SubCount = intent.getStringExtra("SubCount");
            String channelLogo = intent.getStringExtra("CHANNELLOGO");
            String channelName = intent.getStringExtra("CHANNELNAME");


            int x = Integer.parseInt(SubCount);
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            SubCount = formatter.format(x);

            ImageView logo = new ImageView(context);
            Picasso.with(context)
                    .load(channelLogo)
                    .into(logo);

            Log.d("Call Index",counter++ + "");


            views.setTextViewText(R.id.appwidget_text, SubCount);
            views.setTextViewText(R.id.appwidget_text_name, channelName);

            if(logo.getDrawable() != null)
            views.setImageViewBitmap(R.id.channelLogoWidget,((BitmapDrawable) logo.getDrawable()).getBitmap());

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetId = appWidgetManager.getAppWidgetIds(new ComponentName(context, SubscriberCount.class));
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);


        }
        else if(APPWIDGET_CONFIGURE.equals(action)){

        }
        else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.subscriber_count);

            Intent openApp = new Intent(context,ChannelActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context,0,openApp,0);
            views.setOnClickPendingIntent(R.id.fullWidget,pIntent);

            appWidgetManager.updateAppWidget(appWidgetId,views);
        }



    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
        context.stopService(new Intent(context,MySubService.class));
        Log.d("NOTICE","Widget Disabled");
    }

    public void onDeleted(Context context){
        context.stopService(new Intent(context,MySubService.class));
        Log.d("NOTICE","Widget Deleted");
    }


}

