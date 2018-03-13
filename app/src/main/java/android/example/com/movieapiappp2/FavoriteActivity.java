package android.example.com.movieapiappp2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

public class FavoriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, FavoritesAdapter.FavClickListener {
    private FavoritesAdapter adapter;
    RecyclerView favsRecyclerView;
    Context context;

    public static final String MOVIE_POSTER = "MOVIE POSTER: ";
    public static final String MOVIE_DESCRIP = "MOVIE DESCRIPTION:  ";
    public static final String MOVIE_REL_DATE = "MOVIE REL DATE: ";
    public static final String MOVIE_AVG_VOTE = "MOVIE AVG VOTE: ";
    public static final String MOVIE_TITLE = "MOVIE TITLE: ";
    public static final String MOVIE_ID = "MOVIE ID KEY: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        getSupportLoaderManager().initLoader(0, null, this);

        favsRecyclerView = findViewById(R.id.fav_rec_view);
        favsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new FavoritesAdapter(this, null, this);
        favsRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, null, null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


    @Override
    public void onFavItemClick(Movie movie) {
        String movieAvg_vote = movie.getAvgVote();
        String movie_description = movie.getDescription();
        String movie_relDate = movie.getRelDate();
        String movie_poster = movie.getPoster_path();
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
}