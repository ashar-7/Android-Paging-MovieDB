package com.se7en.themoviedb.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.se7en.themoviedb.R
import com.se7en.themoviedb.models.MovieTVShowModel

class HomeRecyclerAdapter:
    PagedListAdapter<MovieTVShowModel, MovieDBViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<MovieTVShowModel>() {

            override fun areItemsTheSame(
                oldItem: MovieTVShowModel,
                newItem: MovieTVShowModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MovieTVShowModel,
                newItem: MovieTVShowModel
            ): Boolean {
                return (oldItem.title == newItem.title)
                        && (oldItem.poster_path == newItem.poster_path)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): MovieDBViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_recycler_item, parent, false) as View

        return MovieDBViewHolder(rootView, parent.measuredWidth)
    }

    override fun onBindViewHolder(holder: MovieDBViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
