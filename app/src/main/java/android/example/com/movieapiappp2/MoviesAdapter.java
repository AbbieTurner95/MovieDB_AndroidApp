package android.example.com.movieapiappp2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abbie on 16/02/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {
    private List<Movie> movieList;
    private final Context mContext;
    private final MovieClickListener listener;

    public MoviesAdapter(Context context, MovieClickListener listener) {
        movieList = new ArrayList<>();
        mContext = context;
        this.listener = listener;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList.clear();
        this.movieList.addAll(movieList);
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        Picasso.with(mContext)
                .load(movie.getPoster())
                .into(holder.moviePosterView);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView moviePosterView;

        public MovieViewHolder(View view){
            super(view);
            moviePosterView = view.findViewById(R.id.movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onMovieItemClick(movieList.get(getAdapterPosition()));
        }
    }

    public interface MovieClickListener{
        void onMovieItemClick(Movie movie);
    }
}