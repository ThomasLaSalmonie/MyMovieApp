package com.udacity.thomas.mymovieapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by thomas on 03/05/2018.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "movie.db";
    public static final int DB_VERSION = 2;

    public MovieDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQLITE_CREATE_DB_PETS = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "(" +
                MovieContract.MovieEntry._id + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                MovieContract.MovieEntry.TITLE_COLUMN + " TEXT NOT NULL ," +
                MovieContract.MovieEntry.RELEASE_DATA_COLUMN + " TEXT NOT NULL ," +
                MovieContract.MovieEntry.OVERVIEW_COLUMN + " TEXT NOT NULL ," +
                MovieContract.MovieEntry.RATING_COLUMN + " REAL NOT NULL ," +
                MovieContract.MovieEntry.THUMBNAIL_COLUMN + " TEXT NOT NULL ," +
                MovieContract.MovieEntry.POSTER_COLUMN + " TEXT NOT NULL"
                + ");";

        sqLiteDatabase.execSQL(SQLITE_CREATE_DB_PETS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

