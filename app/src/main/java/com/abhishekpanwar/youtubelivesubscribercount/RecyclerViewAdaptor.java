package com.abhishekpanwar.youtubelivesubscribercount;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishekpanwar.youtubelivesubscribercount.MainActivity.MainActivity;
import com.abhishekpanwar.youtubelivesubscribercount.Models.ChannelData;
import com.abhishekpanwar.youtubelivesubscribercount.Utilities.CircleTransform;
import com.abhishekpanwar.youtubelivesubscribercount.Utilities.OnLoadMoreListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecyclerViewAdaptor extends RecyclerView.Adapter {

    Context context;
    ArrayList<ChannelData> channelList;
    private OnLoadMoreListener onLoadMoreListener;

    private boolean loading = false;
    private int totalItemCount,visibleItemCount,firstVisibleItem,previousTotal,visibleThreshold = 3;
    private boolean isMoreDataAvailable = true;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private boolean searchResult = false;


    public RecyclerViewAdaptor(Context context, RecyclerView recyclerView) {

        this.context = context;
        this.channelList = new ArrayList<>();

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    visibleItemCount = recyclerView.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            previousTotal = totalItemCount;
                        }
                    }

                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {

            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.channel_list_item, parent, false);
            return new MyViewHolder(v);

        } else {

            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.loadmoreprogressbar, parent, false);
            return new ProgressViewHolder(v);

        }
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(position>=getItemCount()-1 && isMoreDataAvailable && !loading && onLoadMoreListener!=null){
            loading = true;
            onLoadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==VIEW_ITEM){
            ((MyViewHolder)holder).bindData(channelList.get(position));
        }

    }

    @Override
    public int getItemViewType(int position) {

        if(position + 1 == channelList.size() && searchResult && isMoreDataAvailable)
            return VIEW_PROG;
        else
            return VIEW_ITEM;

    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtChannelName, txtSubCount;
        public  ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            txtChannelName = view.findViewById(R.id.channelName);
            txtSubCount = view.findViewById(R.id.txtSubCount);
            imageView = view.findViewById(R.id.channelLogo);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!channelList.get(getAdapterPosition()).getSubscriberCount().equals("Hidden")){
                        String channelID = channelList.get(getAdapterPosition()).getChannelID();
                        String channelName = channelList.get(getAdapterPosition()).getName();
                        Intent intent = new Intent(view.getContext(),MainActivity.class);
                        intent.putExtra("ChannelID",channelID);
                        intent.putExtra("ChannelName",channelName);
                        view.getContext().startActivity(intent);
                    }
                    else{
                        float width = view.getWidth();
                        float one = (float) (width / 100.0);
                        AnimatorSet as = new AnimatorSet();
                        as.playTogether(ObjectAnimator.ofFloat(view, "translationX", 0 * one, -25 * one, 20 * one, -15 * one, 10 * one, -5 * one, 0 * one, 0),
                                ObjectAnimator.ofFloat(view, "rotation", 0, -5, 3, -3, 2, -1, 0));
                        as.setDuration(400);
                        as.start();
                        Toast.makeText(view.getContext(), "Subscriber Count hidden.", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }

        void bindData(ChannelData channelData){

            txtChannelName.setText(channelData.getName());
            txtSubCount.setText(channelData.getSubscriberCount());

            if (!channelData.getImageURL().isEmpty())
                Picasso.with(context)
                        .load(channelData.getImageURL())
                        .transform(new CircleTransform())
                        .into(imageView);
            else
                imageView.setImageResource(R.drawable.noprofilepic);

        }


    }


    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressViewHolder(View v) {
            super(v);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void notifyDataChanged(){
        notifyDataSetChanged();
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    public void addChannels(ArrayList<ChannelData> channelList) {
        this.channelList.addAll(channelList);
    }

    public void setChannelList(ArrayList<ChannelData> channelList) {
        this.channelList.clear();
        this.channelList = channelList;
    }

    public void clearChannelList() {
        this.channelList.clear();
    }

    public void setSearchResult(boolean isSearchResult){
        this.searchResult = isSearchResult;
    }

}
