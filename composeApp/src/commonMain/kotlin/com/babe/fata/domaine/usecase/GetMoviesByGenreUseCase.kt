package com.babe.fata.domaine.usecase



import com.babe.fata.data.MoviesRepository
import com.babe.fata.model.MovieResponse

class GetMoviesByGenreUseCase(
    private val repository: com.babe.fata.data.MoviesRepository
) {
    suspend operator fun invoke(genreId: Int, page: Int = 1): MovieResponse {
        return repository.getByGenre(genreId, page)
    }
}
