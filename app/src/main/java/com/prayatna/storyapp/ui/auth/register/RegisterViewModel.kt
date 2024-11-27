package com.prayatna.storyapp.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.storyapp.data.remote.response.AddResponse
import com.prayatna.storyapp.data.repository.AuthRepository
import com.prayatna.storyapp.helper.Result
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository): ViewModel() {

    private var _register = MutableLiveData<Result<AddResponse>>()
    val register: LiveData<Result<AddResponse>> = _register

    fun register(name: String, email: String, password: String) {
        _register.value = Result.Loading
        viewModelScope.launch {
            _register.value = repository.register(name, email, password)
        }
    }

}