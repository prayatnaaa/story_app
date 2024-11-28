package com.prayatna.storyapp.data.repository

import com.google.gson.Gson
import com.prayatna.storyapp.data.pref.UserPreference
import com.prayatna.storyapp.data.remote.response.AddResponse
import com.prayatna.storyapp.data.remote.response.ErrorResponse
import com.prayatna.storyapp.data.remote.response.LoginResponse
import com.prayatna.storyapp.data.remote.retrofit.AuthApiService
import com.prayatna.storyapp.data.source.UserModel
import com.prayatna.storyapp.helper.Result
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class AuthRepository private constructor(
    private val apiService: AuthApiService,
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
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Result.Error(errorMessage!!)
        }catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<AddResponse> {
        return try {
            val response = apiService.register(name, email, password)
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Result.Error(errorMessage!!)
        }catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: AuthRepository? = null
        fun getInstance(apiService: AuthApiService, userPref: UserPreference): AuthRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthRepository(apiService, userPref)
                INSTANCE = instance
                instance
            }
        }
    }
}