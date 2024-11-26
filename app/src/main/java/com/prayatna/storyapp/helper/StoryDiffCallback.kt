package com.prayatna.storyapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.prayatna.storyapp.data.remote.response.ListStory

internal object StoryDiffCallback : DiffUtil.ItemCallback<ListStory>() {
    override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
        return oldItem == newItem
    }

}