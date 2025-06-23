package com.babe.fata.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.babe.fata.data.MovieCategory
import com.babe.fata.domaine.usecase.*
import com.babe.fata.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

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

    // UI State avec StateFlow pour une meilleure gestion d'état
    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    // États individuels pour compatibilité avec le code existant
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
    var searchResults by mutableStateOf<List<Movie>>(emptyList())
        private set
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

    // Gestion d'erreur améliorée
    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState.asStateFlow()

    /**
     * Charge toutes les catégories en parallèle avec gestion d'erreur améliorée
     */
    fun loadAll(page: Int = 1) {
        viewModelScope.launch {
            isLoading = true
            _errorState.value = null
            
            try {
                val deferredResults = listOf(
                    async { getPopular(page) },
                    async { getNowPlaying(page) },
                    async { getUpcoming(page) },
                    async { getTopRated(page) }
                )

                val results = deferredResults.awaitAll()
                
                popularMovies = results[0].results
                nowPlayingMovies = results[1].results
                upcomingMovies = results[2].results
                topRatedMovies = results[3].results

                updateUiState()
            } catch (e: Exception) {
                handleError("Erreur lors du chargement des films", e)
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Recherche de films avec debouncing et gestion d'erreur
     */
    fun searchMovies(query: String, page: Int = 1) {
        if (query.isBlank()) {
            searchResults = emptyList()
            return
        }

        viewModelScope.launch {
            isLoading = true
            _errorState.value = null
            
            try {
                val response = searchMoviesUseCase(query, page)
                searchResults = response.results
            } catch (e: Exception) {
                handleError("Erreur lors de la recherche", e)
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Charge les détails d'un film avec cache
     */
    fun loadMovieDetail(id: Int, language: String = "fr-FR") {
        // Vérifier si le film est déjà en cache
        if (movieDetail?.id == id) return

        viewModelScope.launch {
            isLoading = true
            _errorState.value = null
            
            try {
                val detail = getMovieDetails(id, language)
                movieDetail = detail
            } catch (e: Exception) {
                handleError("Erreur lors du chargement des détails", e)
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Charge les films similaires
     */
    fun loadSimilar(movieId: Int, page: Int = 1) {
        viewModelScope.launch {
            isLoading = true
            _errorState.value = null
            
            try {
                val response = getSimilar(movieId, page)
                similarMovies = response.results
            } catch (e: Exception) {
                handleError("Erreur lors du chargement des films similaires", e)
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Charge les crédits d'un film
     */
    fun loadMovieCredits(movieId: Int, language: String = "fr-FR") {
        viewModelScope.launch {
            isLoading = true
            _errorState.value = null
            
            try {
                val credits = getMovieCredits(movieId, language)
                movieCredits = credits.cast
            } catch (e: Exception) {
                handleError("Erreur lors du chargement des crédits", e)
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Charge les détails d'un acteur
     */
    fun loadCastDetails(castId: Int, language: String = "fr-FR") {
        viewModelScope.launch {
            isLoading = true
            _errorState.value = null
            
            try {
                castDetails = getCastDetails(castId, language)
            } catch (e: Exception) {
                handleError("Erreur lors du chargement des détails de l'acteur", e)
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Charge les films par genre
     */
    fun loadByGenre(genreId: Int, page: Int = 1) {
        viewModelScope.launch {
            isLoading = true
            _errorState.value = null
            
            try {
                genreMovies = getByGenre(genreId, page).results
            } catch (e: Exception) {
                handleError("Erreur lors du chargement par genre", e)
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Sélectionne un acteur
     */
    fun selectCast(cast: CastMember) {
        selectedCast = cast
        loadCastDetails(cast.id)
    }

    /**
     * Recherche locale d'un film par ID
     */
    fun findById(id: Int): Movie? {
        return popularMovies
            .plus(nowPlayingMovies)
            .plus(upcomingMovies)
            .plus(topRatedMovies)
            .plus(searchResults)
            .firstOrNull { it.id == id }
    }

    /**
     * Efface l'état d'erreur
     */
    fun clearError() {
        _errorState.value = null
    }

    /**
     * Gestion centralisée des erreurs
     */
    private fun handleError(message: String, exception: Exception) {
        _errorState.value = message
        // Log l'erreur pour le debugging
        println("MoviesViewModel Error: $message - ${exception.message}")
    }

    /**
     * Met à jour l'état UI global
     */
    private fun updateUiState() {
        _uiState.value = _uiState.value.copy(
            popularMovies = popularMovies,
            nowPlayingMovies = nowPlayingMovies,
            upcomingMovies = upcomingMovies,
            topRatedMovies = topRatedMovies,
            isLoading = isLoading
        )
    }
}

/**
 * État UI centralisé
 */
data class MoviesUiState(
    val popularMovies: List<Movie> = emptyList(),
    val nowPlayingMovies: List<Movie> = emptyList(),
    val upcomingMovies: List<Movie> = emptyList(),
    val topRatedMovies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)