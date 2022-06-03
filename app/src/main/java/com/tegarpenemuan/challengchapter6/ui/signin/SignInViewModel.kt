package com.tegarpenemuan.challengchapter6.ui.signin

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tegarpenemuan.challengchapter6.data.api.auth.SignInRequest
import com.tegarpenemuan.challengchapter6.data.local.UserEntity
import com.tegarpenemuan.challengchapter6.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * com.tegarpenemuan.challengchapter6.ui.signin
 *
 * Created by Tegar Penemuan on 03/06/2022.
 * https://github.com/tegarpenemuanr3
 *
 */

class SignInViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    private var email: String = ""
    private var password: String = ""

    val shouldShowError: MutableLiveData<String> = MutableLiveData()
    val shouldShowSuccess: MutableLiveData<String> = MutableLiveData()
    val shouldShowLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun onChangeEmail(email: String) {
        this.email = email
    }

    fun onChangePassword(password: String) {
        this.password = password
    }

    fun onClickSignIn() {
        if (email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            shouldShowError.postValue("Email tidak valid")
        } else if (password.isEmpty() && password.length < 8) {
            shouldShowError.postValue("Password tidak valid")
        } else {
            signInWithAPI()
        }
    }

    private fun signInWithAPI() {
        CoroutineScope(Dispatchers.IO).launch {
            shouldShowLoading.postValue(true)
            val request = SignInRequest(
                email = email,
                password = password
            )
            val response = authRepository.signIn(request)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val signInResponse = response.body()
                    signInResponse?.let {
                        shouldShowLoading.postValue(false)

                        authRepository.setPrefLogin(true)
                        authRepository.setPrefEmail(response.body()!!.user.email)

                        insertToLocal(
                            UserEntity(
                                id = response.body()!!.user.id.toString(),
                                name = response.body()!!.user.name,
                                job = response.body()!!.user.job,
                                email = response.body()!!.user.email,
                                image = response.body()!!.user.image
                            )
                        )
                    }
                } else {
                    shouldShowLoading.postValue(false)
                    shouldShowError.postValue("Request Tidak Failed")
                }
            }
        }
    }

    private fun insertToLocal(userEntity: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = authRepository.insertUser(userEntity)
            withContext(Dispatchers.Main) {
                if (user != 0L) {
                    shouldShowSuccess.postValue("Login Berhasil")
                } else {
                    shouldShowError.postValue("Login Gagal")
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: AuthRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
                return SignInViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown class name")
        }
    }
}