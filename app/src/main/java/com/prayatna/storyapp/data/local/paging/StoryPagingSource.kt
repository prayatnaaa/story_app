package com.prayatna.storyapp.data.local.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.prayatna.storyapp.data.pref.UserPreference
import com.prayatna.storyapp.data.remote.response.ListStory
import com.prayatna.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class StoryPagingSource(private val apiService: ApiService, private val pref: UserPreference): PagingSource<Int, ListStory>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStory>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStory> {
        return try {
            val token = runBlocking { pref.getSession().first().token }
            val position = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getStories("Bearer $token", 0, position, params.loadSize)
            Log.d("okhttp", "Response: $token")

            if (response.listStory.isNullOrEmpty()) {
                Log.d("StoryPagingSource", "No stories returned")
            } else {
                Log.d("StoryPagingSource", "Received stories: ${response.listStory}")
            }

            LoadResult.Page(
                data = response.listStory.orEmpty().filterNotNull(),
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (response.listStory!!.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            Log.e("StoryPagingSource", "Error loading data: ${exception.localizedMessage}")
            return LoadResult.Error(exception)
        }
    }
}