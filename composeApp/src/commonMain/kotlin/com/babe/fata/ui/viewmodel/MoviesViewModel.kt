package com.babe.fata.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.babe.fata.data.MovieCategory
import com.babe.fata.domaine.usecase.GetCastDetailsUseCase
import com.babe.fata.domaine.usecase.GetMovieCreditsUseCase
import com.babe.fata.domaine.usecase.GetMovieDetailsUseCase
import com.babe.fata.domaine.usecase.GetMoviesByGenreUseCase
import com.babe.fata.domaine.usecase.GetNowPlayingMoviesUseCase
import com.babe.fata.domaine.usecase.GetPopularMoviesUseCase
import com.babe.fata.domaine.usecase.GetSimilarMoviesUseCase
import com.babe.fata.domaine.usecase.GetTopRatedMoviesUseCase
import com.babe.fata.domaine.usecase.GetUpcomingMoviesUseCase
import com.babe.fata.domaine.usecase.SearchMoviesUseCase
import com.babe.fata.model.CastDetails
import com.babe.fata.model.CastMember
import com.babe.fata.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val getPopular: GetPopularMoviesUseCase,
    private val getNowPlaying: GetNowPlayingMoviesUseCase,
    private val getUpcoming: GetUpcomingMoviesUseCase,
    private val getTopRated: GetTopRatedMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val getByGenre: GetMoviesByGenreUseCase,
    private val getMovieDetails: GetMovieDetailsUseCase,
    private val getSimilar: GetSimilarMoviesUseCase,
    private val getMovieCredits: GetMovieCreditsUseCase,
    private val getCastDetails: GetCastDetailsUseCase

) : ViewModel() {



    var movieDetail by mutableStateOf<Movie?>(null)
        private set
    var popularMovies by mutableStateOf<List<Movie>>(emptyList())
        private set
    var nowPlayingMovies by mutableStateOf<List<Movie>>(emptyList())
        private set
    var upcomingMovies by mutableStateOf<List<Movie>>(emptyList())
        private set
    var topRatedMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    // Résultats de recherche
    var searchResults by mutableStateOf<List<Movie>>(emptyList())
        private set

    // Films par genre
    var genreMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var similarMovies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var movieCredits by mutableStateOf<List<CastMember>>(emptyList())
        private set

    var selectedCast by mutableStateOf<CastMember?>(null)
        private set

    var castDetails by mutableStateOf<CastDetails?>(null)
        private set

    // Scope dédié pour les chargements
    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * Charge toutes les catégories en parallèle et gère le loader global.
     */
    fun loadAll(page: Int = 1) {
        isLoading = true
        scope.launch {
            val popDeferred = async { getPopular(page).results }
            val nowDeferred = async { getNowPlaying(page).results }
            val upcomingDeferred = async { getUpcoming(page).results }
            val topDeferred = async { getTopRated(page).results }

            popularMovies = popDeferred.await()
            nowPlayingMovies = nowDeferred.await()
            upcomingMovies = upcomingDeferred.await()
            topRatedMovies = topDeferred.await()

            isLoading = false
        }
    }

    /**
     * Charge une seule catégorie selon l'enum et gère le loader.
     */
    fun loadCategory(category: MovieCategory, page: Int = 1) {
        isLoading = true
        scope.launch {
            when (category) {
                MovieCategory.POPULAR -> popularMovies = getPopular(page).results
                MovieCategory.NOW_PLAYING -> nowPlayingMovies = getNowPlaying(page).results
                MovieCategory.UPCOMING -> upcomingMovies = getUpcoming(page).results
                MovieCategory.TOP_RATED -> topRatedMovies = getTopRated(page).results
            }
            isLoading = false
        }
    }

    /**
     * Met à jour la liste de résultats selon la requête de recherche.
     */
    fun searchMovies(query: String, page: Int = 1) {
        if (query.isBlank()) {
            searchResults = emptyList()
            return
        }

        isLoading = true
        scope.launch {
            val response = searchMoviesUseCase(query, page)
            searchResults = response.results
            isLoading = false
        }
    }

    /**
     * Charge une liste de films par genre.
     */
    fun loadByGenre(genreId: Int, page: Int = 1) {
        isLoading = true
        scope.launch {
            genreMovies = getByGenre(genreId, page).results
            isLoading = false
        }
    }

    /**
     * Récupère le détail d'un film et met à jour le state.
     */
    fun loadMovieDetail(id: Int, language: String = "fr-FR") {
        isLoading = true
        scope.launch {
            val detail = getMovieDetails(id, language)
            movieDetail = detail
            isLoading = false
        }
    }

    /**
     * Recherche localement un film par son ID dans les listes chargées.
     */
    fun findById(id: Int): Movie? {
        return popularMovies
            .plus(nowPlayingMovies)
            .plus(upcomingMovies)
            .plus(topRatedMovies)
            .plus(searchResults)
            .firstOrNull { it.id == id }
    }


    fun loadSimilar(movieId: Int, page: Int = 1) {
        isLoading = true
        scope.launch {
            val response = getSimilar(movieId, page)
            similarMovies = response.results
            isLoading = false
        }
    }

    fun loadMovieCredits(movieId: Int, language: String = "fr-FR") {
        isLoading = true
        scope.launch {
            val credits = getMovieCredits(movieId, language)
            movieCredits = credits.cast
            isLoading = false
        }
    }

    fun selectCast(cast: CastMember) {
        selectedCast = cast
        loadMovieCredits(cast.id)
    }
    fun loadCastDetails(castId: Int, language: String = "fr-FR") {
        isLoading = true
        scope.launch {
            castDetails = getCastDetails(castId, language)
            isLoading = false
        }
    }



}