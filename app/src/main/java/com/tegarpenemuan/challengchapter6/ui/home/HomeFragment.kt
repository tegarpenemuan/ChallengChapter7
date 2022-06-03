package com.tegarpenemuan.challengchapter6.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.tegarpenemuan.challengchapter6.common.GetInisial.getInitial
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.databinding.FragmentHomeBinding
import com.tegarpenemuan.challengchapter6.datastore.pref
import com.tegarpenemuan.challengchapter6.ui.detail.DetailActivity
import com.tegarpenemuan.challengchapter6.ui.home.adapter.ListGenreAdapter
import com.tegarpenemuan.challengchapter6.ui.home.adapter.MovieNowPlayingAdapter
import com.tegarpenemuan.challengchapter6.ui.home.adapter.MoviePopulerAdapter
import com.tegarpenemuan.challengchapter6.model.MoviePopulerModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    val viewModel: HomeViewModel by viewModels()
    lateinit var moviePopulerAdapter: MoviePopulerAdapter
    lateinit var movieNowPlayingAdapter: MovieNowPlayingAdapter
    lateinit var listGenreAdapter: ListGenreAdapter
    private var db: MyDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

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

        viewModel.getMoviePopuler()
        viewModel.getListMovie()
        viewModel.getUser()


        bindViewModel()

        val db = MyDatabase.getInstance(this.requireContext())
        viewModel.getDataUser(requireActivity().pref().getPrefEmail().toString())

        viewModel.onViewLoaded(db)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = MyDatabase.getInstance(requireContext().applicationContext)
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
            binding.tvTitle.text = it.name
            if (it.image == null) {
                binding.tvInisial.text = it.name.getInitial()
            } else {
                Glide.with(requireContext())
                    .load("https://tegarpenemuan.xyz/storage/user/" + it.image)
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