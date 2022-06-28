package com.tegarpenemuan.challengchapter6.data.api.auth

import com.google.gson.annotations.SerializedName

/**
 * com.tegarpenemuan.challengchapter6.data.api.auth
 *
 * Created by Tegar Penemuan on 31/05/2022.
 * https://github.com/tegarpenemuanr3
 *
 */

data class GantiPasswordRequest(
    @SerializedName("password") var password: String? = null,
    @SerializedName("password_baru") var password_baru: String? = null
)