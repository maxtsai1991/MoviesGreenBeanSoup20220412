package com.example.moviesgreenbeansoup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.movie_row.view.*

class MovieAdapter(var movies : List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){

    inner class MovieViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val poster = itemView.movie_poster  // 電影照片(ImageView)
        val title = itemView.movie_title    // 電影名稱(TextView)
        val popularity = itemView.movie_pop // 電影評分(TextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_row, parent , false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        // 所有的電影(movies)如果不是null,在執行run裡面的工作
        movies?.run {
            val movie = movies.get(position)
            holder.title.setText(movie.title)
            holder.popularity.setText(movie.popularity.toString())
        }
    }

    override fun getItemCount(): Int {
        return movies.size ?: 0
    }
}