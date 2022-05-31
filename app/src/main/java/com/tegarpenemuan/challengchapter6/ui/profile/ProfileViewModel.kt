package com.tegarpenemuan.challengchapter6.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.tegarpenemuan.challengchapter6.common.GetInisial.getInitial
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.model.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel : ViewModel() {
    private var db: MyDatabase? = null

    val shouldShowUser: MutableLiveData<UserModel> = MutableLiveData()
    val shouldShowUserError: MutableLiveData<String> = MutableLiveData()

    fun onViewLoaded(db: MyDatabase) {
        this.db = db
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
}