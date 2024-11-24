package com.prayatna.storyapp.data.di

import android.content.Context
import com.prayatna.storyapp.data.pref.UserPreference
import com.prayatna.storyapp.data.pref.dataStore
import com.prayatna.storyapp.data.repository.UserRepository

object Injection {
    fun getInstance(context: Context) : UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}