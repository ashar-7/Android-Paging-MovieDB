package com.se7en.themoviedb.repositories

import androidx.lifecycle.Transformations.switchMap
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.se7en.themoviedb.ApiService
import com.se7en.themoviedb.Listing
import com.se7en.themoviedb.adapters.FragmentType
import com.se7en.themoviedb.datasource.MovieDBDataSourceFactory
import com.se7en.themoviedb.models.MovieTVShowModel
import java.util.concurrent.Executor

class ListRepository private constructor(
    private val apiService: ApiService,
    private val networkExecutor: Executor
) {

    fun searchUsers(query: String, type: FragmentType): Listing<MovieTVShowModel> {

        val factory = MovieDBDataSourceFactory(type, query, apiService, networkExecutor)

        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .build()

        val livePagedList = LivePagedListBuilder(factory, config)
            .setFetchExecutor(networkExecutor)
            .build()

        return Listing(
            pagedList = livePagedList,
            networkState = switchMap(factory.sourceLiveData) { it.networkState },
            retry = { factory.sourceLiveData.value?.retryAllFailed() },
            refresh = { factory.sourceLiveData.value?.invalidate() },
            refreshState = switchMap(factory.sourceLiveData) { it.initialLoad })
    }

    companion object {
        @Volatile private var instance: ListRepository? = null

        fun getInstance(apiService: ApiService, executor: Executor): ListRepository {
            return instance ?: synchronized(this) {
                instance ?: ListRepository(apiService, executor).also { instance = it }
            }
        }
    }
}