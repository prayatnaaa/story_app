package com.prayatna.storyapp.ui.user

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.prayatna.storyapp.data.remote.response.AddResponse
import com.prayatna.storyapp.data.remote.response.ListStory
import com.prayatna.storyapp.data.repository.UserRepository
import com.prayatna.storyapp.helper.Result
import kotlinx.coroutines.launch
import java.io.File

class UserViewModel(private var repository: UserRepository) : ViewModel() {
    private var _story = MutableLiveData<Result<AddResponse>>()
    val story: MutableLiveData<Result<AddResponse>> = _story

    var currentImageUri: Uri? = null
    val stories: LiveData<PagingData<ListStory>> =
        repository.getStory().cachedIn(viewModelScope)

//    fun getStories() = repository.getStories()
    fun getDetailStoryById(id: String) = repository.getDetailStoryById(id)
    fun addStory(image: File, description: String) {
        _story.value = Result.Loading
        viewModelScope.launch{
            _story.value = repository.addStory(image, description)
        }
    }
}