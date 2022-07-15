package com.example.headwaytestapp.show_repos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView
import com.example.headwaytestapp.R
import com.example.headwaytestapp.dao.Repository
import com.example.headwaytestapp.databinding.RepositoryItemBinding


class RepositoryAdapter(private val onItemClicked: (Repository) -> Unit) :
    ListAdapter<Repository, ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.repository_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position), onItemClicked)

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Repository>() {
            override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean =
                oldItem == newItem
        }
    }
}

class ViewHolder constructor(private val binding: RepositoryItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Repository, onItemClicked: (Repository) -> Unit) {
        binding.apply {
            this.repositoryItem = item
        }
        binding.root.setOnClickListener {
            onItemClicked(item)
            binding.viewed.visibility = View.VISIBLE
        }
        binding.executePendingBindings()
    }
}
