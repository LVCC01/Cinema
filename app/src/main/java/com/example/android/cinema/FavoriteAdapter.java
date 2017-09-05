package com.example.android.cinema;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.cinema.data.FavoriteMovieListContract;

/**
 * Created by hjadhav on 8/21/2017.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private static final String TAG = FavoriteAdapter.class.getSimpleName();

    private Cursor mCursor;
    private Context mContext;

    public FavoriteAdapter(Context context,Cursor cursor){
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.favorite_list_item,parent,false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)){
            return;
        }
        String name = mCursor.getString(mCursor.getColumnIndex(FavoriteMovieListContract.FavoriteMovieListEntry.COLUMN_MOVIE_NAME));
        String id = mCursor.getString(mCursor.getColumnIndex(FavoriteMovieListContract.FavoriteMovieListEntry.COLUMN_MOVIE_ID));

        holder.nameTextView.setText(name);
        holder.idTextView.setText(id);
    }

    @Override
    public int getItemCount() {
        Log.v(TAG,"mCursor: "+mCursor.getCount());
        return mCursor.getCount();
    }

    public void swapCursor (Cursor newCursor){
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null){
            this.notifyDataSetChanged();
        }
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder  {

        TextView nameTextView;
        TextView idTextView;
        public FavoriteViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.movie_name);
            idTextView = (TextView) itemView.findViewById(R.id.movie_id);
        }

    }
}
