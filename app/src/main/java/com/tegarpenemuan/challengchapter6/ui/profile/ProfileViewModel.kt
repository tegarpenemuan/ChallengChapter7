package com.tegarpenemuan.challengchapter6.ui.profile

import android.net.Uri
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tegarpenemuan.challengchapter6.common.ConvertToMultipart.toMultipartBody
import com.tegarpenemuan.challengchapter6.common.GetInisial.getInitial
import com.tegarpenemuan.challengchapter6.data.api.auth.GantiPasswordRequest
import com.tegarpenemuan.challengchapter6.data.api.auth.SignUpRequest
import com.tegarpenemuan.challengchapter6.data.api.auth.UpdateProfileRequest
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.model.UserModel
import com.tegarpenemuan.challengchapter6.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.File

class ProfileViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    private var db: MyDatabase? = null
    private var username: String = ""
    private var job: String = ""
    private var id: String = ""
    private var passwordLama: String = ""
    private var passwordBaru: String = ""
    private var fileImage: File? = null

    val showResponseError: MutableLiveData<String> = MutableLiveData()
    val shouldShowUser: MutableLiveData<UserModel> = MutableLiveData()
    val shouldShowLoading: MutableLiveData<Boolean> = MutableLiveData()
    val shouldShowUserError: MutableLiveData<String> = MutableLiveData()
    val showResponseSuccess: MutableLiveData<String> = MutableLiveData()

    fun onViewLoaded(db: MyDatabase) {
        this.db = db
    }

    fun onChangeUsername(username: String) {
        this.username = username
    }

    fun onChangeJob(job: String) {
        this.job = job
    }

    fun onChangePasswordLama(passwordLama: String) {
        this.passwordLama = passwordLama
    }

    fun onChangePasswordBaru(passwordBaru: String) {
        this.passwordBaru = passwordBaru
    }

    fun getUriPath(uri: Uri) {
        fileImage = File(uri.path ?: "")
    }

    fun getId(Id: String) {
        id = Id
    }

    fun onValidate() {
        if (username.isEmpty()) {
            showResponseError.postValue("Nama tidak boleh kosong")
        } else if (job.isEmpty()) {
            showResponseError.postValue("Pekerjaan tidak boleh kosong")
        } else {
            updateProfile()
        }
    }

    fun GantiPassword() {
        if (passwordLama.isEmpty()) {
            showResponseError.postValue("Password Lama tidak boleh kosong")
        } else if (passwordBaru.isEmpty()) {
            showResponseError.postValue("Password Baru tidak boleh kosong")
        } else {
            updatePassword()
        }
    }

    private fun updatePassword() {
        CoroutineScope(Dispatchers.IO).launch {
            val updateProfile = repository.gantiPassword(
                id, GantiPasswordRequest(
                    password = passwordLama,
                    password_baru = passwordBaru
                )
            )
            withContext(Dispatchers.Main) {
                if (updateProfile.isSuccessful) {
                    shouldShowLoading.postValue(false)
                    showResponseSuccess.postValue("Password Berhasil Diubah")
                } else {
                    shouldShowLoading.postValue(false)
                    showResponseError.postValue("Password Gagal Diubah")
                }
            }
        }
    }

    private fun updateProfile() {
        shouldShowLoading.postValue(true)
        val file = fileImage.toMultipartBody("image")
        val name = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), username)
        val job = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), job)

        val request = UpdateProfileRequest(
            name = name,
            job = job,
            data = file,
        )

        CoroutineScope(Dispatchers.IO).launch {
            val updateProfile = repository.updateProfile(id, request)
            withContext(Dispatchers.Main) {
                if (updateProfile.isSuccessful) {
                    shouldShowLoading.postValue(false)
                    //showResponseSuccess.postValue("Update Profile Berhasil")
                    val nama = updateProfile.body()!!.data.name
                    val job = updateProfile.body()!!.data.job
                    val image = updateProfile.body()!!.data.image
                    updateDataUser(id, nama, job, image)
                } else {
                    shouldShowLoading.postValue(false)
                    showResponseError.postValue("Request Tidak Failed")
                }
            }
        }
    }

    fun updateDataUser(id: String, name: String, job: String, image: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = db?.userDAO()?.updateUser(id, name, job, image)
            withContext(Dispatchers.Main) {
                if (user !== null) {
                    showResponseSuccess.postValue("Update Profile Berhasil")
                } else {
                    shouldShowUserError.postValue("Anonymous")
                }
            }
        }
    }

    fun getDataUser(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = db?.userDAO()?.getUsername(email = email)
            withContext(Dispatchers.Main) {
                if (user !== null) {
                    shouldShowUser.postValue(
                        UserModel(
                            id = user.id,
                            name = user.name,
                            job = user.job,
                            email = user.email,
                            image = user.image
                        )
                    )
                } else {
                    shouldShowUserError.postValue("Anonymous")
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val repository: AuthRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown class name")
        }
    }

}