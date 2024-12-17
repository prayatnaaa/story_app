package com.prayatna.storyapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prayatna.storyapp.data.local.entity.StoryEntity

interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStories(stories: List<StoryEntity>)

    @Query("SELECT * FROM story")
    fun getStories(): LiveData<List<StoryEntity>>

    @Query("SELECT * FROM story WHERE id = :id")
    fun getStoryById(id: Int): LiveData<StoryEntity>
}