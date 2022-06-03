package com.tegarpenemuan.challengchapter6.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tegarpenemuan.challengchapter6.data.local.movie.MovieEntity
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel : ViewModel() {
    private var db: MyDatabase? = null

    val shouldShowMovie: MutableLiveData<List<MovieEntity>> = MutableLiveData()
    val shouldShowMovieError: MutableLiveData<String> = MutableLiveData()

    fun onViewLoaded(db: MyDatabase) {
        this.db = db
    }

    fun getDataUser() {
        CoroutineScope(Dispatchers.IO).launch {
            val movie = db?.movieDAO()?.getMovieLocal()
            withContext(Dispatchers.Main) {
                if (movie !== null) {
                    shouldShowMovie.postValue(movie!!)
                } else {
                    shouldShowMovieError.postValue("Gagal Menyimpan")
                }
            }
        }
    }
}