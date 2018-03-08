package android.example.com.movieapiappp2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.example.com.movieapiappp2.MovieContract.MovieEntry;

/**
 * Created by Abbie on 22/02/2018.
 */

public class MovieDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "moviesDb.db";
    private static final int VERSION = 1;

    public MovieDBHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL PRIMARY KEY, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieEntry.COLUMN_AVG_VOTE + " TEXT, " +
                MovieEntry.COLUMN_DESCRIP + " TEXT, " +
                MovieEntry.COLUMN_REL_DATE + " TEXT, " +
                MovieEntry.COLUMN_TIMESTAMP + " DEFAULT CURRENT_TIMESTAMP NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}