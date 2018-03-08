package android.example.com.movieapiappp2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailActivity extends AppCompatActivity implements ReviewsAdapter.ReviewClickListener {
    public static final String API_KEY = "";

    private MovieApi.MoviesApi service;
    private Context context;
    private ReviewsAdapter reviewsAdapter;

    private TextView film_review_title;
    private TextView faves_string;
    private ContentValues cv = new ContentValues();
    private Cursor c;
    private RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        recyclerView = findViewById(R.id.reviewRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        reviewsAdapter = new ReviewsAdapter(this, this);
        recyclerView.setAdapter(reviewsAdapter);
        List<Movie> reviews = new ArrayList<>();
        reviewsAdapter.setReviewsList(reviews);

        final ImageView movie_poster = findViewById(R.id.movie_poster);
        final TextView movie_title = findViewById(R.id.movie_title);
        TextView movie_rel_date = findViewById(R.id.movie_rel_date);
        TextView movie_descrip = findViewById(R.id.movie_description);
        final TextView movie_avg_vote = findViewById(R.id.movie_avg_vote);
        Button trailer = findViewById(R.id.trailer_content);
        film_review_title = findViewById(R.id.film_review_title);
        final Button fav_button = findViewById(R.id.favorites_button);
        faves_string = findViewById(R.id.faves_string);

        Intent intent = getIntent();
        final String movieTitle = intent.getStringExtra(MainActivity.MOVIE_TITLE);
        final String moviePoster = intent.getStringExtra(MainActivity.MOVIE_POSTER);
        final String movieRelDate = intent.getStringExtra(MainActivity.MOVIE_REL_DATE);
        final String movieDescrip = intent.getStringExtra(MainActivity.MOVIE_DESCRIP);
        final String movieAvgVote = intent.getStringExtra(MainActivity.MOVIE_AVG_VOTE);
        final String movieID = intent.getStringExtra(MainActivity.MOVIE_ID);

        Picasso.with(context).load(moviePoster).into(movie_poster);

        movie_title.setText(movieTitle);
        movie_rel_date.setText(movieRelDate);
        movie_descrip.setText(movieDescrip);
        movie_avg_vote.setText(movieAvgVote);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", API_KEY);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        service = restAdapter.create(MovieApi.MoviesApi.class);
        trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMovieTrailer(movieID);
            }
        });
        showMovieReviews(movieID);

        c = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " +
                        DatabaseUtils.sqlEscapeString(movieID), null, null);

        if(c.getCount() != 0){
            faves_string.setVisibility(View.GONE);
            fav_button.setVisibility(View.GONE);
        }

        fav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (c.getCount() == 0) {
                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movieID);
                    cv.put(MovieContract.MovieEntry.COLUMN_TITLE, movieTitle);
                    cv.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, moviePoster);
                    cv.put(MovieContract.MovieEntry.COLUMN_AVG_VOTE, movieAvgVote);
                    cv.put(MovieContract.MovieEntry.COLUMN_DESCRIP, movieDescrip);
                    cv.put(MovieContract.MovieEntry.COLUMN_REL_DATE, movieRelDate);
                    getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);

                    faves_string.setVisibility(View.GONE);
                    fav_button.setVisibility(View.GONE);
                    Toast.makeText(DetailActivity.this, R.string.movie_added_to_fav_strng,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();
        mListState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                    recyclerView.getLayoutManager().onRestoreInstanceState(mListState);
                }
            }, 50);
        }
        recyclerView.setLayoutManager(layoutManager);
    }


    public void showMovieTrailer(final String movieID) {
        service.getMovieTrailer(movieID, new Callback<Movie.MovieResult>() {
            @Override
            public void success(Movie.MovieResult movieResult, Response response) {
                List<Movie> results = movieResult.getResults();
                String key = results.get(0).getKey();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key)));
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void showMovieReviews(final String movieID){
        service.getMovieReviews(movieID, new Callback<Movie.MovieResult>() {
            @Override
            public void success(Movie.MovieResult movieResult, Response response) {
                List<Movie> reviews = movieResult.getResults();

                if(reviews.size() > 0){
                    reviewsAdapter.setReviewsList(movieResult.getResults());
                } else {
                    film_review_title.setText(R.string.no_reviews_string);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    @Override
    public void onReviewItemClick(Movie movie) {}
}
