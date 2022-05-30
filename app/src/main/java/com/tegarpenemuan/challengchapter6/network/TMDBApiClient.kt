package com.tegarpenemuan.challengchapter6.network

import com.tegarpenemuan.challengchapter6.data.api.tmdb.TMDBAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TMDBApiClient {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val APIKEY = "0fbaf8c27d542bc99bfc67fb877e3906"

    private val logging: HttpLoggingInterceptor
        get() {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            return httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            }
        }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val instanceTMDB: TMDBAPI by lazy {
        retrofit.create(TMDBAPI::class.java)
    }

}