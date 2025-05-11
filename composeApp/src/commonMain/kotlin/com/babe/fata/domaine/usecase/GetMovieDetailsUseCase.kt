package com.babe.fata.domaine.usecase

import com.babe.fata.data.MoviesRepository
import com.babe.fata.model.Movie

class GetMovieDetailsUseCase(
    private val repo: MoviesRepository
) {
    suspend operator fun invoke(id: Int, language: String = "fr-FR"): Movie {
        return repo.getMovieDetails(id, language)
    }
}
