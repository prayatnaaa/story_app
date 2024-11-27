package com.prayatna.storyapp.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.prayatna.storyapp.data.repository.AuthRepository
import com.prayatna.storyapp.data.source.UserModel
import kotlinx.coroutines.launch

class SettingsViewModel(private val authRepository: AuthRepository) : ViewModel() {

    var user: LiveData<UserModel> = authRepository.getSession().asLiveData()

    fun logout() {
        viewModelScope.launch {
        authRepository.logout()
        }
    }
}