package com.prayatna.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.prayatna.storyapp.data.pref.UserPreference
import com.prayatna.storyapp.data.remote.response.AddResponse
import com.prayatna.storyapp.data.remote.response.ErrorResponse
import com.prayatna.storyapp.data.remote.response.GetDetailStoryResponse
import com.prayatna.storyapp.data.remote.response.GetStoryResponse
import com.prayatna.storyapp.data.remote.retrofit.ApiService
import com.prayatna.storyapp.data.source.UserModel
import com.prayatna.storyapp.helper.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(private val apiService: ApiService, private val userPref: UserPreference) {

    private val getToken = runBlocking { userPref.getSession().first().token }

    fun getSession(): Flow<UserModel> {
        return userPref.getSession()
    }


    fun getStories(location: String, token: String): LiveData<Result<GetStoryResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val getStories = apiService.getStories(location, "Bearer $token")
                emit(Result.Success(getStories))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                emit(Result.Error(errorMessage!!))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getDetailStoryById(id: String, token: String): LiveData<Result<GetDetailStoryResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val detailStory = apiService.getDetailStoryById(id, "Bearer $token")
                emit(Result.Success(detailStory))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                Log.e("LoginError", "login: $errorMessage")
                Result.Error(errorMessage!!)
            } catch (e: Exception) {
                Log.e("LoginError", "login: ${e.message}")
                emit(Result.Error(e.message.toString()))
            }
        }

    suspend fun addStory(image: File, description: String, token: String): Result<AddResponse> {
        return try {
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImage = image.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                image.name,
                requestImage
            )
            val response = apiService.addStory(multipartBody, requestBody, "Bearer $token")
            Result.Success(response)
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            Log.e("LoginError", "login: $errorMessage")
            Result.Error(errorMessage!!)
        } catch (e: Exception) {
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