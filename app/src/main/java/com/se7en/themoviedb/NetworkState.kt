package com.se7en.themoviedb

sealed class NetworkState {
    object Loading: NetworkState()
    // isEmpty to show "No results found" error
    class Success(val isEmpty: Boolean) : NetworkState()
    class Failure(val isInitial: Boolean, val throwable: Throwable) : NetworkState()
}
