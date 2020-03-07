package com.se7en.themoviedb.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

//@Entity
data class MovieTVShowModel(
    //@PrimaryKey
    var id: Int,
    var popularity: Float,
    var vote_count: Int,
    var video: Boolean?, // movies only
    var poster_path: String,
    var adult: Boolean?, // movies only
    var backdrop_path: String,
    var original_language: String,
    //@Ignore
    var genre_ids: List<Int>,
    var vote_average: Float,
    var overview: String,
    //@Ignore
    var origin_country: List<String>?, // tv shows only

    @SerializedName("original_title", alternate=["original_name"])
    var original_title: String,
    @SerializedName("title", alternate=["name"])
    var title: String,
    @SerializedName("release_date", alternate=["first_air_date"])
    var release_date: String
): Serializable

