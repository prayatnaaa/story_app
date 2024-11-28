package com.prayatna.storyapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.prayatna.storyapp.data.repository.AuthRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AuthRepository) : ViewModel() {
    fun getSession() = repository.getSession().asLiveData()
    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}