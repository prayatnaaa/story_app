package com.prayatna.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import com.prayatna.storyapp.data.local.paging.StoryPagingSource
import com.prayatna.storyapp.data.local.room.StoryDatabase
import com.prayatna.storyapp.data.pref.UserPreference
import com.prayatna.storyapp.data.remote.response.AddResponse
import com.prayatna.storyapp.data.remote.response.ErrorResponse
import com.prayatna.storyapp.data.remote.response.GetDetailStoryResponse
import com.prayatna.storyapp.data.remote.response.GetStoryResponse
import com.prayatna.storyapp.data.remote.response.ListStory
import com.prayatna.storyapp.data.remote.retrofit.ApiService
import com.prayatna.storyapp.helper.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPref: UserPreference,
    private val database: StoryDatabase
) {

    private val token = runBlocking { userPref.getSession().first().token }

//    fun getStories(): LiveData<Result<List<ListStory>>> {
//        return liveData {
//            Pager(
//                config = PagingConfig(
//                    pageSize = 5
//                ),
//                pagingSourceFactory = {
//                    StoryPagingSource(database, apiService, userPref)
//                }
//            )
//        }
//    }

    fun getStory(): LiveData<PagingData<ListStory>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryPagingSource(database, apiService, userPref),
            pagingSourceFactory = {
                database.storyDao().getStories()
            }
        ).liveData
    }

    fun getDetailStoryById(id: String): LiveData<Result<GetDetailStoryResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val detailStory = apiService.getDetailStoryById("Bearer $token", id)
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

    suspend fun addStory(image: File, description: String): Result<AddResponse> {
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

    fun getStoriesWithLocation(): LiveData<Result<GetStoryResponse>> {
        return liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getStoriesWithLocation(token = "Bearer $token")
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                emit(Result.Error(errorMessage!!))
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null
        fun getInstance(apiService: ApiService, userPref: UserPreference, database: StoryDatabase): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(apiService, userPref, database)
                INSTANCE = instance
                instance
            }
        }
    }
}