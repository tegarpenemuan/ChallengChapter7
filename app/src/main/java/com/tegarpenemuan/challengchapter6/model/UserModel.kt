package com.tegarpenemuan.challengchapter6.model

/**
 * com.tegarpenemuan.challengchapter6.model
 *
 * Created by Tegar Penemuan on 31/05/2022.
 * https://github.com/tegarpenemuanr3
 *
 */

data class UserModel (
    val id: String,
    val name: String,
    val job: String,
    val email: String,
    val image: String? = null,
)