package com.babe.fata.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,

    @SerialName("poster_path")      val posterPath: String?           = null,
    @SerialName("backdrop_path")    val backdropPath: String?         = null,
    @SerialName("vote_average")     val voteAverage: Double?          = null,
    @SerialName("vote_count")       val voteCount: Int?               = null,
    @SerialName("release_date")     val releaseDate: String?          = null,
    val popularity: Double?           = null,
    val adult: Boolean?               = null,
    @SerialName("original_title")   val originalTitle: String?        = null,
    @SerialName("original_language")val originalLanguage: String?     = null,

    // Pour les listes, TMDB renvoie genre_ids ; on met une valeur par défaut
    @SerialName("genre_ids")
    val genreIds: List<Int>                   = emptyList(),

    // Pour l'écran détail, TMDB renvoie plutôt un tableau `genres`
    @SerialName("genres")
    val genres: List<Genre>?                  = null,

    // Champs supplémentaires pour détails
    val budget: Long?                         = null,
    val revenue: Long?                        = null,
    val runtime: Int?                         = null,
    val status: String?                       = null,
    val tagline: String?                      = null,
    val homepage: String?                     = null,
    @SerialName("imdb_id")
    val imdbId: String?                       = null,
    @SerialName("belongs_to_collection")
    val belongsToCollection: CollectionInfo?  = null,
    @SerialName("origin_country")
    val originCountry: List<String>?          = null,

    // Pour l'écran détail, TMDB renvoie ces tableaux complets
    @SerialName("production_companies")
    val productionCompanies: List<Company>?    = null,
    @SerialName("production_countries")
    val productionCountries: List<Country>?    = null,
    @SerialName("spoken_languages")
    val spokenLanguages: List<Language>?       = null,

    // champ `video` existe sur certaines réponses
    val video: Boolean?                       = null
)

@Serializable
data class Genre(
    val id: Int,
    val name: String
)

@Serializable
data class CollectionInfo(
    val id: Int,
    val name: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null
)

@Serializable
data class Company(
    val id: Int,
    val name: String,
    @SerialName("logo_path") val logoPath: String? = null,
    @SerialName("origin_country") val originCountry: String? = null
)

@Serializable
data class Country(
    @SerialName("iso_3166_1") val isoCode: String,
    val name: String
)

@Serializable
data class Language(
    @SerialName("iso_639_1") val isoCode: String,
    @SerialName("english_name") val englishName: String? = null,
    val name: String? = null
)