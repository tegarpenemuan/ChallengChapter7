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

    @Multipart
    @POST("update_profile/{id}")
    suspend fun updateProfile(
        @Path("id") id: String,
        @Part("name") name: RequestBody? = null,
        @Part("job") job: RequestBody? = null,
        @Part data: MultipartBody.Part? = null
    ): Response<UpdateProfileResponse>

    @POST("ganti_password/{id}")
    suspend fun gantiPassword(
        @Path("id") id: String,
        @Body request: GantiPasswordRequest
    ): Response<GantiPasswordResponse>
}

