package com.abhishekpanwar.youtubelivesubscribercount;


import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelData;
import com.abhishekpanwar.youtubelivesubscribercount.Services.MySubService;
import com.abhishekpanwar.youtubelivesubscribercount.Utilities.FavoritesDatabaseHandler;

import java.util.ArrayList;

public class MyAppWidgetConfigure extends AppCompatActivity {

    RecyclerViewAdaptorConfigure recyclerViewAdaptorConfigure;
    ArrayList<ChannelData> channelArrayListConfigure;
    RecyclerView favoritesList;
    public static Intent intentService;

    public  static  String widgetChannelID = "";
    public  static  String widgetChannelLogo = "";
    public  static  String widgetChannelName = "";


    public static int mAppWidgetId = 0 ;
    FavoritesDatabaseHandler db;

    TextView addFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_app_widget_configure);

        db = new FavoritesDatabaseHandler(this);

        addFav = (TextView)findViewById(R.id.NoDataTextConfigure);

        intentService  = new Intent(MyAppWidgetConfigure.this,MySubService.class);

        favoritesList = (RecyclerView) findViewById(R.id.channelListConfigure);
        favoritesList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdaptorConfigure = new RecyclerViewAdaptorConfigure(this,this);
        favoritesList.setAdapter(recyclerViewAdaptorConfigure);

        channelArrayListConfigure = new ArrayList<ChannelData>();
        channelArrayListConfigure = db.getAllChannels();

        recyclerViewAdaptorConfigure.channelList = db.getAllChannels();
        recyclerViewAdaptorConfigure.notifyDataSetChanged();

        if(recyclerViewAdaptorConfigure.channelList.size() == 0)
            addFav.setVisibility(View.VISIBLE);
        else
            addFav.setVisibility(View.INVISIBLE);




        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);


        }

    }

    @Override
    public void onBackPressed(){
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_CANCELED, resultValue);
        finish();

    }


}
