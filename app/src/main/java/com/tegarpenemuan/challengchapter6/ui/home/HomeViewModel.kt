package com.tegarpenemuan.challengchapter6.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.model.ListGenreModel
import com.tegarpenemuan.challengchapter6.model.UserModel
import com.tegarpenemuan.challengchapter6.network.TMDBApiClient
import com.tegarpenemuan.challengchapter6.model.MoviePopulerModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
    private var db: MyDatabase? = null

    val shouldShowMoviePopuler: MutableLiveData<List<MoviePopulerModel>> = MutableLiveData()
    val shouldShowListGenre: MutableLiveData<List<ListGenreModel>> = MutableLiveData()
    val shouldShowUsername: MutableLiveData<String> = MutableLiveData()
    val shouldShowUser: MutableLiveData<UserModel> = MutableLiveData()
    val shouldShowUserError: MutableLiveData<String> = MutableLiveData()

    fun onViewLoaded(db: MyDatabase) {
        this.db = db
    }

    fun getMoviePopuler() {
        CoroutineScope(Dispatchers.IO).launch {
            val response =
                TMDBApiClient.instanceTMDB.getMoviePopuler("0fbaf8c27d542bc99bfc67fb877e3906")
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    val moviePopularResponse = response.body()
                    val moviePopularModels = moviePopularResponse?.results?.map {
                        MoviePopulerModel(
                            id = it.id ?: -1,
                            image = "https://image.tmdb.org/t/p/w500/" + it.poster_path.orEmpty(),
                            title = it.title.orEmpty(),
                            vote_average = it.vote_average,
                            overview = it.overview
                        )
                    } ?: listOf()
                    shouldShowMoviePopuler.postValue(moviePopularModels)
                }
            }
        }
    }

    fun getListMovie() {
        CoroutineScope(Dispatchers.IO).launch {
            val response =
                TMDBApiClient.instanceTMDB.getListGenre("0fbaf8c27d542bc99bfc67fb877e3906")
            withContext(Dispatchers.IO) {
                if (response.isSuccessful) {
                    val listGenreResponse = response.body()
                    val listGenreModels = listGenreResponse?.genres?.map {
                        ListGenreModel(
                            id = it.id,
                            name = it.name
                        )
                    } ?: listOf()
                    shouldShowListGenre.postValue(listGenreModels)
                }
            }
        }
    }

    fun getUser() {
        CoroutineScope(Dispatchers.IO).launch {
            val user = db?.userDAO()?.getUsername("tegarpenemuan@gmail.com")
            user?.let {
                shouldShowUsername.postValue("Welcome ${it.name} \uD83D\uDC4B")
            } ?: run {
                shouldShowUsername.postValue("Welcome Anonymous \uD83D\uDC4B")
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
}