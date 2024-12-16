package com.prayatna.storyapp.ui.maps

import androidx.lifecycle.ViewModel
import com.prayatna.storyapp.data.repository.UserRepository

class MapsViewModel(private val repository: UserRepository): ViewModel() {

    fun getStoriesLocation() = repository.getStoriesWithLocation()

}