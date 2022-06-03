package com.tegarpenemuan.challengchapter6.ui.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tegarpenemuan.challengchapter6.data.local.MovieEntity
import com.tegarpenemuan.challengchapter6.databinding.ListItemMovieNowPlayingBinding
import com.tegarpenemuan.challengchapter6.model.MovieModel

class MovieLocalAdapter(private val listener: EventListener, private var list: List<MovieEntity>) :
    RecyclerView.Adapter<MovieLocalAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ListItemMovieNowPlayingBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun updateList(list: List<MovieEntity>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemMovieNowPlayingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        Glide.with(holder.binding.root.context)
            .load(item.image)
            .into(holder.binding.ivPoster)
        holder.binding.tvTitleMovie.text = item.title
        holder.binding.tvOverview.text = item.overview

        holder.itemView.setOnClickListener {
            listener.onClick(item)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface EventListener {
        fun onClick(item: MovieEntity)
    }
}