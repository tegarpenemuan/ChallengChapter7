package com.tegarpenemuan.challengchapter6.ui.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tegarpenemuan.challengchapter6.data.local.movie.MovieEntity
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.databinding.ActivityDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    val viewModel: DetailViewModel by viewModels()
    private var db: MyDatabase? = null

    private var id: String = ""
    private var title: String = ""
    private var overview: String = ""
    private var image: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = MyDatabase.getInstance(this)
        viewModel.onViewLoaded(db!!)

        val intent = intent
        val id = intent.getStringExtra("id")

        viewModel.getMoviePopuler(id!!.toInt())
        viewModel.getDataLocal(id!!)

        bindViewModel()
        bindView()
    }

    private fun bindView() {
        binding.toggleFavorite.setOnClickListener {
            if (binding.toggleFavorite.isChecked) {
                CoroutineScope(Dispatchers.IO).launch {
                    db?.movieDAO()?.addMovieLocal(
                        movieEntity = MovieEntity(
                            id = id,
                            title = title,
                            overview = overview,
                            image = image
                        )
                    )
                }
                Toast.makeText(applicationContext, "Ditambahkan ke favorit", Toast.LENGTH_SHORT)
                    .show()
            }

            if (!binding.toggleFavorite.isChecked) {
                CoroutineScope(Dispatchers.IO).launch {
                    val delete = db?.movieDAO()?.deleteMovie(id)
                    if (delete == 1) {
                        Toast.makeText(
                            applicationContext,
                            "Dihapus dari favorit",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    onBackPressed()
                }
            }
        }
//            binding.toggleFavorite.setOnCheckedChangeListener { _, isChecked ->
//                if (isChecked) {
//                    //Toast.makeText(applicationContext, "$isChecked", Toast.LENGTH_SHORT).show()
//
//                    CoroutineScope(Dispatchers.IO).launch {
//                        db?.movieDAO()?.addMovieLocal(
//                            movieEntity = MovieEntity(
//                                id = id,
//                                title = title,
//                                overview = overview,
//                                image = image
//                            )
//                        )
//                    }
//                    Toast.makeText(
//                        applicationContext,
//                        "Ditambahkan ke favorit",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } else {
//                    //Toast.makeText(applicationContext, "$isChecked", Toast.LENGTH_SHORT).show()
//                    CoroutineScope(Dispatchers.IO).launch {
//                        val delete = db?.movieDAO()?.deleteMovie(id)
//                        if (delete == 1) {
//                            Toast.makeText(
//                                applicationContext,
//                                "Dihapus dari favorit",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            onBackPressed()
//                        }
//                    }
//                }
//            }
    }

    private fun bindViewModel() {
        viewModel.shouldShowToggleButton.observe(this) {
            binding.toggleFavorite.isChecked = it
        }

        viewModel.shouldShowDetailMovie.observe(this) {
            id = it.id
            title = it.original_title
            overview = it.overview
            image = "https://image.tmdb.org/t/p/w500/" + it.backdrop_path

            Glide.with(applicationContext)
                .load(image)
                .into(binding.ivCover)
            binding.tvTitle.text = title
            binding.tvOverview.text = overview
        }
    }
}