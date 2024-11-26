package com.prayatna.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prayatna.storyapp.data.di.Injection
import com.prayatna.storyapp.data.repository.UserRepository
import com.prayatna.storyapp.ui.auth.login.LoginViewModel
import com.prayatna.storyapp.ui.main.MainViewModel

class UserViewModelFactory private constructor(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserViewModelFactory? = null
        fun getInstance(context: Context) : UserViewModelFactory {
            return INSTANCE ?: synchronized(this){
                val instance = UserViewModelFactory(Injection.getInstance(context))
                INSTANCE = instance
                instance
            }
        }
    }
}