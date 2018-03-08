package android.example.com.movieapiappp2;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Abbie on 26/02/2018.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavsViewHolder> {
    private static final String TAG = FavoritesAdapter.class.getSimpleName();
    private Context mContext;
    private Cursor mCursor;
    private FavClickListener listener;

    public FavoritesAdapter(Context context, Cursor cursor, FavClickListener listener) {
        this.mContext = context;
        mCursor = cursor;
        this.listener = listener;
    }

    @Override
    public FavsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fav_film_item, parent, false);
        return new FavsViewHolder(view);
    }

    public void setData(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    private Movie getMovieFromCursor(Cursor cursor, int position) {
        boolean success = cursor.moveToPosition(position);
        Movie movie = null;

        if (success) {
            movie = new Movie();
            movie.setID(String.valueOf(cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID))));
            movie.setTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE)));
            movie.setAvgVote(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_AVG_VOTE)));
            movie.setDescription(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_DESCRIP)));
            movie.setRelDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_REL_DATE)));
            movie.setPoster(cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH)));
        }

        Log.d(TAG, "getMovieFromCursor: movie: " + movie);
        return movie;
    }

    @Override
    public void onBindViewHolder(FavsViewHolder holder, int position) {
        if (position>=0){
            mCursor.moveToPosition(position);

            String title = getString(mCursor, mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
            String poster = getString(mCursor, mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
            String date = getString(mCursor, mCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_REL_DATE));
            
            holder.favTitle.setText(title);
            holder.dateView.setText(date);
            Picasso.with(mContext)
                    .load(poster)
                    .into(holder.favPoster);
        }
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public class FavsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        ImageView favPoster;
        TextView favTitle;
        TextView dateView;

        public FavsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            favPoster = itemView.findViewById(R.id.fav_poster);
            favTitle = itemView.findViewById(R.id.fav_title);
            dateView = itemView.findViewById(R.id.date_view);

        }

        public void onClick(View v) {
            listener.onFavItemClick(getMovieFromCursor(mCursor, getAdapterPosition()));
        }
    }

    public interface FavClickListener{
        void onFavItemClick(Movie position);
    }


    public String getString(Cursor cursor, int ci) {
        String value = null;

        try {
            if (!cursor.isNull(ci)) {
                value = cursor.getString(ci);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}