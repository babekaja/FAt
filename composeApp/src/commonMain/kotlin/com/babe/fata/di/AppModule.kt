package com.babe.fata.di

import com.babe.fata.data.MoviesRepository
import com.babe.fata.data.MoviesRepositoryImpl
import com.babe.fata.domaine.usecase.GetCastDetailsUseCase
import com.babe.fata.domaine.usecase.GetMovieCreditsUseCase
import com.babe.fata.domaine.usecase.GetMovieDetailsUseCase
import com.babe.fata.domaine.usecase.GetMoviesByGenreUseCase
import com.babe.fata.domaine.usecase.GetNowPlayingMoviesUseCase
import com.babe.fata.domaine.usecase.GetPopularMoviesUseCase
import com.babe.fata.domaine.usecase.GetSimilarMoviesUseCase
import com.babe.fata.domaine.usecase.GetTopRatedMoviesUseCase
import com.babe.fata.domaine.usecase.GetUpcomingMoviesUseCase
import com.babe.fata.domaine.usecase.SearchMoviesUseCase
import com.babe.fata.ui.viewmodel.MoviesViewModel
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import org.koin.core.module.dsl.singleOf

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    // Ktor HttpClient en singleton
    single<HttpClient> { HttpClient(CIO) }

    // Repository
    single<MoviesRepository> { MoviesRepositoryImpl(get()) }

    // UseCases en singleton via singleOf
    singleOf(::GetPopularMoviesUseCase)
    singleOf(::GetNowPlayingMoviesUseCase)
    singleOf(::GetUpcomingMoviesUseCase)
    singleOf(::GetTopRatedMoviesUseCase)
    singleOf(::SearchMoviesUseCase)
    singleOf(::GetMoviesByGenreUseCase)
    singleOf(::GetMovieDetailsUseCase)
    singleOf(::GetSimilarMoviesUseCase)

    factory { GetMovieCreditsUseCase(repository = get()) }

    factory { GetCastDetailsUseCase(get()) }

    // ViewModel
    viewModel {
        MoviesViewModel(
            getPopular = get(),
            getNowPlaying = get(),
            getUpcoming = get(),
            getTopRated = get(),
            searchMoviesUseCase = get(),
            getByGenre = get(),
            getMovieDetails = get(),
            getSimilar = get(),
            getMovieCredits = get(),
            getCastDetails = get()
        )
    }
}