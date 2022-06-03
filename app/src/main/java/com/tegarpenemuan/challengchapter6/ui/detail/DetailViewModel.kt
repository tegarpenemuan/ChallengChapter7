package com.tegarpenemuan.challengchapter6.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.model.DetailModel
import com.tegarpenemuan.challengchapter6.network.TMDBApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel : ViewModel() {
    private var db: MyDatabase? = null

    val shouldShowDetailMovie: MutableLiveData<DetailModel> = MutableLiveData()
    val shouldShowDetailMovieError: MutableLiveData<String> = MutableLiveData()
    val shouldShowToggleButton: MutableLiveData<Boolean> = MutableLiveData()

    fun onViewLoaded(db: MyDatabase) {
        this.db = db
    }

    fun getMoviePopuler(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response =
                TMDBApiClient.instanceTMDB.getMovieDetails(id)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    val moviePopularResponse = response.body()
                    shouldShowDetailMovie.postValue(
                        DetailModel(
                            id = moviePopularResponse?.id.toString(),
                            backdrop_path = moviePopularResponse!!.poster_path,
                            overview = moviePopularResponse.overview,
                            original_title = moviePopularResponse.original_title
                        )
                    )
                } else {
                    shouldShowDetailMovieError.postValue("Data Error")
                }
            }
        }
    }

    fun getDataLocal(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val data = db?.movieDAO()?.getOneMovie(id)
            if (data !== null) {
                shouldShowToggleButton.postValue(true)
            } else {
                shouldShowToggleButton.postValue(false)
            }
        }
    }
}