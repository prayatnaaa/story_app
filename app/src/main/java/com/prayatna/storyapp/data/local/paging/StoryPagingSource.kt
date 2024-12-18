package com.prayatna.storyapp.data.local.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.prayatna.storyapp.data.local.entity.RemoteKeys
import com.prayatna.storyapp.data.local.room.StoryDatabase
import com.prayatna.storyapp.data.pref.UserPreference
import com.prayatna.storyapp.data.remote.response.ListStory
import com.prayatna.storyapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalPagingApi::class)
class StoryPagingSource(
    private val database: StoryDatabase,
    private val apiService: ApiService,
    private val pref: UserPreference
) : RemoteMediator<Int, ListStory>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

//    override fun getRefreshKey(state: PagingState<Int, ListStory>): Int? {
//        return state.anchorPosition?.let { position ->
//            val anchorPage = state.closestPageToPosition(position)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStory> {
//        return try {
//            val token = runBlocking { pref.getSession().first().token }
//            val position = params.key ?: INITIAL_PAGE_INDEX
//            val response = apiService.getStories("Bearer $token", 0, position, params.loadSize)
//            Log.d("okhttp", "Response: $token")
//
//            if (response.listStory.isNullOrEmpty()) {
//                Log.d("StoryPagingSource", "No stories returned")
//            } else {
//                Log.d("StoryPagingSource", "Received stories: ${response.listStory}")
//            }
//
//            LoadResult.Page(
//                data = response.listStory.orEmpty().filterNotNull(),
//                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
//                nextKey = if (response.listStory!!.isEmpty()) null else position + 1
//            )
//        } catch (exception: Exception) {
//            Log.e("StoryPagingSource", "Error loading data: ${exception.localizedMessage}")
//            return LoadResult.Error(exception)
//        }
//    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStory>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyForClosestPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLasItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
                nextKey
            }
        }
        try {
            val token = runBlocking { pref.getSession().first().token }
            val response = apiService.getStories("Bearer $token", 0, page, state.config.pageSize)
            val endPageReach = response.listStory!!.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.storyDao().deleteAll()
                }
                val prefKey = if (page == 1) null else page - 1
                val nextKey = if (endPageReach) null else page + 1
                val keys = response.listStory.map {
                    RemoteKeys(id = it!!.id, prevKey = prefKey, nextKey = nextKey)
                }
                database.remoteKeysDao().insertAll(keys)
                database.storyDao().insertStories(response.listStory.filterNotNull())
            }
            return MediatorResult.Success(endOfPaginationReached = endPageReach)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLasItem(state: PagingState<Int, ListStory>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ListStory>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForClosestPosition(state: PagingState<Int, ListStory>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

}