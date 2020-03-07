package com.se7en.themoviedb.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.se7en.themoviedb.ApiService
import com.se7en.themoviedb.adapters.FragmentType
import com.se7en.themoviedb.models.MovieTVShowModel
import java.util.concurrent.Executor

class MovieDBDataSourceFactory(
    private val type: FragmentType,
    private val query: String,
    private val service: ApiService,
    private val retryExecutor: Executor
) : DataSource.Factory<Int, MovieTVShowModel>() {
    var sourceLiveData = MutableLiveData<MovieDBDataSource>()

    override fun create(): DataSource<Int, MovieTVShowModel> {
        val source = MovieDBDataSource(type, query, service, retryExecutor)
        sourceLiveData.postValue(source)
        return source
    }
}
