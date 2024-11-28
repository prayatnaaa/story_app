package com.prayatna.storyapp.data.di

import android.content.Context
import com.prayatna.storyapp.data.pref.UserPreference
import com.prayatna.storyapp.data.pref.dataStore
import com.prayatna.storyapp.data.remote.retrofit.ApiConfig
import com.prayatna.storyapp.data.repository.AuthRepository

object Injection {
    fun getInstance(context: Context) : AuthRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getInstance()
        return AuthRepository.getInstance(apiService, pref)
    }
}