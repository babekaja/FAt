package com.babe.fata.domaine.usecase

import com.babe.fata.data.MoviesRepository
import com.babe.fata.model.MovieResponse

/**
 * Cas d’usage pour la recherche de films via TMDb.
 *
 * @property repository instance de MoviesRepository
 */
class SearchMoviesUseCase(
    private val repository: MoviesRepository
) {
    /**
     * Lance une requête de recherche de films.
     *
     * @param query chaîne de recherche
     * @param page numéro de page (par défaut = 1)
     * @return MovieResponse contenant la liste des résultats
     */
    suspend operator fun invoke(query: String, page: Int = 1): MovieResponse {
        return repository.searchMovies(query, page)
    }
}
