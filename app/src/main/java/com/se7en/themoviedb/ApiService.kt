package com.se7en.themoviedb

import com.se7en.themoviedb.models.DiscoverResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("discover/movie")
    fun getMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<DiscoverResponse>

    @GET("discover/tv")
    fun getTVShows(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<DiscoverResponse>

    @GET("search/movie")
    fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<DiscoverResponse>

    @GET("search/tv")
    fun searchTVShows(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Call<DiscoverResponse>

    companion object {
        // generate and add your api key own here
        const val API_KEY = "api-key"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}