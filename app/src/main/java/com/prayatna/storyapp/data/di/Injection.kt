package com.prayatna.storyapp.data.di

import android.content.Context
import com.prayatna.storyapp.data.pref.UserPreference
import com.prayatna.storyapp.data.pref.dataStore
import com.prayatna.storyapp.data.remote.retrofit.ApiConfig
import com.prayatna.storyapp.data.repository.UserRepository
import com.prayatna.storyapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun getInstance(context: Context) : AuthRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val userToken = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getInstance(userToken.token!!)
        return AuthRepository.getInstance(apiService, pref)
    }

    fun storyRepoInstance(context: Context) : UserRepository {
        val userToken = runBlocking { UserPreference.getInstance(context.dataStore).getSession().first() }
        val apiService = ApiConfig.getInstance(userToken.token!!)
        return UserRepository.getInstance(apiService)
    }
}