package com.example.android.cinema;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import static com.example.android.cinema.utilities.OpenTrailerJsonUtils.trailers;

/**
 * Created by hjadhav on 7/30/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    private int mTrailerNumberItems;
    private ArrayList<MovieProfileData> trailerArray;
    public MovieProfileData trailerProfileData ;
    Button listItemTrailerView;
    int clickItemIndex;

    // Create a final private ListItemTrailerClickListener called mOnClickListener
    final private ListItemTrailerClickListener mOnTrailerClickListener;

    public TrailerAdapter(int numberOfTrailers, ListItemTrailerClickListener mOnTrailerClickListener){
        mTrailerNumberItems = numberOfTrailers;
        this.mOnTrailerClickListener = mOnTrailerClickListener;
    }

    public TrailerAdapter(ListItemTrailerClickListener mOnTrailerClickListener){
        this.mOnTrailerClickListener = mOnTrailerClickListener;
    }

    // Add an interface called ListItemTrailerClickListener
    // Within that interface, define a void method called onListItemTrailerClick that takes an int as a parameter
    public interface ListItemTrailerClickListener {
        void onListItemTrailerClick (int clickedItemTrailerIndex);
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,viewGroup,shouldAttachToParentImmediately);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);



        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
//        trailerProfileData = trailers.get(position);
//        Log.v(TAG,"movieProfileData: "+ trailerProfileData);
//        Log.v(TAG,"Trailer Name: "+trailerProfileData.getNameText()) ;

        trailerProfileData = trailers.get(position);
        String trailerName = trailerProfileData.getNameText();
        Log.v(TAG, "trailerName: "+trailerName);
        holder.bind(trailerName);
        Log.v(TAG, "onBindViewHolder reached");
    }

    @Override
    public int getItemCount() {
//        return mTrailerNumberItems;

        if (null == trailerArray) return 0;
        return trailerArray.size();
    }

    public void setTrailerData(ArrayList<MovieProfileData> data) {
        trailerArray = data;
        notifyDataSetChanged();
    }


    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TrailerViewHolder(View itemView) {
            super(itemView);
            listItemTrailerView = (Button) itemView.findViewById(R.id.child_tv_trailer);
//            itemView.setOnClickListener(this);
            listItemTrailerView.setOnClickListener(this);
        }

       /* void bind(int listIndexTrailer){
            listItemTrailerView.setText(String.valueOf(listIndexTrailer));
        }*/
        void bind(String trailerName){
            Log.v(TAG,"TrailerAdapter bind method reached ");
            Log.v(TAG,"trailerName: "+trailerName);
            listItemTrailerView.setText(trailerName);

 /*           listItemTrailerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.v(TAG,"You clicked trailer: "+trailerProfileData.getNameText());
                }
            });*/
        }


        @Override
        public void onClick(View v) {
            Log.v(TAG,"Code is reaching onClick");
            int clickedPositionTrailer = getAdapterPosition();
            mOnTrailerClickListener.onListItemTrailerClick(clickedPositionTrailer);
            Log.v(TAG,"clickedPositionTrailer: "+clickedPositionTrailer);

            String trailerKey = trailerProfileData.getKeyText();
            Log.v(TAG,"KEY: "+ trailerKey);

            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + trailerKey));

        }
    }

    public void setWeatherData(ArrayList<MovieProfileData> data) {
        trailerArray = data;
        notifyDataSetChanged();
    }
    public void swapArray() {

        try {
            trailerArray.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
