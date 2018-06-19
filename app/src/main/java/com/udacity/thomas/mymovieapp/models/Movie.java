package com.udacity.thomas.mymovieapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by thomas on 22/03/2018.
 */

public class Movie {

    private int page;
    private int total_results;
    private int total_pages;
    private List<MovieDetail> results;


    public Movie(int page, int total_results, int total_pages, List<MovieDetail> results) {
        this.page = page;
        this.total_results = total_results;
        this.total_pages = total_pages;
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<MovieDetail> getResults() {
        return results;
    }

    public void setResults(List<MovieDetail> results) {
        this.results = results;
    }

    public static class MovieDetail implements Parcelable {

        private int vote_count;
        private int id;
        private boolean video;
        private double vote_average;
        private String title;
        private double popularity;
        private String poster_path;
        private String original_language;
        private String original_title;
        private String backdrop_path;
        private boolean adult;
        private String overview;
        private String release_date;
        private List<Integer> genre_ids;


        public MovieDetail(int vote_count, int id, boolean video, double vote_average, String title, double popularity, String poster_path, String original_language, String original_title, String backdrop_path, boolean adult, String overview, String release_date, List<Integer> genre_ids) {
            this.vote_count = vote_count;
            this.id = id;
            this.video = video;
            this.vote_average = vote_average;
            this.title = title;
            this.popularity = popularity;
            this.poster_path = poster_path;
            this.original_language = original_language;
            this.original_title = original_title;
            this.backdrop_path = backdrop_path;
            this.adult = adult;
            this.overview = overview;
            this.release_date = release_date;
            this.genre_ids = genre_ids;
        }

        protected MovieDetail(Parcel in) {
            vote_count = in.readInt();
            id = in.readInt();
            video = in.readByte() != 0;
            vote_average = in.readDouble();
            title = in.readString();
            popularity = in.readDouble();
            poster_path = in.readString();
            original_language = in.readString();
            original_title = in.readString();
            backdrop_path = in.readString();
            adult = in.readByte() != 0;
            overview = in.readString();
            release_date = in.readString();
        }

        public static final Creator<MovieDetail> CREATOR = new Creator<MovieDetail>() {
            @Override
            public MovieDetail createFromParcel(Parcel in) {
                return new MovieDetail(in);
            }

            @Override
            public MovieDetail[] newArray(int size) {
                return new MovieDetail[size];
            }
        };

        public int getVote_count() {
            return vote_count;
        }

        public void setVote_count(int vote_count) {
            this.vote_count = vote_count;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean isVideo() {
            return video;
        }

        public void setVideo(boolean video) {
            this.video = video;
        }

        public double getVote_average() {
            return vote_average;
        }

        public void setVote_average(double vote_average) {
            this.vote_average = vote_average;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getPopularity() {
            return popularity;
        }

        public void setPopularity(double popularity) {
            this.popularity = popularity;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public String getOriginal_language() {
            return original_language;
        }

        public void setOriginal_language(String original_language) {
            this.original_language = original_language;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }

        public boolean isAdult() {
            return adult;
        }

        public void setAdult(boolean adult) {
            this.adult = adult;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        public List<Integer> getGenre_ids() {
            return genre_ids;
        }

        public void setGenre_ids(List<Integer> genre_ids) {
            this.genre_ids = genre_ids;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(vote_count);
            parcel.writeInt(id);
            parcel.writeByte((byte) (video ? 1 : 0));
            parcel.writeDouble(vote_average);
            parcel.writeString(title);
            parcel.writeDouble(popularity);
            parcel.writeString(poster_path);
            parcel.writeString(original_language);
            parcel.writeString(original_title);
            parcel.writeString(backdrop_path);
            parcel.writeByte((byte) (adult ? 1 : 0));
            parcel.writeString(overview);
            parcel.writeString(release_date);
        }
    }
}
