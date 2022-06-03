package com.tegarpenemuan.challengchapter6.model

data class MoviePopulerModel(
    val id: Int,
    val image: String,
    val title: String,
    val vote_average: Double,
    val overview: String?
)