package com.prayatna.storyapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.prayatna.storyapp.data.local.entity.StoryEntity
import com.prayatna.storyapp.data.remote.response.ListStory

internal object StoryDiffCallback : DiffUtil.ItemCallback<ListStory>() {
    override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
        return oldItem == newItem
    }
}

internal object LocalDiffCallback : DiffUtil.ItemCallback<StoryEntity>() {
    override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
        return oldItem == newItem
    }
}