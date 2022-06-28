package com.tegarpenemuan.challengchapter6.data.api.auth

data class GantiPasswordResponse(
    val `data`: Data,
    val message: String
) {
    data class Data(
        val created_at: String,
        val email: String,
        val email_verified_at: Any,
        val id: Int,
        val image: String,
        val job: String,
        val name: String,
        val updated_at: String
    )
}