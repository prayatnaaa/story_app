package com.prayatna.storyapp.data.remote.retrofit

import com.prayatna.storyapp.data.remote.response.AddResponse
import com.prayatna.storyapp.data.remote.response.GetDetailStoryResponse
import com.prayatna.storyapp.data.remote.response.GetStoryResponse
import com.prayatna.storyapp.data.remote.response.LoginResponse
import okhttp3.MultipartBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //auth
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : AddResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : LoginResponse


    //stories
    @GET("stories")
    suspend fun getStories(
        @Query("location") location: String,
    ): GetStoryResponse

    @GET("stories/:id")
    suspend fun getDetailStoryById(
        @Path("id") id: String,
    ) : GetDetailStoryResponse

    @Multipart
    @POST("stories")
    fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: String,
    ) : AddResponse

    @Multipart
    @POST("stories/guest")
    fun addGuestStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: String
    ) : AddResponse
}