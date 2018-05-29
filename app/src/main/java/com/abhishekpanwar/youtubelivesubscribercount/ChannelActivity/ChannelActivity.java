package com.abhishekpanwar.youtubelivesubscribercount.ChannelActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishekpanwar.youtubelivesubscribercount.About_Us;
import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelData;
import com.abhishekpanwar.youtubelivesubscribercount.R;
import com.abhishekpanwar.youtubelivesubscribercount.RecyclerViewAdaptor;
import com.abhishekpanwar.youtubelivesubscribercount.SettingsActivity.SettingsActivity;
import com.abhishekpanwar.youtubelivesubscribercount.Utilities.NetworkChecker;
import com.abhishekpanwar.youtubelivesubscribercount.Utilities.OnLoadMoreListener;
import com.abhishekpanwar.youtubelivesubscribercount.Utilities.Preferences.Prefs;
import com.google.firebase.messaging.FirebaseMessaging;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChannelActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ChannelActivityMVP.View {

    @BindView(R.id.channelList)
    RecyclerView channelList;
    @BindView(R.id.searchView)
    MaterialSearchView searchView;
    @BindView(R.id.simpleProgressBar)
    ProgressBar simpleProgressBar;
    @BindView(R.id.ContentText)
    TextView contentText;
    @BindView(R.id.NoDataText)
    TextView NoDataText;

    private ChannelActivityPresenter mPresenter;
    private RecyclerViewAdaptor recyclerViewAdaptor;
    private Boolean searchResult = false;
    private NetworkChecker networkChecker;
    private String queryGlobal;
    private String queryShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MultiDex.install(this);

        if(Prefs.getNightMode())
            setTheme(R.style.AppThemeNightNoActionBar);
        else
            setTheme(R.style.AppThemeNoActionBar);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        FirebaseMessaging.getInstance().subscribeToTopic("test");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mPresenter = new ChannelActivityPresenter();

        channelList.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdaptor = new RecyclerViewAdaptor(this,channelList);
        channelList.setAdapter(recyclerViewAdaptor);
        searchView.setCursorDrawable(R.drawable.search_cursor);
        networkChecker = new NetworkChecker(this);

        recyclerViewAdaptor.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (searchResult)
                mPresenter.getChannels(queryGlobal,true);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(networkChecker.isNetworkConnected()){

                    contentText.setText(String.format(getString(R.string.searchMessage),query));
                    searchResult = true;
                    channelList.setAdapter(null);
                    recyclerViewAdaptor.setSearchResult(true);
                    recyclerViewAdaptor.clearChannelList();
                    recyclerViewAdaptor.notifyDataChanged();
                    channelList.setAdapter(recyclerViewAdaptor);

                    queryShown = query;
                    query = query.replace(" ","+");
                    queryGlobal = query;
                    mPresenter.resetSearch();
                    mPresenter.getChannels(query,false);

                }
                else
                    Toast.makeText(getApplicationContext(),"No internet connection",Toast.LENGTH_LONG).show();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.setView(this,this);
        if(!searchResult)
        mPresenter.getFavorites();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(searchView.isSearchOpen()){
            searchView.closeSearch();
        }
        else if(searchResult){
            recyclerViewAdaptor.setSearchResult(false);
            showProgressBar(false);
            mPresenter.getFavorites();

        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_about_us) {
            Intent intent = new Intent(this,About_Us.class);
            startActivity(intent);

        }
        else if (id == R.id.nav_github) {
            String url = "https://github.com/panwarabhishek345/ytlsc-android-app";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);

        } else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "I just loved this app. Do check this out here https://goo.gl/W2X6aZ";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject here");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
            return true;

        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateData(ArrayList<ChannelData> favoritesArrayList){

        searchResult = false;
        contentText.setText(R.string.favorites);
        recyclerViewAdaptor.setSearchResult(false);
        recyclerViewAdaptor.setChannelList(favoritesArrayList);
        recyclerViewAdaptor.notifyDataChanged();

    }

    @Override
    public void setMoreDataAvailable(boolean moreDataAvailable) {
        recyclerViewAdaptor.setMoreDataAvailable(moreDataAvailable);
    }

    public void showSearchResult(ArrayList<ChannelData> searchArrayList){

        searchResult = true;
        recyclerViewAdaptor.setSearchResult(true);
        contentText.setText(String.format(getString(R.string.searchMessage),queryShown));
        recyclerViewAdaptor.addChannels(searchArrayList);
        recyclerViewAdaptor.notifyDataChanged();

    }

    public void showNoDataText(boolean show){
        NoDataText.setText("No Favorites");
        if(!show)
            NoDataText.setVisibility(View.INVISIBLE);
        else
            NoDataText.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoSearchResultText(boolean show) {
        recyclerViewAdaptor.setMoreDataAvailable(false);
        recyclerViewAdaptor.notifyDataChanged();
        NoDataText.setText("Nothing found.");
        if(!show)
            NoDataText.setVisibility(View.INVISIBLE);
        else
            NoDataText.setVisibility(View.VISIBLE);
    }

    public void showProgressBar(boolean show){
        if(!show)
            simpleProgressBar.setVisibility(View.INVISIBLE);
        else
            simpleProgressBar.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

}
