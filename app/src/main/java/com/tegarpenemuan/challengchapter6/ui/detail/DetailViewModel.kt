package com.tegarpenemuan.challengchapter6.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tegarpenemuan.challengchapter6.model.DetailModel
import com.tegarpenemuan.challengchapter6.network.TMDBApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailViewModel : ViewModel() {
    val shouldShowDetailMovie: MutableLiveData<DetailModel> = MutableLiveData()
    val shouldShowDetailMovieError: MutableLiveData<String> = MutableLiveData()

    fun getMoviePopuler(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val response =
                TMDBApiClient.instanceTMDB.getMovieDetails(id)
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    val moviePopularResponse = response.body()
                    shouldShowDetailMovie.postValue(
                        DetailModel(
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
}