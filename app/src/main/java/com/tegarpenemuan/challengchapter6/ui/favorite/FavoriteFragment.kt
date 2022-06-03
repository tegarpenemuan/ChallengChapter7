package com.tegarpenemuan.challengchapter6.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tegarpenemuan.challengchapter6.data.local.movie.MovieEntity
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.databinding.FragmentFavoriteBinding
import com.tegarpenemuan.challengchapter6.ui.detail.DetailActivity
import com.tegarpenemuan.challengchapter6.ui.favorite.adapter.MovieLocalAdapter

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    val viewModel: FavoriteViewModel by viewModels()
    lateinit var movieLocalAdapter: MovieLocalAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val db = MyDatabase.getInstance(this.requireContext())
        viewModel.onViewLoaded(db)

        viewModel.getDataUser()
        bindViewModel()

        movieLocalAdapter = MovieLocalAdapter(
            listener = object : MovieLocalAdapter.EventListener {
                override fun onClick(item: MovieEntity) {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("id", item.id.toString())
                    startActivity(intent)
                }
            },
            list = emptyList()
        )
        binding.rvMovieNowPlaying.adapter = movieLocalAdapter

        return root
    }

    private fun bindViewModel() {
        viewModel.shouldShowMovie.observe(viewLifecycleOwner) {
            movieLocalAdapter.updateList(it)
            if (it.isNotEmpty()) {
                binding.tvKosong.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}