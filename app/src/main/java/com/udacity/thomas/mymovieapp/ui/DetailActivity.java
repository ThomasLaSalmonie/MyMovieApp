package com.udacity.thomas.mymovieapp.ui;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.udacity.thomas.mymovieapp.API.API;
import com.udacity.thomas.mymovieapp.API.Constants;
import com.udacity.thomas.mymovieapp.API.MovieAPI;
import com.udacity.thomas.mymovieapp.API.RetrofitCall;
import com.udacity.thomas.mymovieapp.BuildConfig;
import com.udacity.thomas.mymovieapp.R;
import com.udacity.thomas.mymovieapp.adapter.ReviewAdapter;
import com.udacity.thomas.mymovieapp.adapter.VideoAdapter;
import com.udacity.thomas.mymovieapp.data.MovieContract;
import com.udacity.thomas.mymovieapp.databinding.ActivityDetailBinding;
import com.udacity.thomas.mymovieapp.models.Movie;
import com.udacity.thomas.mymovieapp.models.MovieReview;
import com.udacity.thomas.mymovieapp.models.MovieVideo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity implements VideoAdapter.MovieVideoOnClickListener {

    //declare data binding variable
    private ActivityDetailBinding mDetailBinding;
    //store url that will be share
    private String url;

    //this variable to enable changing menu item icon
    private Menu menu;

    //make switch between two cases (Favourite | not Favourite)
    private boolean isFavourite;

    //retrieve data when app is online
    Movie.MovieDetail movieDetail;

    //adapters for two recycler view
    VideoAdapter videoAdapter;
    ReviewAdapter reviewAdapter;
    /*
    * retrieve data from cursor
    * */
    String ID;
    String title;
    String overview;
    String poster;
    String thumb;
    String rate;
    String releaseData;

    //couple of lists as a data source for videos and reviews adapter
    List<MovieVideo.MovieVideos> videos;
    List<MovieReview.MovieReviews> reviews;

    /*
    *
    * */
    LinearLayoutManager trailer_layout_manager;
    LinearLayoutManager reviews_layout_manager;

    //declare Key for recycler views states
    private final String trailersStateKey = "trailers_state";
    private final String reviewsStateKey = "reviews_state";
    //declare Parcelable to store recycler view state
    private Parcelable trailersStateParcelable = null;
    private Parcelable reviewsStateParcelable = null;
    //declare bundle to store recycler view state key & parcelable
    private static Bundle mBundleRecyclerViewState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        trailer_layout_manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reviews_layout_manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mDetailBinding.rvTrailers.setLayoutManager(trailer_layout_manager);
        mDetailBinding.rvReviews.setLayoutManager(reviews_layout_manager);


        //when details activty come from this  states (Top Rated | Most Popular)
        if (getIntent().getExtras().getParcelable("key") != null) {

            movieDetail = getIntent().getParcelableExtra("key");

            Picasso.with(this)
                    .load(Constants.BASE_POSTER_URL + movieDetail.getPoster_path())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(mDetailBinding.ivPosterDetail, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(DetailActivity.this)
                                    .load(Constants.BASE_POSTER_URL + movieDetail.getPoster_path())
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.error)
                                    .into(mDetailBinding.ivPosterDetail);
                        }
                    });

            setSupportActionBar(mDetailBinding.toolbarDetail);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(movieDetail.getTitle());
            }
            Picasso.with(this)
                    .load(Constants.BASE_POSTER_URL + movieDetail.getBackdrop_path())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(mDetailBinding.ivBackdoor, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(DetailActivity.this)
                                    .load(Constants.BASE_POSTER_URL + movieDetail.getBackdrop_path())
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.error)
                                    .into(mDetailBinding.ivBackdoor);
                        }
                    });

            mDetailBinding.tvReleaseData.setText(movieDetail.getRelease_date());
            mDetailBinding.tvUserRating.setText(String.valueOf(movieDetail.getVote_average()));
            mDetailBinding.tvOverview.setText(movieDetail.getOverview());
            ID = String.valueOf(movieDetail.getId());
            retrieveVideos();
            retrieveReviews();

        } else {
            //when detail activty come from favourite movies
            setDataFromCursor();
        }


    }


    //retrieve movie videos
    void retrieveVideos() {
        MovieAPI movieAPI = RetrofitCall.getMovie().create(MovieAPI.class);

        Call<MovieVideo> call = movieAPI.getMovieVideos(movieDetail.getId(), API.getAPIKey());
        call.enqueue(new Callback<MovieVideo>() {
            @Override
            public void onResponse(Call<MovieVideo> call, Response<MovieVideo> response) {
                videos = response.body().getResults();

                //get first movie url to share it
                url = Constants.YOUTUBE_URL + videos.get(0).getKey();
                //Toast.makeText(getApplicationContext(),videos.size()+"",Toast.LENGTH_LONG).show();
                videoAdapter = new VideoAdapter(DetailActivity.this, videos, DetailActivity.this);
                videoAdapter.notifyDataSetChanged();

                mDetailBinding.rvTrailers.setAdapter(videoAdapter);

            }

            @Override
            public void onFailure(Call<MovieVideo> call, Throwable t) {

            }
        });

    }

    //retrieve movie reviews
    void retrieveReviews() {
        MovieAPI movieAPI = RetrofitCall.getMovie().create(MovieAPI.class);
        Call<MovieReview> call = movieAPI.getMovieReview(movieDetail.getId(), API.getAPIKey());
        call.enqueue(new Callback<MovieReview>() {
            @Override
            public void onResponse(Call<MovieReview> call, Response<MovieReview> response) {
                reviews = response.body().getResults();
                reviewAdapter = new ReviewAdapter(reviews);
                reviewAdapter.notifyDataSetChanged();
                mDetailBinding.rvReviews.setAdapter(reviewAdapter);
            }

            @Override
            public void onFailure(Call<MovieReview> call, Throwable t) {

            }
        });
    }

    //retrieve movie from db
    void setDataFromCursor() {
        if (getIntent().getExtras() != null) {
            poster = Constants.BASE_POSTER_URL + getIntent().getExtras().getString("poster");

            Picasso.with(this)
                    .load(poster)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(mDetailBinding.ivPosterDetail, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(DetailActivity.this)
                                    .load(poster)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.error)
                                    .into(mDetailBinding.ivPosterDetail);
                        }
                    });

            title = getIntent().getExtras().getString("title");
            setSupportActionBar(mDetailBinding.toolbarDetail);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(title);
            }
            thumb = Constants.BASE_POSTER_URL + getIntent().getExtras().getString("thumb");

            Picasso.with(this)
                    .load(thumb)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(mDetailBinding.ivBackdoor, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(DetailActivity.this)
                                    .load(thumb)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.error)
                                    .into(mDetailBinding.ivBackdoor);
                        }
                    });

            releaseData = getIntent().getExtras().getString("releaseData");
            rate = getIntent().getExtras().getString("rate");
            overview = getIntent().getExtras().getString("overview");


            mDetailBinding.tvReleaseData.setText(releaseData);
            mDetailBinding.tvUserRating.setText(rate);
            mDetailBinding.tvOverview.setText(overview);

            isFavourite = true;

            ID = getIntent().getExtras().getString("id");
            mDetailBinding.textView3.setVisibility(View.GONE);
            mDetailBinding.textView4.setVisibility(View.GONE);
            mDetailBinding.rvReviews.setVisibility(View.GONE);
            mDetailBinding.rvTrailers.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        this.menu = menu;
        if (isFavourite()) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_24dp));
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border24dp));
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.share_action) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(Intent.createChooser(shareIntent, "Share link using"));
        } else if (item.getItemId() == R.id.favourite_action) {
            if (!isFavourite) {
                //add movie to favourite and change icon
                addMovieToFavourite();
                isFavourite = true;
                menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_24dp));
                Snackbar snackbar = Snackbar.make(mDetailBinding.coordinator, "Added To Favourite", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                removeMovieFromFavourite();
                                isFavourite = false;
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(DetailActivity.this, R.drawable.ic_favorite_border24dp));

                            }
                        });

                snackbar.show();

            } else {
                //remove movie from favourite and change icon

                removeMovieFromFavourite();
                isFavourite = false;
                menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border24dp));

                Snackbar snackbar = Snackbar.make(mDetailBinding.coordinator, "Remove From Favourite", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addMovieToFavourite();
                                isFavourite = true;
                                menu.getItem(0).setIcon(ContextCompat.getDrawable(DetailActivity.this, R.drawable.ic_favorite_24dp));
                            }
                        });
                snackbar.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    //add movie to favourite
    void addMovieToFavourite() {
        ContentValues cv = new ContentValues();

        if (movieDetail != null) {
            cv.put(MovieContract.MovieEntry._id, ID);
            cv.put(MovieContract.MovieEntry.TITLE_COLUMN, movieDetail.getTitle());
            cv.put(MovieContract.MovieEntry.RELEASE_DATA_COLUMN, movieDetail.getRelease_date());
            cv.put(MovieContract.MovieEntry.OVERVIEW_COLUMN, movieDetail.getOverview());
            cv.put(MovieContract.MovieEntry.RATING_COLUMN, movieDetail.getVote_average());
            cv.put(MovieContract.MovieEntry.THUMBNAIL_COLUMN, movieDetail.getBackdrop_path());
            cv.put(MovieContract.MovieEntry.POSTER_COLUMN, movieDetail.getPoster_path());
        } else {
            cv.put(MovieContract.MovieEntry._id, ID);
            cv.put(MovieContract.MovieEntry.TITLE_COLUMN, title);
            cv.put(MovieContract.MovieEntry.RELEASE_DATA_COLUMN, releaseData);
            cv.put(MovieContract.MovieEntry.OVERVIEW_COLUMN, overview);
            cv.put(MovieContract.MovieEntry.RATING_COLUMN, rate);
            cv.put(MovieContract.MovieEntry.THUMBNAIL_COLUMN, thumb);
            cv.put(MovieContract.MovieEntry.POSTER_COLUMN, poster);
        }
        Uri inserted = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);

    }

    //check if movie is favourite or not
    boolean isFavourite() {

        try (Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null,
                MovieContract.MovieEntry._id + " = " +
                        DatabaseUtils.sqlEscapeString(ID), null, null)) {
            if (cursor != null && cursor.getCount() != 0) {
                return true;
            }
            return false;
        }
    }

    //remove movie from favourite
    void removeMovieFromFavourite() {
        String selection = MovieContract.MovieEntry._id + " = " +
                DatabaseUtils.sqlEscapeString(ID);
        int result = getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, selection, null);

    }


    //handle movie video on click
    @Override
    public void onVideoClick(MovieVideo.MovieVideos movieVideos) {
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + movieVideos.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_URL + movieVideos.getKey()));

        //if youtube app not found video can open from browser
        try {
            startActivity(youtubeIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(webIntent);
        }

    }

    //handle orientation


    @Override
    protected void onPause() {
        super.onPause();
        mBundleRecyclerViewState = new Bundle();

        trailersStateParcelable = mDetailBinding.rvTrailers.getLayoutManager().onSaveInstanceState();
        reviewsStateParcelable = mDetailBinding.rvReviews.getLayoutManager().onSaveInstanceState();

        mBundleRecyclerViewState.putParcelable(trailersStateKey, trailersStateParcelable);
        mBundleRecyclerViewState.putParcelable(reviewsStateKey, reviewsStateParcelable);
    }

    //return recycler view state
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mBundleRecyclerViewState != null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    trailersStateParcelable = mBundleRecyclerViewState.getParcelable(trailersStateKey);
                    reviewsStateParcelable = mBundleRecyclerViewState.getParcelable(reviewsStateKey);
                    mDetailBinding.rvTrailers.getLayoutManager().onRestoreInstanceState(trailersStateParcelable);
                    mDetailBinding.rvReviews.getLayoutManager().onRestoreInstanceState(reviewsStateParcelable);

                }
            }, 50);
        }

        mDetailBinding.rvTrailers.setLayoutManager(trailer_layout_manager);
        mDetailBinding.rvReviews.setLayoutManager(reviews_layout_manager);

    }
}