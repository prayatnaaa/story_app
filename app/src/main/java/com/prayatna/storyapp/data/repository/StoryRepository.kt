package com.prayatna.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.gson.Gson
import com.prayatna.storyapp.data.remote.response.ErrorResponse
import com.prayatna.storyapp.data.remote.response.GetDetailStoryResponse
import com.prayatna.storyapp.data.remote.response.GetStoryResponse
import com.prayatna.storyapp.data.remote.retrofit.ApiService
import com.prayatna.storyapp.helper.Result
import retrofit2.HttpException

class StoryRepository private constructor(private val apiService: ApiService) {

    fun getStories(location: String): LiveData<Result<GetStoryResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val getStories = apiService.getStories(location)
                Log.d("StoryList", getStories.toString())
                emit(Result.Success(getStories))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                Log.e("LoginError", "login: $errorMessage")
                emit(Result.Error(errorMessage!!))
            } catch (e: Exception) {
                Log.e("LoginError", "login: ${e.message}")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun getDetailStoryById(id: String): LiveData<Result<GetDetailStoryResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val detailStory = apiService.getDetailStoryById(id)
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

    companion object {
        @Volatile
        private var INSTANCE: StoryRepository? = null
        fun getInstance(apiService: ApiService): StoryRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = StoryRepository(apiService)
                INSTANCE = instance
                instance
            }
        }
    }
}