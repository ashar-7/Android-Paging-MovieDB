package com.se7en.themoviedb.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.ViewModel
import com.se7en.themoviedb.ApiService
import com.se7en.themoviedb.adapters.FragmentType
import com.se7en.themoviedb.repositories.ListRepository
import java.util.concurrent.Executors

class ListViewModel(type: FragmentType): ViewModel() {

    private val executor = Executors.newFixedThreadPool(5)
    private val apiService = ApiService.create()
    private val repository = ListRepository.getInstance(apiService, executor)

    private val searchQuery = MutableLiveData<String>()

    private val listing = map(searchQuery) {
        repository.searchUsers(it, type)
    }

    val pagedList = switchMap(listing) { it.pagedList }
    val networkState = switchMap(listing) { it.networkState }
    val refreshState = switchMap(listing) { it.refreshState }

    fun setQuery(query: String) {
        if(searchQuery.value == query) return
        searchQuery.value = query
    }

    fun refresh() {
        listing.value?.refresh?.invoke()
    }

    fun retry() {
        listing.value?.retry?.invoke()
    }
}
