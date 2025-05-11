package com.babe.fata.domaine.usecase

import com.babe.fata.data.MoviesRepository
import com.babe.fata.model.CastDetails
import com.babe.fata.model.CastMember


class GetCastDetailsUseCase(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(castId: Int, language: String = "fr-FR"): CastDetails {
        return repository.getCastDetails(castId, language)
    }
}
