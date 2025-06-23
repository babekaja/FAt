package com.babe.fata.data

import com.babe.fata.ApiConfig
import com.babe.fata.model.CastDetails
import com.babe.fata.model.CreditsResponse
import com.babe.fata.model.Movie
import com.babe.fata.model.MovieResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class MoviesRepositoryImpl(
    private val client: HttpClient
) : MoviesRepository {
    
    private val json = Json { 
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override suspend fun getPopular(page: Int): MovieResponse =
        fetchMovies(MovieCategory.POPULAR, page)

    override suspend fun getNowPlaying(page: Int): MovieResponse =
        fetchMovies(MovieCategory.NOW_PLAYING, page)

    override suspend fun getUpcoming(page: Int): MovieResponse =
        fetchMovies(MovieCategory.UPCOMING, page)

    override suspend fun getTopRated(page: Int): MovieResponse =
        fetchMovies(MovieCategory.TOP_RATED, page)

    override suspend fun getByCategory(category: MovieCategory, page: Int): MovieResponse =
        fetchMovies(category, page)

    override suspend fun searchMovies(query: String, page: Int): MovieResponse =
        fetchSearchResults(query, page)

    override suspend fun getByGenre(genreId: Int, page: Int): MovieResponse {
        val url = "${ApiConfig.baseUrl}/discover/movie"
        return executeRequest(url) {
            parameter("api_key", ApiConfig.apiKey)
            parameter("with_genres", genreId)
            parameter("page", page)
            parameter("language", ApiConfig.language)
        }
    }

    override suspend fun getMovieDetails(id: Int, language: String): Movie {
        val url = "${ApiConfig.baseUrl}/movie/$id"
        return executeRequest(url) {
            parameter("api_key", ApiConfig.apiKey)
            parameter("language", language)
        }
    }

    override suspend fun getSimilarMovies(id: Int, page: Int): MovieResponse {
        val url = "${ApiConfig.baseUrl}/movie/$id/similar"
        return executeRequest(url) {
            parameter("api_key", ApiConfig.apiKey)
            parameter("language", ApiConfig.language)
            parameter("page", page)
        }
    }

    override suspend fun getMovieCredits(id: Int, language: String): CreditsResponse {
        val url = "${ApiConfig.baseUrl}/movie/$id/credits"
        return executeRequest(url) {
            parameter("api_key", ApiConfig.apiKey)
            parameter("language", language)
        }
    }

    override suspend fun getCastDetails(castId: Int, language: String): CastDetails {
        val url = "${ApiConfig.baseUrl}/person/$castId"
        return executeRequest(url) {
            parameter("api_key", ApiConfig.apiKey)
            parameter("language", language)
        }
    }

    private suspend fun fetchMovies(category: MovieCategory, page: Int): MovieResponse {
        val url = "${ApiConfig.baseUrl}/movie/${category.path}"
        return executeRequest(url) {
            parameter("api_key", ApiConfig.apiKey)
            parameter("page", page)
            parameter("language", ApiConfig.language)
        }
    }

    private suspend fun fetchSearchResults(query: String, page: Int): MovieResponse {
        val url = "${ApiConfig.baseUrl}/search/movie"
        return executeRequest(url) {
            parameter("api_key", ApiConfig.apiKey)
            parameter("query", query)
            parameter("page", page)
            parameter("language", ApiConfig.language)
        }
    }

    /**
     * Méthode générique pour exécuter les requêtes avec gestion d'erreur
     */
    private suspend inline fun <reified T> executeRequest(
        url: String,
        crossinline block: HttpRequestBuilder.() -> Unit
    ): T {
        return try {
            val response: String = client.get(url) {
                contentType(ContentType.Application.Json)
                block()
            }.bodyAsText()
            
            json.decodeFromString<T>(response)
        } catch (e: Exception) {
            throw NetworkException("Erreur réseau: ${e.message}", e)
        }
    }
}

/**
 * Exception personnalisée pour les erreurs réseau
 */
class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)