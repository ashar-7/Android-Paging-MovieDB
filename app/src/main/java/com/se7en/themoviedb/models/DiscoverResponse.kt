package com.se7en.themoviedb.models

data class DiscoverResponse(
    val page: Int,
    val total_results: Int,
    val total_pages: Int,
    val results: List<MovieTVShowModel>
)
