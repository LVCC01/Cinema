package com.example.android.cinema;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.cinema.utilities.NetworkUtils;
import com.example.android.cinema.utilities.OpenMovieJsonUtils;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import static com.example.android.cinema.utilities.OpenMovieJsonUtils.movies;

/**
 * Created by hjadhav on 4/18/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<MovieProfileData> posterArray;
    public MovieProfileData movieProfileData ;
    int clickItemIndex;


    // variable for ListItemClickListener which can be referenced to Adapter , ViewHolder and MainActivity
    final private ListItemClickListener mOnClickListener;
    // interface for onClickListener
    public interface ListItemClickListener {
        void onListItemClick (int clickedItemIndex);
    }

    public MovieAdapter(ListItemClickListener listener){
        mOnClickListener = listener;
    }

    public MovieAdapter(ArrayList<MovieProfileData> posterData,ListItemClickListener listener ){
        posterArray = posterData;
        mOnClickListener = listener;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView listItemView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            listItemView = (ImageView) itemView.findViewById(R.id.iv_item_poster);

            // setOnClickListener on the View passed into the constructor (use 'this' as the OnClickListener)
            itemView.setOnClickListener(this);
        }

        public void bind(URL posterUrl){
            // added placeholder image and error image to PICASSO
            Picasso.with(context).load(posterUrl.toString()).placeholder(R.drawable.monroe).error(R.drawable.error_page).into(listItemView);
            Log.v(TAG,"Picasso point reached");
        }

        @Override
        public void onClick(View v) {
            clickItemIndex = getAdapterPosition();
            Log.v(TAG,"clickItemIndex: " + clickItemIndex);
            mOnClickListener.onListItemClick(clickItemIndex);

            Class destinationActivity = ChildActivity.class;

        }
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.poster_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem,viewGroup,false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);

        int height = viewGroup.getMeasuredHeight()/3;

        view.setMinimumHeight(height);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
            Log.v(TAG,"point-A reached");

        movieProfileData = movies.get(position);
        Log.v(TAG,"movieProfileData: "+ movieProfileData);
        Log.v(TAG,"movieProfileData.getPosterUrl(): "+movieProfileData.getPosterUrl()) ;
        Log.v(TAG,"movieProfileData.getTitleText(): "+movieProfileData.getTitleText()) ;

        URL posterUrl = NetworkUtils.buildPosterUrl(movieProfileData.getPosterUrl());
        Log.v(TAG,"posterUrl: "+posterUrl);
        holder.bind(posterUrl);
    }

    @Override
    public int getItemCount() {
        if (null == posterArray) return 0;
        return posterArray.size();
    }

    public void setWeatherData(ArrayList<MovieProfileData> data) {
        posterArray = data;
        notifyDataSetChanged();
    }
    public void swapArray() {

        try {
            posterArray.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
