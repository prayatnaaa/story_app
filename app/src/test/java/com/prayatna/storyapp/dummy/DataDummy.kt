package com.prayatna.storyapp.dummy

import androidx.paging.PagingData
import com.prayatna.storyapp.data.remote.response.ListStory

object DataDummy {

    fun generateDummyStoryEntity(): PagingData<ListStory> {
        val storyList = ArrayList<ListStory>()
        for (i in 0..10) {
            val story = ListStory(
                name = "UI testing",
                description = "This is UI testing",
                photoUrl = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                id = "hahahha$i"
            )
            storyList.add(story)
        }
        return PagingData.from(storyList)
    }
}