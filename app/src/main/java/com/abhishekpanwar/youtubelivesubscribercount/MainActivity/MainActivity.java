package com.abhishekpanwar.youtubelivesubscribercount.MainActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishekpanwar.youtubelivesubscribercount.BaseActivity;
import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelData;
import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelDataDetailed;
import com.abhishekpanwar.youtubelivesubscribercount.R;
import com.abhishekpanwar.youtubelivesubscribercount.Utilities.CircleTransform;
import com.abhishekpanwar.youtubelivesubscribercount.Utilities.NetworkChecker;
import com.robinhood.ticker.TickerUtils;
import com.robinhood.ticker.TickerView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainActivityMVP.View{

    @BindView(R.id.tickerView)
    TickerView tickerView;
    @BindView(R.id.displayVideoCount)
    TextView txtVideoCount;
    @BindView(R.id.displayViewCount)
    TextView txtViewCount;
    @BindView(R.id.tapReload)
    TextView txtTapReload;
    @BindView(R.id.channelName)
    TextView txtChannelName;
    @BindView(R.id.displayChannelImage)
    ImageView imgChannelLogo;
    @BindView(R.id.displayChannelArt)
    ImageView imgChannelArt;
    @BindView(R.id.contentLayout)
    RelativeLayout contentLayout;
    @BindView(R.id.parentLayout)
    RelativeLayout parentLayout;
    @BindView(R.id.simpleProgressBar2)
    ProgressBar simpleProgressBar;
    @BindView(R.id.gotoButton)
    LinearLayout gotoButton;
    @BindView(R.id.switchFavorite)
    Switch switchFavorite;

    private Handler mHandler;
    private String ChannelID = "";
    private String channelName = "";
    private String channelLogo = "";
    private String subscriberCount;
    private NetworkChecker networkChecker;
    private MainActivityPresenter mPresenter;
    private boolean dataLoaded = false;
    private Runnable mStatusChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.channel_live_data);
        ButterKnife.bind(this);

        mPresenter = new MainActivityPresenter();

        showContentLayout(false);

        ChannelID = getIntent().getStringExtra("ChannelID");
        channelName = getIntent().getStringExtra("ChannelName");
        this.setTitle(channelName);

        networkChecker = new NetworkChecker(this);

        Typeface bonkers = Typeface.createFromAsset(getAssets(), "Bonkers.otf");
        txtChannelName.setText(channelName);

        tickerView.setCharacterList(TickerUtils.getDefaultNumberList());
        tickerView.setAnimationInterpolator(new OvershootInterpolator());
        tickerView.setTypeface(bonkers);

        mHandler = new Handler();


        switchFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

              boolean alreadySaved = mPresenter.checkExists(ChannelID);
              if(isChecked && !alreadySaved){
                  mPresenter.addChannel(new ChannelData(ChannelID,channelName,channelLogo,subscriberCount));
                  showToastMessage("Channel added to Favorites");

              }
              else if(!isChecked && alreadySaved){
                  mPresenter.deleteChannel(new ChannelData(ChannelID,channelName,channelLogo,subscriberCount));
                  showToastMessage("Channel removed from Favorites");

              }

            }
        });

        gotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://m.youtube.com/channel/" + ChannelID;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                stopRepeatingTask();
                startActivity(i);
            }
        });

        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.setView(this,this,ChannelID);

        mStatusChecker = new Runnable() {
            @Override
            public void run() {
                try {
                    mPresenter.getData();
                } finally {
                    mHandler.postDelayed(mStatusChecker, 1600);
                }
            }
        };

        loadData();
    }

    private void loadData() {

        if(networkChecker.isNetworkConnected() && !dataLoaded){
            mPresenter.setChecked(ChannelID);
            mPresenter.getDataFirst();
            dataLoaded = true;
            startRepeatingTask();
        }
        else if (!networkChecker.isNetworkConnected()){
            showContentLayout(false);
            showSimpleProgressBar(false);
            showTapReloadText(true);
            showToastMessage("No Internet Connection!");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();

    }

    void startRepeatingTask() {
        mStatusChecker.run();
    }

    public void stopRepeatingTask() {
        dataLoaded = false;
        mHandler.removeCallbacks(mStatusChecker);
    }

    public void setSwitchFavorite(boolean checked){
        switchFavorite.setChecked(checked);
    }

    public void showSimpleProgressBar(boolean show){
        if(show)
            simpleProgressBar.setVisibility(View.VISIBLE);
        else
            simpleProgressBar.setVisibility(View.INVISIBLE);
    }

    public void showContentLayout(boolean show){
        if(show){
            contentLayout.setVisibility(View.VISIBLE);
            dataLoaded = true;
        }
        else{
            contentLayout.setVisibility(View.INVISIBLE);
            dataLoaded = false;
        }
    }

    @Override
    public void setData(ChannelDataDetailed channelData) {

        txtViewCount.setText(channelData.getChannelViews());
        txtVideoCount.setText(channelData.getChannelVideos());
        channelLogo = channelData.getChannelLogo();

        Picasso.with(MainActivity.this)
                .load(channelData.getChannelArt())
                .into(imgChannelArt);
        Picasso.with(MainActivity.this)
                .load(channelData.getChannelLogo())
                .transform(new CircleTransform())
                .into(imgChannelLogo);
    }

    public void setSubscriberCount(String subscriberCount) {
        this.subscriberCount  = subscriberCount;
        tickerView.setText(subscriberCount);
    }

    public void showToastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    public boolean isDataLoaded(){
        return dataLoaded;
    }

    public void showTapReloadText(boolean show){
        if(show)
            txtTapReload.setVisibility(View.VISIBLE);
        else
            txtTapReload.setVisibility(View.INVISIBLE);
    }
}
