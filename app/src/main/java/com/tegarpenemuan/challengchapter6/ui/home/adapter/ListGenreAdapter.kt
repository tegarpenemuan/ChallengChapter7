package com.tegarpenemuan.challengchapter6.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tegarpenemuan.challengchapter6.databinding.ListItemHomeCategoryBinding
import com.tegarpenemuan.challengchapter6.model.ListGenreModel

class ListGenreAdapter(private var list: List<ListGenreModel>) :
    RecyclerView.Adapter<ListGenreAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ListItemHomeCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun updateList(list: List<ListGenreModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ListItemHomeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.binding.tvName.text = item.name
    }

    override fun getItemCount(): Int {
        return list.size
    }

}