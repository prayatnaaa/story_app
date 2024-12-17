package com.prayatna.storyapp.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "story")
@Parcelize
data class StoryEntity(

    @PrimaryKey
    val id: Int =0,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String? = null,
    val lat: Double? = null,
    val lon: Double? = null
): Parcelable
