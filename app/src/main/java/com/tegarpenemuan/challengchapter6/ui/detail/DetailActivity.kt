package com.tegarpenemuan.challengchapter6.ui.detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tegarpenemuan.challengchapter6.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val id = intent.getStringExtra("id")
        viewModel.getMoviePopuler(id!!.toInt())

//        binding.tvDescription.text = id.toString()
        bindViewModel()
    }

    private fun bindViewModel() {
        viewModel.shouldShowDetailMovie.observe(this) {
            Glide.with(applicationContext)
                .load("https://image.tmdb.org/t/p/w500/" + it.backdrop_path)
                .into(binding.ivCover)
            binding.tvTitle.text = it.original_title
            binding.tvOverview.text = it.overview
        }
    }
}