package com.tegarpenemuan.challengchapter6.data.api.auth

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {
    @POST("login")
    suspend fun login(@Body request: SignInRequest): Response<SignInResponse>

    @Multipart
    @POST("register")
    suspend fun register(
        @Part("name") name: RequestBody? = null,
        @Part("email") email: RequestBody? = null,
        @Part("job") job: RequestBody? = null,
        @Part("password") password: RequestBody? = null,
        @Part data: MultipartBody.Part? = null
    ): Response<SignUpResponse>

//    @FormUrlEncoded
//    @POST("login")
//    fun login(
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): Call<SignInResponse>

//    @Multipart
//    @POST("register")
//    fun registrasiUpload(
//        @Part("name") name: RequestBody,
//        @Part("email") email: RequestBody,
//        @Part("job") job: RequestBody,
//        @Part("password") password: RequestBody,
//        @Part data: MultipartBody.Part? = null
//    ): Call<SignUpResponse>


}

