package com.babe.fata.domaine.usecase

import com.babe.fata.data.MoviesRepository
import com.babe.fata.model.MovieResponse


class GetPopularMoviesUseCase(
    private val repo: MoviesRepository
) {
    suspend operator fun invoke(page: Int = 1): MovieResponse =
     repo.getPopular(page)
}