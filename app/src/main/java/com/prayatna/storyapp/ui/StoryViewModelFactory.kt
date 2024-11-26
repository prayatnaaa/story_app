package com.prayatna.storyapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prayatna.storyapp.data.di.Injection
import com.prayatna.storyapp.data.repository.StoryRepository
import com.prayatna.storyapp.ui.home.HomeViewModel

class StoryViewModelFactory private constructor(private val storyRepository: StoryRepository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
             HomeViewModel(storyRepository) as T
            }

            else -> {
                throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: StoryViewModelFactory? = null
        fun getInstance(context: Context): StoryViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryViewModelFactory(Injection.storyRepoInstance(context))
                INSTANCE = instance
                instance
            }
        }
    }
}