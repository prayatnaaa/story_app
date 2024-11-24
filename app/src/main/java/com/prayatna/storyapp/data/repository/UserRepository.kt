package com.prayatna.storyapp.data.repository

import com.prayatna.storyapp.data.pref.UserPreference
import com.prayatna.storyapp.data.source.UserModel
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(private val userPref: UserPreference){
    suspend fun saveSession(user: UserModel){
        userPref.saveSession(user)
    }
    fun getSession(): Flow<UserModel> {
        return userPref.getSession()
    }
    suspend fun logout() {
        userPref.logout()
    }

    companion object{
        @Volatile
        private var INSTANCE: UserRepository? = null
        fun getInstance(userPref: UserPreference): UserRepository {
            return INSTANCE ?: synchronized(this){
                val instance = UserRepository(userPref)
                INSTANCE = instance
                instance
            }
        }
    }
}