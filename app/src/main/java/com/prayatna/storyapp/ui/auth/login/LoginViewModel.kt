package com.prayatna.storyapp.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.storyapp.data.repository.UserRepository
import com.prayatna.storyapp.data.source.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository): ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}