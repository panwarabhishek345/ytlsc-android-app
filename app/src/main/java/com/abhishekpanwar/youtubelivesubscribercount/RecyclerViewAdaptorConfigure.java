package com.abhishekpanwar.youtubelivesubscribercount;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelData;
import com.abhishekpanwar.youtubelivesubscribercount.Utilities.CircleTransform;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecyclerViewAdaptorConfigure extends RecyclerView.Adapter<RecyclerViewAdaptorConfigure.MyViewHolder> {

    Context context;
    ArrayList<ChannelData> channelList;
    int previousPosition = 0;
    Activity mActivity;




    public RecyclerViewAdaptorConfigure(Context context,final Activity mActivity) {
        this.context = context;
        this.channelList = new ArrayList<ChannelData>();
        this.mActivity=mActivity;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtChannelName, txtSubCount;
        public  ImageView imageView;
        Activity mActivity;


        public MyViewHolder(View view, final Activity mActivity) {
            super(view);
            this.mActivity=mActivity;
            txtChannelName = (TextView) view.findViewById(R.id.channelName);
            txtSubCount = (TextView) view.findViewById(R.id.txtSubCount);
            imageView = (ImageView) view.findViewById(R.id.channelLogo);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String channelID = channelList.get(getAdapterPosition()).getChannelID();
                    String channelLogo = channelList.get(getAdapterPosition()).getImageURL();
                    String channelName = channelList.get(getAdapterPosition()).getName();



                    MyAppWidgetConfigure.widgetChannelID = channelID;
                    MyAppWidgetConfigure.widgetChannelLogo = channelLogo;
                    MyAppWidgetConfigure.widgetChannelName = channelName;

                    MyAppWidgetConfigure.intentService.putExtra("CHANNELID",channelID);
                    MyAppWidgetConfigure.intentService.putExtra("CHANNELLOGO",channelLogo);
                    MyAppWidgetConfigure.intentService.putExtra("CHANNELNAME",channelName);

                    view.getContext().startService(MyAppWidgetConfigure.intentService);
                    Intent resultValue = new Intent();
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, MyAppWidgetConfigure.mAppWidgetId);
                    mActivity.setResult(mActivity.RESULT_OK, resultValue);
                    mActivity.finish();


                }
            });

        }


    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channel_list_item, parent, false);

        return new MyViewHolder(itemView,mActivity);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChannelData tmpChannelData = channelList.get(position);
        holder.txtChannelName.setText(tmpChannelData.getName());
        holder.txtSubCount.setText(tmpChannelData.getSubscriberCount());
        Picasso.with(context)
                .load(tmpChannelData.getImageURL())
                .transform(new CircleTransform())
                .into(holder.imageView);

        if(position>previousPosition ){
           // ChannelActivity.animate(holder,true);
        }
        else
        {
            //ChannelActivity.animate(holder,false);
        }

        previousPosition = position;

    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }
}
