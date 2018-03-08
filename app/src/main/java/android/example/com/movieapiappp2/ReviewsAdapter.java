package android.example.com.movieapiappp2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abbie on 22/02/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {
    private List<Movie> reviewList;
    private final Context mContext;
    private ReviewClickListener listener;

    public ReviewsAdapter(Context context, ReviewClickListener listener) {
        reviewList = new ArrayList<>();
        mContext = context;
        this.listener = listener;
    }

    public void setReviewsList(List<Movie> movieList) {
        this.reviewList.clear();
        this.reviewList.addAll(movieList);
        notifyDataSetChanged();
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Movie movie = reviewList.get(position);
        holder.reviewAuthor.setText(movie.getAuthor());
        holder.reviewContent.setText(movie.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView reviewContent;
        public TextView reviewAuthor;

        public ReviewViewHolder(View view) {
            super(view);
            reviewAuthor = view.findViewById(R.id.review_author);
            reviewContent = view.findViewById(R.id.review);
        }

        @Override
        public void onClick(View v) {
        }
    }

    public interface ReviewClickListener{
        void onReviewItemClick(Movie movie);
    }
}