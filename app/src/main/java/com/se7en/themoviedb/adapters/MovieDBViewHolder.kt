package com.se7en.themoviedb.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.se7en.themoviedb.models.MovieTVShowModel
import kotlinx.android.synthetic.main.movie_recycler_item.view.*

class MovieDBViewHolder(view: View, private val parentWidth: Int): RecyclerView.ViewHolder(view) {

    fun bind(item: MovieTVShowModel?) {
        // change item size based on device width
        val margin = 2
        val numColumns = 3
        val itemSize = (parentWidth / numColumns) - (2 * margin)
        itemView.posterImage.layoutParams.width = itemSize

        if(item != null) {
            itemView.movieName.text = item.title

            Glide.with(itemView.context).load(
                "https://image.tmdb.org/t/p/w185/${item.poster_path}"
            ).into(itemView.posterImage)
        }
    }
}
