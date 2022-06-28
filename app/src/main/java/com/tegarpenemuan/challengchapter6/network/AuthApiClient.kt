package com.tegarpenemuan.challengchapter6.network

import com.tegarpenemuan.challengchapter6.data.api.auth.AuthApi
import com.tegarpenemuan.challengchapter6.data.api.tmdb.TMDBAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthApiClient {
    private const val BASE_URL = "https://tegarpenemuan.000webhostapp.com/api/"

    val instance: AuthApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(AuthApi::class.java)
    }
}