package com.udacity.thomas.mymovieapp.API;

import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by thomas on 03/05/2018.
 */

public class RetrofitCall {

    //return retrofit object to can call from anywhere

    @NonNull
    public static Retrofit getMovie() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}

