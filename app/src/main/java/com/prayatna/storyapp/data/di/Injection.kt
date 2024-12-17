package com.prayatna.storyapp.data.di

import android.content.Context
import com.prayatna.storyapp.data.local.room.StoryDatabase
import com.prayatna.storyapp.data.pref.UserPreference
import com.prayatna.storyapp.data.pref.dataStore
import com.prayatna.storyapp.data.remote.retrofit.ApiConfig
import com.prayatna.storyapp.data.repository.AuthRepository
import com.prayatna.storyapp.data.repository.UserRepository

object Injection {
    fun getInstance(context: Context) : AuthRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getInstance()
        return AuthRepository.getInstance(apiService, pref)
    }

    fun storyRepoInstance(context: Context) : UserRepository {
        val apiService = ApiConfig.getInstance()
        val pref = UserPreference.getInstance(context.dataStore)
        val database = StoryDatabase.getInstance(context)
        return UserRepository.getInstance(apiService, pref, database)
    }
}