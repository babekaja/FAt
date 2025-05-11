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
    private val json = Json { ignoreUnknownKeys = true }

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

    private suspend fun fetchMovies(category: MovieCategory, page: Int): MovieResponse {
        val url = "${ApiConfig.baseUrl}/movie/${category.path}"
        val raw: String = client.get(url) {
            contentType(ContentType.Application.Json)
            parameter("api_key", ApiConfig.apiKey)
            parameter("page", page)
            parameter("language", "fr-FR")   // ← demande le français
        }.bodyAsText()
        return json.decodeFromString(raw)
    }

    private suspend fun fetchSearchResults(query: String, page: Int): MovieResponse {
        val url = "${ApiConfig.baseUrl}/search/movie"
        val raw: String = client.get(url) {
            contentType(ContentType.Application.Json)
            parameter("api_key", ApiConfig.apiKey)
            parameter("query", query)
            parameter("page", page)
            parameter("language", "fr-FR")   // ← idem
        }.bodyAsText()
        return json.decodeFromString(raw)
    }

    // MoviesRepositoryImpl.kt
    override suspend fun getByGenre(genreId: Int, page: Int): MovieResponse {
        val url = "${ApiConfig.baseUrl}/discover/movie"
        val raw: String = client.get(url) {
            contentType(ContentType.Application.Json)
            parameter("api_key", ApiConfig.apiKey)
            parameter("with_genres", genreId)
            parameter("page", page)
        }.bodyAsText()
        return json.decodeFromString(raw)
    }


    override suspend fun getMovieDetails(id: Int, language: String): Movie {
        val url = "${ApiConfig.baseUrl}/movie/$id"
        val raw: String = client.get(url) {
            contentType(ContentType.Application.Json)
            parameter("api_key", ApiConfig.apiKey)
            parameter("language", language)
        }.bodyAsText()
        return json.decodeFromString(raw)
    }


    override suspend fun getSimilarMovies(id: Int, page: Int): MovieResponse {
        val url = "${ApiConfig.baseUrl}/movie/$id/similar"
        val raw: String = client.get(url) {
            contentType(ContentType.Application.Json)
            parameter("api_key", ApiConfig.apiKey)
            parameter("language", "fr-FR")
            parameter("page", page)
        }.bodyAsText()
        return json.decodeFromString(raw)
    }

    override suspend fun getMovieCredits(id: Int, language: String): CreditsResponse {
        val url = "${ApiConfig.baseUrl}/movie/$id/credits"
        val raw: String = client.get(url) {
            contentType(ContentType.Application.Json)
            parameter("api_key", ApiConfig.apiKey)
            parameter("language", language)
        }.bodyAsText()
        return json.decodeFromString(raw)
    }

    override suspend fun getCastDetails(castId: Int, language: String): CastDetails {
        val url = "${ApiConfig.baseUrl}/person/$castId"
        val rawJson: String = client.get(url) {
            contentType(ContentType.Application.Json)
            parameter("api_key", ApiConfig.apiKey)
            parameter("language", language)
        }.bodyAsText()
        // On précise le type générique pour que Kotlinx le désérialise bien en CastDetails
        return json.decodeFromString<CastDetails>(rawJson)
    }


}
