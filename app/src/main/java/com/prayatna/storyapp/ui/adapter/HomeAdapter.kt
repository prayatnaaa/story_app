package com.prayatna.storyapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prayatna.storyapp.data.remote.response.ListStory
import com.prayatna.storyapp.databinding.StoryItemBinding
import com.prayatna.storyapp.helper.StoryDiffCallback

class HomeAdapter : ListAdapter<ListStory, HomeAdapter.ViewHolder>(StoryDiffCallback) {
    class ViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStory) {
            binding.tvTitle.text = story.name
            binding.tvDescription.text = story.description
            Glide.with(binding.storyImage.context).load(story.photoUrl!!).into(binding.storyImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story)
    }

}