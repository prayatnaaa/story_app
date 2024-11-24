package com.prayatna.storyapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.prayatna.storyapp.data.repository.UserRepository

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession() = repository.getSession().asLiveData()
}