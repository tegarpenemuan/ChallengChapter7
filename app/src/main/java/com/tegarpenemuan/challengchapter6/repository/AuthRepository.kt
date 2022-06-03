package com.tegarpenemuan.challengchapter6.repository

import com.tegarpenemuan.challengchapter6.data.api.auth.*
import com.tegarpenemuan.challengchapter6.data.local.user.UserEntity
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.datastore.DataStoreManager
import retrofit2.Response

/**
 * com.tegarpenemuan.challengchapter6.repository
 *
 * Created by Tegar Penemuan on 31/05/2022.
 * https://github.com/tegarpenemuanr3
 *
 */

class AuthRepository(
    private val dataStoreManager: DataStoreManager,
    private val api: AuthApi,
    private val db: MyDatabase,
) {

    suspend fun signUpApi(request: SignUpRequest): Response<SignUpResponse> {
        return api.register(
            name = request.name,
            email = request.email,
            job = request.job,
            password = request.password,
            data = request.data
        )
    }

    suspend fun signIn(request: SignInRequest): Response<SignInResponse> {
        return api.login(request)
    }

    //    suspend fun setUsername(value: String) {
//        dataStoreManager.setPrefUsername(value)
//    }
//
//    suspend fun getUsername(): String? {
//        return dataStoreManager.getPrefUsername()
//    }
//
//    suspend fun setPassword(value: String) {
//        dataStoreManager.setPrefPassword(value)
//    }
//
//    suspend fun getPassword(): String? {
//        return dataStoreManager.getPrefPassword()
//    }
//
    fun setPrefLogin(value: Boolean) {
        dataStoreManager.setPrefLogin(value)
    }

    fun setPrefEmail(value: String) {
        dataStoreManager.setPrefEmail(value)
    }

    fun insertUser(userEntity: UserEntity): Long {
        return db.userDAO().insertUser(userEntity)
    }

    fun getUsername(email: String): UserEntity? {
        return db.userDAO().getUsername(email)
    }

//    suspend fun signIn(request: SignInRequest): Response<SignInResponse> {
//        return api.login(request)
//    }
}