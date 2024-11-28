package com.prayatna.storyapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.prayatna.storyapp.data.repository.AuthRepository

class MainViewModel(private val repository: AuthRepository) : ViewModel() {
    fun getSession() = repository.getSession().asLiveData()
}