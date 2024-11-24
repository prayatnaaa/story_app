package com.prayatna.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prayatna.storyapp.data.di.Injection
import com.prayatna.storyapp.data.repository.UserRepository
import com.prayatna.storyapp.ui.auth.login.LoginViewModel
import com.prayatna.storyapp.ui.main.MainViewModel

class ViewModelFactory private constructor(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        fun getInstance(context: Context) : ViewModelFactory {
            return INSTANCE ?: synchronized(this){
                val instance = ViewModelFactory(Injection.getInstance(context))
                INSTANCE = instance
                instance
            }
        }
    }
}