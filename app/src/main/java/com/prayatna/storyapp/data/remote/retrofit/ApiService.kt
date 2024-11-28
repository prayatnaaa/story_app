package com.prayatna.storyapp.data.remote.retrofit

import com.prayatna.storyapp.data.remote.response.AddResponse
import com.prayatna.storyapp.data.remote.response.GetDetailStoryResponse
import com.prayatna.storyapp.data.remote.response.GetStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //stories
    @GET("stories")
    suspend fun getStories(
        @Query("location") location: String,
    ): GetStoryResponse

    @GET("stories/{id}")
    suspend fun getDetailStoryById(
        @Path("id") id: String,
    ) : GetDetailStoryResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ) : AddResponse
}