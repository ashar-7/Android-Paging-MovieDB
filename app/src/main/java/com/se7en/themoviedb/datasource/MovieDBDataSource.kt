package com.se7en.themoviedb.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.se7en.themoviedb.ApiService
import com.se7en.themoviedb.NetworkState
import com.se7en.themoviedb.adapters.FragmentType
import com.se7en.themoviedb.models.DiscoverResponse
import com.se7en.themoviedb.models.MovieTVShowModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class MovieDBDataSource(
    private val type: FragmentType,
    private val query: String,
    private val apiService: ApiService,
    private val retryExecutor: Executor

): PageKeyedDataSource<Int, MovieTVShowModel>() {

    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null

    fun retryAllFailed() {
        Log.d("DBDataSource", "retrying $retry")
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    fun getNextPage(currentPage: Int, totalPages: Int) =
        if(currentPage < totalPages) currentPage + 1 else null


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MovieTVShowModel>
    ) {

        networkState.postValue(NetworkState.Loading)
        initialLoad.postValue(NetworkState.Loading)

        val cb = object : Callback<DiscoverResponse> {
            override fun onFailure(call: Call<DiscoverResponse>, t: Throwable) {
                Log.d("MovieDBDataSource", t.message ?: "Null error")

                initialLoad.postValue(NetworkState.Failure(true, t))
                networkState.postValue(NetworkState.Failure(true, t))
                retry = { loadInitial(params, callback) }
            }

            override fun onResponse(
                call: Call<DiscoverResponse>,
                response: Response<DiscoverResponse>
            ) {
                val body = response.body() ?: return

                val currentPage = body.page
                val totalPages = body.total_pages
                val nextPage = getNextPage(currentPage, totalPages)

                val list = body.results
                callback.onResult(list, null, nextPage)

                retry = null
                networkState.postValue(NetworkState.Success(list.isEmpty()))
                initialLoad.postValue(NetworkState.Success(list.isEmpty()))
            }
        }

        when(type) {
            FragmentType.MOVIES ->
                when {
                    query.isBlank() -> apiService.getMovies(1).enqueue(cb)
                    else -> apiService.searchMovies(query, 1).enqueue(cb)
                }
            FragmentType.TV_SHOWS ->
                when {
                    query.isBlank() -> apiService.getTVShows(1).enqueue(cb)
                    else -> apiService.searchTVShows(query, 1).enqueue(cb)
                }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MovieTVShowModel>
    ) {
        networkState.postValue(NetworkState.Loading)

        val cb = object: Callback<DiscoverResponse> {
            override fun onFailure(call: Call<DiscoverResponse>, t: Throwable) {
                Log.d("MovieDBDataSource", t.message ?: "Null error")

                networkState.postValue(NetworkState.Failure(false, t))
                retry = { loadAfter(params, callback) }
            }

            override fun onResponse(
                call: Call<DiscoverResponse>,
                response: Response<DiscoverResponse>
            ) {
                val body = response.body() ?: return
                val currentPage = body.page
                val totalPages = body.total_pages
                val nextPage = getNextPage(currentPage, totalPages)

                val list = body.results
                callback.onResult(list, nextPage)
                networkState.postValue(NetworkState.Success(list.isEmpty()))
                retry = null
            }
        }

        when(type) {
            FragmentType.MOVIES ->
                when {
                    query.isBlank() -> apiService.getMovies(params.key).enqueue(cb)
                    else -> apiService.searchMovies(query, params.key).enqueue(cb)
                }
            FragmentType.TV_SHOWS ->
                when {
                    query.isBlank() -> apiService.getTVShows(params.key).enqueue(cb)
                    else -> apiService.searchTVShows(query, params.key).enqueue(cb)
                }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, MovieTVShowModel>
    ) {
        // not applicable for this project
    }
}
