package com.example.moviesgreenbeansoup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import kotlinx.android.synthetic.main.movie_row.view.*

class MovieAdapter(var movies : List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>(){

    inner class MovieViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val poster = itemView.movie_poster  // 電影照片/海報(ImageView)
        val title = itemView.movie_title    // 電影名稱(TextView)
        val popularity = itemView.movie_pop // 電影評分(TextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_row, parent , false)
        return MovieViewHolder(view)
    }
        // 得到TMDP資料的時候,是跑到onBindViewHolder方法裡
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        // 所有的電影(movies)如果不是null,在執行run裡面的工作
        movies?.run {
            val movie = movies.get(position)
            holder.title.setText(movie.title)
            holder.popularity.setText(movie.popularity.toString())
            // 完整TMDP圖片URL是 : https://image.tmdb.org/t/p/w500/wwemzKWzjKYJFfCeiB57q3r4Bcm.png
            holder.poster.load("https://image.tmdb.org/t/p/w500${movie.poster_path}"){
                placeholder(R.drawable.movie_poster)        // 還沒讀到API圖時顯示的圖片
//                transformations(CircleCropTransformation()) // 圓形圖顯示 , 如註解掉,圖片則顯示長方形
                error(R.drawable.error_movie_poster)        // 讀不到API圖時顯示的圖片
            }
        }
    }

    override fun getItemCount(): Int {
        return movies.size ?: 0
    }
}