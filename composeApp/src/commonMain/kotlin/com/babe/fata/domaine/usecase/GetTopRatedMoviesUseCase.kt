// src/commonMain/kotlin/com/babe/fata/domaine/usecase/GetTopRatedMoviesUseCase.kt
package com.babe.fata.domaine.usecase

import com.babe.fata.data.MoviesRepository
import com.babe.fata.model.MovieResponse

class GetTopRatedMoviesUseCase(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(page: Int = 1): MovieResponse =
        repository.getTopRated(page)
}
