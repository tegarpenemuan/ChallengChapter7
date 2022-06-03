package com.tegarpenemuan.challengchapter6.repository

import com.tegarpenemuan.challengchapter6.data.api.tmdb.TMDBAPI
import com.tegarpenemuan.challengchapter6.data.api.tmdb.listgenre.ListGenreResponse
import com.tegarpenemuan.challengchapter6.data.api.tmdb.moviepopuler.MoviePopulerResponse
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import retrofit2.Response

/**
 * com.tegarpenemuan.challengchapter6.repository
 *
 * Created by Tegar Penemuan on 03/06/2022.
 * https://github.com/tegarpenemuanr3
 *
 */

class MovieRepository(
    private val api: TMDBAPI,
    private val db: MyDatabase
) {
    suspend fun getMoviePopuler(key: String): Response<MoviePopulerResponse> {
        return api.getMoviePopuler(key)
    }

    suspend fun getListGenre(key: String): Response<ListGenreResponse> {
        return api.getListGenre(key)
    }
}