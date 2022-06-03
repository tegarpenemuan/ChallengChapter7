package com.tegarpenemuan.challengchapter6.data.local.movie

import androidx.room.*

/**
 * com.tegarpenemuan.challengchapter6.data.local
 *
 * Created by Tegar Penemuan on 31/05/2022.
 * https://github.com/tegarpenemuanr3
 *
 */

@Dao
interface MovieDAO {
    @Insert
    suspend fun addMovieLocal(movieEntity: MovieEntity)

    @Delete
    suspend fun deleteMovieLocal(movieEntity: MovieEntity)

    @Query("SELECT * FROM movie")
    suspend fun getMovieLocal(): List<MovieEntity>

    @Query("SELECT * FROM movie WHERE id=:id LIMIT 1")
    fun getOneMovie(id: String): MovieEntity?

    @Query("DELETE FROM movie WHERE id = :id")
    fun deleteMovie(id: String): Int
}