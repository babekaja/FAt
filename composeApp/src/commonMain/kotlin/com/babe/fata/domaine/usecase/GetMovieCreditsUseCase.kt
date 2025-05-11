package com.babe.fata.domaine.usecase

import com.babe.fata.data.MoviesRepository
import com.babe.fata.model.CreditsResponse

class GetMovieCreditsUseCase(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(movieId: Int, language: String = "fr-FR"): CreditsResponse =
        repository.getMovieCredits(movieId, language)
}