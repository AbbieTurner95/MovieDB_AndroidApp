package android.example.com.movieapiappp2;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Abbie on 26/02/2018.
 */

public class MovieContract {

    public static final String AUTHORITY = "android.example.com.movieapiappp2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movieId";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_AVG_VOTE = "avgVote";
        public static final String COLUMN_DESCRIP = "overview";
        public static final String COLUMN_REL_DATE = "relDate";

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES;

    }
}
