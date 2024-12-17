package com.prayatna.storyapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.prayatna.storyapp.data.remote.response.ListStory

@Database(entities = [ListStory::class], version = 1, exportSchema = false)
abstract class StoryDatabase: RoomDatabase() {
    companion object {
        @Volatile
        private var instance: StoryDatabase? = null
        fun getInstance(context: Context): StoryDatabase =
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                StoryDatabase::class.java, "Story.db"
            ).fallbackToDestructiveMigration().build()
    }
}