package com.udacity.thomas.mymovieapp.API;

import com.udacity.thomas.mymovieapp.models.Movie;
import com.udacity.thomas.mymovieapp.models.MovieReview;
import com.udacity.thomas.mymovieapp.models.MovieVideo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by thomas on 03/05/2018.
 */

public interface MovieAPI {


    //return top rated json and access it use model
    @GET("top_rated")
    Call<Movie> getTopRatedMovie(@Query("api_key") String API);

    //return most popular json and access it use model


    @GET("popular")
    Call<Movie> getPopularMovie(@Query("api_key") String API);

    //return  movie reviews json and access it use model

    @GET("{id}/reviews")
    Call<MovieReview> getMovieReview(@Path("id") int id, @Query("api_key") String API);

    //return movie Videos json and access it use model

    @GET("{id}/videos")
    Call<MovieVideo> getMovieVideos(@Path("id") int id, @Query("api_key") String API);
}

