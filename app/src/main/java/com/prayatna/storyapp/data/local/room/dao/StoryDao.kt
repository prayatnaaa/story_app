package com.prayatna.storyapp.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.prayatna.storyapp.data.remote.response.ListStory

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStories(stories: List<ListStory>)

    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int, ListStory>

    @Query("DELETE FROM story")
    suspend fun deleteAll()

    @Query("SELECT * FROM story WHERE id = :id")
    fun getStoryById(id: Int): LiveData<ListStory>
}