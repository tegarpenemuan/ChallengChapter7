package com.tegarpenemuan.challengchapter6.ui.signup

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tegarpenemuan.challengchapter6.common.ConvertToMultipart.toMultipartBody
import com.tegarpenemuan.challengchapter6.data.api.auth.SignUpRequest
import com.tegarpenemuan.challengchapter6.data.api.auth.SignUpResponse
import com.tegarpenemuan.challengchapter6.repository.AuthRepository
import com.tegarpenemuan.challengchapter6.ui.signin.SignInActivity
import com.tegarpenemuan.challengchapter6.ui.signin.SignInViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * com.tegarpenemuan.challengchapter6.ui.signup
 *
 * Created by Tegar Penemuan on 03/06/2022.
 * https://github.com/tegarpenemuanr3
 *
 */

class SignUpViewModel(
    private val repository: AuthRepository
): ViewModel() {

    private var name: String = ""
    private var job: String = ""
    private var email: String = ""
    private var password: String = ""
    private var fileImage: File? = null

    val showResponseError: MutableLiveData<String> = MutableLiveData()
    val shouldShowLoading: MutableLiveData<Boolean> = MutableLiveData()
    val showResponseSuccess: MutableLiveData<String> = MutableLiveData()

    fun onChangeName(name: String) {
        this.name = name
    }

    fun onChangeJob(job: String) {
        this.job = job
    }

    fun onChangeEmail(email: String) {
        this.email = email
    }

    fun onChangePassword(password: String) {
        this.password = password
    }

    fun getUriPath(uri: Uri) {
        fileImage = File(uri.path ?: "")
    }

    fun onValidate() {
        if (name.isEmpty() && name.length < 3) {
            showResponseError.postValue("Nama tidak valid")
        } else if (job.isEmpty() && name.length < 3) {
            showResponseError.postValue("Pekerjaan tidak valid")
        }  else if (email.isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showResponseError.postValue("Email tidak valid")
        } else if (password.isEmpty() && password.length < 8) {
            showResponseError.postValue("Password tidak valid")
        } else {
            SignUp()
        }
    }

    fun SignUp() {
        shouldShowLoading.postValue(true)
        val file = fileImage.toMultipartBody("image")
        val name = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), name)
        val email = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), email)
        val job = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), job)
        val password = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), password)

        val request = SignUpRequest(
            name = name,
            email = email,
            job = job,
            password = password,
            data = file,
        )
        CoroutineScope(Dispatchers.IO).launch {
            shouldShowLoading.postValue(true)
            val result = repository.signUpApi(request = request)
            withContext(Dispatchers.Main) {
                if (result.isSuccessful) {
                    shouldShowLoading.postValue(false)
                    showResponseSuccess.postValue("Register Berhasil")
                } else {
                    shouldShowLoading.postValue(false)
                    showResponseError.postValue("Request Tidak Failed")
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class FactorySignUp(
        private val repository: AuthRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                return SignUpViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown class name")
        }
    }
}