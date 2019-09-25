package com.ian.junemon.spe_learning_mvvm.movie.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *
Created by Ian Damping on 24/09/2019.
Github = https://github.com/iandamping
 */
@Entity(tableName = "movie_popular_pagination_data")
data class MoviePopularPaginationData(
        @PrimaryKey
        @ColumnInfo(name = "imdb_movie_popular_pagination__id") var id: Int?,
        @ColumnInfo(name = "imdb_movie_popular_pagination__tittle") var title: String?,
        @ColumnInfo(name = "imdb_movie_popular_pagination__poster_path") var poster_path: String?
)