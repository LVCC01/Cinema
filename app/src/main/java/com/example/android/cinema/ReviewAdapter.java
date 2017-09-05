package com.example.android.cinema;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.android.cinema.utilities.OpenReviewJsonUtils.reviews;

/**
 * Created by hjadhav on 8/8/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private static final String TAG = ReviewAdapter.class.getSimpleName();
    private ArrayList<MovieProfileData> reviewArray;
    public MovieProfileData reviewProfileData ;
    private int mReviewNumberItems;

    public ReviewAdapter (int numberOfReviews){
        mReviewNumberItems = numberOfReviews;
    }

    public ReviewAdapter (){}

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,viewGroup,shouldAttachToParentImmediately);
        ReviewAdapter.ReviewViewHolder viewHolder = new ReviewAdapter.ReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {

        reviewProfileData = reviews.get(position);
        Log.v(TAG,"reviewProfileData for onBindViewHolder: "+reviewProfileData);
        String reviewContent = reviewProfileData.getContentReviewText();
        Log.v(TAG,"reviewContent for onBindViewHolder: "+reviewContent);
        holder.bind(reviewContent);
        Log.v(TAG, "onBindViewHolder reached");
    }

    @Override
    public int getItemCount() {
        if (null == reviewArray) return 0;
        return reviewArray.size();
    }

    public void setReviewData(ArrayList<MovieProfileData> data) {
        reviewArray = data;
        notifyDataSetChanged();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView listItemReviewView;
        public ReviewViewHolder(View itemView) {
            super(itemView);
            listItemReviewView = (TextView) itemView.findViewById(R.id.child_tv_review);
        }
        void bind (String reviewContent){
            Log.v(TAG,"reviewContent: "+reviewContent);
            listItemReviewView.setText(reviewContent);
        }
    }
    public void setWeatherData(ArrayList<MovieProfileData> data) {
        reviewArray = data;
        notifyDataSetChanged();
    }

    public void swapArray() {

        try {
            reviewArray.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
