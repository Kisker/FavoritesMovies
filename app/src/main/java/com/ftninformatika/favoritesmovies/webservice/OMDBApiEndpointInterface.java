package com.ftninformatika.favoritesmovies.webservice;

import com.ftninformatika.favoritesmovies.model.Movie;
import com.ftninformatika.favoritesmovies.model.Result;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface OMDBApiEndpointInterface {

    @GET("/")
    Call<Result> getMoviesByTitle(@QueryMap Map<String, String> options);

    @GET("/")
    Call<Movie> getMoviesDetails(@QueryMap Map<String, String> options);
}
