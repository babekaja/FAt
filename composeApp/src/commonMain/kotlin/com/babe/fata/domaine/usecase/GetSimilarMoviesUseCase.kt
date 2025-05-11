package com.babe.fata.domaine.usecase

import com.babe.fata.data.MoviesRepository
import com.babe.fata.model.MovieResponse

class GetSimilarMoviesUseCase(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(movieId: Int, page: Int = 1): MovieResponse {
        return repository.getSimilarMovies(movieId, page)
    }
}