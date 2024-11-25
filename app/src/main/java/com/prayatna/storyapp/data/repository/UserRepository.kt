package com.prayatna.storyapp.data.repository

import android.util.Log
import com.google.gson.Gson
import com.prayatna.storyapp.data.pref.UserPreference
import com.prayatna.storyapp.data.remote.response.ErrorResponse
import com.prayatna.storyapp.data.remote.response.LoginResponse
import com.prayatna.storyapp.data.remote.retrofit.ApiService
import com.prayatna.storyapp.data.source.UserModel
import com.prayatna.storyapp.helper.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPref: UserPreference
) {
    suspend fun saveSession(user: UserModel) {
        userPref.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPref.getSession()
    }

    suspend fun logout() {
        userPref.logout()
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(email, password)
            Log.d("LoginSuccess", "login: ${response.message}")
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Log.e("LoginError", "login: $errorMessage")
            Result.Error(errorMessage!!)
        }catch (e: Exception) {
            Log.e("LoginError", "login: ${e.message}")
            Result.Error(e.message.toString())
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null
        fun getInstance(apiService: ApiService, userPref: UserPreference): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(apiService, userPref)
                INSTANCE = instance
                instance
            }
        }
    }
}