package com.prayatna.storyapp.data.remote.response

import androidx.room.Entity
import androidx.room.PrimaryKey

data class GetStoryResponse(
    val listStory: List<ListStory?>? = null,
    val error: Boolean? = null,
    val message: String? = null
)

@Entity(tableName = "story")
data class ListStory(
    val photoUrl: String? = null,
    val createdAt: String? = null,
    val name: String? = null,
    val description: String? = null,
    val lon: Double? = null,
    @PrimaryKey
    val id: String,
    val lat: Double? = null
)

