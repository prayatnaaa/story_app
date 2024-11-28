package com.prayatna.storyapp.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.storyapp.data.remote.response.LoginResponse
import com.prayatna.storyapp.data.repository.AuthRepository
import com.prayatna.storyapp.data.source.UserModel
import com.prayatna.storyapp.helper.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private var _login = MutableLiveData<Result<LoginResponse>>()
    val login: LiveData<Result<LoginResponse>> = _login

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, password: String) {
        _login.value = Result.Loading
        viewModelScope.launch {
            _login.value = repository.login(email, password)
        }
    }
}