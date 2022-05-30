package com.tegarpenemuan.challengchapter6.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tegarpenemuan.challengchapter6.database.MyDatabase
import com.tegarpenemuan.challengchapter6.databinding.FragmentHomeBinding
import com.tegarpenemuan.challengchapter6.datastore.pref
import com.tegarpenemuan.challengchapter6.ui.home.adapter.ListGenreAdapter
import com.tegarpenemuan.challengchapter6.ui.home.adapter.MovieNowPlayingAdapter
import com.tegarpenemuan.challengchapter6.ui.home.adapter.MoviePopulerAdapter
import com.tegarpenemuan.challengchapter6.ui.profile.ProfileFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        moviePopulerAdapter = MoviePopulerAdapter(emptyList())
        binding.rvMoviePopuler.adapter = moviePopulerAdapter

        movieNowPlayingAdapter = MovieNowPlayingAdapter(emptyList())
        binding.rvMovieNowPlaying.adapter = movieNowPlayingAdapter

        listGenreAdapter = ListGenreAdapter(emptyList())
        binding.rvListGenre.adapter = listGenreAdapter

        viewModel.getMoviePopuler()
        viewModel.getListMovie()
        viewModel.getUser()

        bindViewModel()

        val db = MyDatabase.getInstance(this.requireContext())
        getUser(requireActivity().pref().getPrefEmail().toString())

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
    }

    fun getUser(email: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = db?.userDAO()?.getUsername(email = email)
            withContext(Dispatchers.Main) {
                if (user !== null ) {
                    val username = user.name
                    val image =  user.image

                    binding.tvTitle.text = username
                    Glide.with(requireContext())
                        .load("https://tegarpenemuan.xyz/storage/user/"+image)
                        .into(binding.ivProfile)
                } else {
                    binding.tvTitle.text = "Anonymous"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}