package com.udacity.thomas.mymovieapp;

import android.app.Application;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

/**
 * Created by thomas on 03/05/2018.
 */

public class MoviesApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        //--------offline capabilities for picasso
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso build = builder.build();
        //build.setIndicatorsEnabled(true);
        build.setLoggingEnabled(true);
        Picasso.setSingletonInstance(build);

    }
}
