package com.tegarpenemuan.challengchapter6.data.api.tmdb

import com.tegarpenemuan.challengchapter6.network.TMDBApiClient
import com.tegarpenemuan.challengchapter6.data.api.tmdb.detailmovie.DetailMovieResponse
import com.tegarpenemuan.challengchapter6.data.api.tmdb.listgenre.ListGenreResponse
import com.tegarpenemuan.challengchapter6.data.api.tmdb.moviepopuler.MoviePopulerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBAPI {
    @GET("movie/popular")
    suspend fun getMoviePopuler(
        @Query("api_key") apiKey: String = TMDBApiClient.APIKEY
    ): Response<MoviePopulerResponse>

    @GET("genre/movie/list")
    suspend fun getListGenre(
        @Query("api_key") apiKey: String = TMDBApiClient.APIKEY
    ): Response<ListGenreResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = TMDBApiClient.APIKEY
    ): Response<DetailMovieResponse>
}