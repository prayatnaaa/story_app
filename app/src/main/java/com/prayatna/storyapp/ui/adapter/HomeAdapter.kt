package com.prayatna.storyapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prayatna.storyapp.data.remote.response.ListStory
import com.prayatna.storyapp.databinding.StoryItemBinding
import com.prayatna.storyapp.helper.StoryDiffCallback
import com.prayatna.storyapp.ui.user.home.DetailActivity

class HomeAdapter : PagingDataAdapter<ListStory, HomeAdapter.ViewHolder>(StoryDiffCallback) {
    class ViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStory) {
            binding.tvTitle.text = story.name
            binding.tvDescription.text = story.description
            Glide.with(binding.storyImage.context).load(story.photoUrl).into(binding.storyImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        if (story != null) {
            holder.bind(story)
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ID, story.id)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

}