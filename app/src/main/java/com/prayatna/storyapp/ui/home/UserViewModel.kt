package com.prayatna.storyapp.ui.home

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.storyapp.data.remote.response.AddResponse
import com.prayatna.storyapp.data.repository.AuthRepository
import com.prayatna.storyapp.helper.Result
import kotlinx.coroutines.launch
import java.io.File

class UserViewModel(private var repository: AuthRepository) : ViewModel() {
    private var _story = MutableLiveData<Result<AddResponse>>()
    val story: MutableLiveData<Result<AddResponse>> = _story

    var currentImageUri: Uri? = null

    fun getStories(location: String) = repository.getStories(location)
    fun getDetailStoryById(id: String) = repository.getDetailStoryById(id)
    fun addStory(image: File, description: String) {
        viewModelScope.launch {
            _story.value = repository.addStory(image, description)
        }
    }
}