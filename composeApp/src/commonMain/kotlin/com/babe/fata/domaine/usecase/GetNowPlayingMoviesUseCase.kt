package com.babe.fata.domaine.usecase


import com.babe.fata.data.MoviesRepository
import com.babe.fata.model.MovieResponse


class GetNowPlayingMoviesUseCase(
    private val repository: MoviesRepository
) {
    suspend operator fun invoke(page: Int = 1): MovieResponse =
        repository.getNowPlaying(page)
}
