package com.prayatna.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prayatna.storyapp.data.di.Injection
import com.prayatna.storyapp.data.repository.AuthRepository
import com.prayatna.storyapp.data.repository.UserRepository
import com.prayatna.storyapp.ui.home.UserViewModel
import cz.msebera.android.httpclient.auth.AUTH

class UserViewModelFactory private constructor(private val repository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(repository) as T
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserViewModelFactory? = null
        fun getInstance(context: Context): UserViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = UserViewModelFactory(Injection.getInstance(context))
                INSTANCE = instance
                instance
            }
        }
    }
}