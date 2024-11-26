package com.prayatna.storyapp.ui.home

import androidx.lifecycle.ViewModel
import com.prayatna.storyapp.data.repository.StoryRepository

class HomeViewModel(private var repository: StoryRepository) : ViewModel() {
    fun getStories(location: String) = repository.getStories(location)
    fun getDetailStoryById(id: String) = repository.getDetailStoryById(id)
}