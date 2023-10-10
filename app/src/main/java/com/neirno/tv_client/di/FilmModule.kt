package com.neirno.tv_client.di

import com.neirno.tv_client.data.repository.FilmRepositoryImpl
import com.neirno.tv_client.domain.repository.FilmRepository
import com.neirno.tv_client.domain.use_case.film.GetCategories
import com.neirno.tv_client.domain.use_case.film.GetFilmsByCategory
import com.neirno.tv_client.domain.use_case.film.FilmUseCase
import com.neirno.tv_client.domain.use_case.film.GetPrivateFilms
import com.neirno.tv_client.domain.use_case.film.PlayAndGetFilmInfo
import com.neirno.tv_client.domain.use_case.film.PlayPrivateFilm
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FilmModule {

    @Provides
    @Singleton
    fun provideFilmRepository(retrofit: Retrofit) : FilmRepository {
        return FilmRepositoryImpl(retrofit)
    }

    @Provides
    @Singleton
    fun provideFilmUseCase(
        filmRepository: FilmRepository
    ): FilmUseCase {
        return FilmUseCase(
            getCategories = GetCategories(filmRepository),
            getFilmsByCategory = GetFilmsByCategory(filmRepository),
            playAndGetFilmInfo = PlayAndGetFilmInfo(filmRepository),
            getPrivateFilms = GetPrivateFilms(filmRepository),
            playPrivateFilm = PlayPrivateFilm(filmRepository)
        )
    }
}
