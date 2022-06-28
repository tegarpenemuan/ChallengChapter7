package com.tegarpenemuan.challengchapter6.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.tegarpenemuan.challengchapter6.Constant
import com.tegarpenemuan.challengchapter6.common.GetInisial.getInitial
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.databinding.FragmentHomeBinding
import com.tegarpenemuan.challengchapter6.datastore.DataStoreManager
import com.tegarpenemuan.challengchapter6.datastore.pref
import com.tegarpenemuan.challengchapter6.ui.detail.DetailActivity
import com.tegarpenemuan.challengchapter6.ui.home.adapter.ListGenreAdapter
import com.tegarpenemuan.challengchapter6.ui.home.adapter.MovieNowPlayingAdapter
import com.tegarpenemuan.challengchapter6.ui.home.adapter.MoviePopulerAdapter
import com.tegarpenemuan.challengchapter6.model.MoviePopulerModel
import com.tegarpenemuan.challengchapter6.network.AuthApiClient
import com.tegarpenemuan.challengchapter6.network.TMDBApiClient
import com.tegarpenemuan.challengchapter6.repository.AuthRepository
import com.tegarpenemuan.challengchapter6.repository.MovieRepository


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModel.Factory(
            MovieRepository(
                TMDBApiClient.instanceTMDB,
                MyDatabase.getInstance(requireContext())
            ),
            AuthRepository(
                DataStoreManager(requireContext()),
                AuthApiClient.instance,
                MyDatabase.getInstance(requireContext())
            )
        )
    }
    lateinit var moviePopulerAdapter: MoviePopulerAdapter
    lateinit var movieNowPlayingAdapter: MovieNowPlayingAdapter
    lateinit var listGenreAdapter: ListGenreAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        viewModel.getMoviePopuler()
        viewModel.getListMovie()

        bindView()
        bindViewModel()

        viewModel.getDataUser(requireActivity().pref().getPrefEmail().toString())
        return root
    }

    private fun bindView() {
        moviePopulerAdapter = MoviePopulerAdapter(
            listener = object : MoviePopulerAdapter.EventListener {
                override fun onClick(item: MoviePopulerModel) {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("id", item.id.toString())
                    startActivity(intent)
                }
            },
            list = emptyList()
        )
        binding.rvMoviePopuler.adapter = moviePopulerAdapter

        movieNowPlayingAdapter = MovieNowPlayingAdapter(
            listener = object : MovieNowPlayingAdapter.EventListener {
                override fun onClick(item: MoviePopulerModel) {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("id", item.id.toString())
                    startActivity(intent)
                }
            },
            list = emptyList()
        )
        binding.rvMovieNowPlaying.adapter = movieNowPlayingAdapter

        listGenreAdapter = ListGenreAdapter(emptyList())
        binding.rvListGenre.adapter = listGenreAdapter
    }

    private fun bindViewModel() {
        viewModel.shouldShowMoviePopuler.observe(requireActivity()) {
            moviePopulerAdapter.updateList(it)
        }

        viewModel.shouldShowMoviePopuler.observe(requireActivity()) {
            movieNowPlayingAdapter.updateList(it)
        }

        viewModel.shouldShowListGenre.observe(requireActivity()) {
            listGenreAdapter.updateList(it)
        }

        viewModel.shouldShowUsername.observe(viewLifecycleOwner) {
            binding.tvTitle.text = it.toString()
        }
        viewModel.shouldShowUser.observe(viewLifecycleOwner) {
            binding.tvTitle.text = "Welcome ${it.name} \uD83D\uDC4B"
            if (it.image == null) {
                binding.tvInisial.text = it.name.getInitial()
            } else {
                Glide.with(requireContext())
                    .load(Constant.ImageUrl.IMAGE_URL + it.image)
                    .into(binding.ivProfile)
            }
        }
        viewModel.shouldShowUserError.observe(viewLifecycleOwner) {
            binding.tvTitle.text = it.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}