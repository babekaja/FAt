// src/commonMain/kotlin/com/babe/fata/data/MoviesRepository.kt
package com.babe.fata.data

import com.babe.fata.model.CastDetails
import com.babe.fata.model.CastMember
import com.babe.fata.model.CreditsResponse
import com.babe.fata.model.Movie
import com.babe.fata.model.MovieResponse

interface MoviesRepository {
    suspend fun getPopular(page: Int = 1): MovieResponse
    suspend fun getNowPlaying(page: Int = 1): MovieResponse
    suspend fun getUpcoming(page: Int = 1): MovieResponse
    suspend fun getTopRated(page: Int = 1): MovieResponse

    // optionnel : méthode générique si vous voulez un seul point d'entrée
    suspend fun getByCategory(category: MovieCategory, page: Int = 1): MovieResponse

    // Nouvelle méthode pour la recherche de films
    suspend fun searchMovies(query: String, page: Int = 1): MovieResponse

    suspend fun getByGenre(genreId: Int, page: Int = 1): MovieResponse

    suspend fun getMovieDetails(id: Int, language: String = "fr-FR"): Movie


    suspend fun getSimilarMovies(id: Int, page: Int = 1): MovieResponse

    suspend fun getMovieCredits(id: Int, language: String = "fr-FR"): CreditsResponse

    suspend fun getCastDetails(castId: Int, language: String = "fr-FR"): CastDetails


}

// src/commonMain/kotlin/com/babe/fata/data/MovieCategory.kt


enum class MovieCategory(val path: String) {
    POPULAR("popular"),
    NOW_PLAYING("now_playing"),
    UPCOMING("upcoming"),
    TOP_RATED("top_rated"),
}
