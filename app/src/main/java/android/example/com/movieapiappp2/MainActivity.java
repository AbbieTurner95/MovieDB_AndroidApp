package android.example.com.movieapiappp2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.example.com.movieapiappp2.MovieApi.MoviesApi;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MovieClickListener {

    public static final String MOVIE_POSTER = "MOVIE POSTER: ";
    public static final String MOVIE_DESCRIP = "MOVIE DESCRIPTION:  ";
    public static final String MOVIE_REL_DATE = "MOVIE REL DATE: ";
    public static final String MOVIE_AVG_VOTE = "MOVIE AVG VOTE: ";
    public static final String MOVIE_TITLE = "MOVIE TITLE: ";
    public static final String MOVIE_ID = "MOVIE ID KEY: ";
    public static final String API_KEY = "";

    private MoviesAdapter moviesAdapter;
    private MoviesApi service;
    private List<Movie> movies;

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isOnline()) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.no_connection_str)
                    .setCancelable(false)
                    .setMessage("You seem to have lost your connection, please connect and try again!")
                    .setIcon(R.drawable.no_internet)
                    .show();
        }

        layoutManager = new GridLayoutManager(this, 2);

        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(layoutManager);

        moviesAdapter = new MoviesAdapter(this, this);
        recyclerView.setAdapter(moviesAdapter);

        movies = new ArrayList<>();
        moviesAdapter.setMovieList(movies);

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

        service = restAdapter.create(MoviesApi.class);

        showPopularItems();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_popular:
                                showPopularItems();
                            case R.id.action_rated:
                                showTopRatedItems();
                        }
                        return true;
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

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager.setSpanCount(2);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager.setSpanCount(2);
        }
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onMovieItemClick(Movie movie) {
        String movieAvg_vote = movie.getAvgVote();
        String movie_description = movie.getDescription();
        String movie_relDate = movie.getRelDate();
        String movie_poster = movie.getPoster();
        String movie_title = movie.getTitle();
        String movie_id = movie.getID();

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(MOVIE_AVG_VOTE, movieAvg_vote);
        intent.putExtra(MOVIE_DESCRIP, movie_description);
        intent.putExtra(MOVIE_REL_DATE, movie_relDate);
        intent.putExtra(MOVIE_POSTER, movie_poster);
        intent.putExtra(MOVIE_TITLE, movie_title);
        intent.putExtra(MOVIE_ID, movie_id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.selection_fav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.favorites_selection:
                Intent intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showPopularItems() {
        service.getPopularMovies(new Callback<Movie.MovieResult>() {
            @Override
            public void success(Movie.MovieResult movieResult, Response response) {
                moviesAdapter.setMovieList(movieResult.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public void showTopRatedItems() {
        service.getTopRatedMovies(new Callback<Movie.MovieResult>() {
            @Override
            public void success(Movie.MovieResult movieResult, Response response) {
                moviesAdapter.setMovieList(movieResult.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}