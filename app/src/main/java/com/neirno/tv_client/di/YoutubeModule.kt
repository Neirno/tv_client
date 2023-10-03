package com.neirno.tv_client.di

import com.neirno.tv_client.data.repository.YoutubeRepositoryImpl
import com.neirno.tv_client.domain.repository.YoutubeRepository
import com.neirno.tv_client.domain.use_case.youtube.SearchVideo
import com.neirno.tv_client.domain.use_case.youtube.SendVideoUrl
import com.neirno.tv_client.domain.use_case.youtube.YoutubeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object YoutubeModule {

    @Singleton
    @Provides
    fun provideYoutubeRepository(retrofit: Retrofit): YoutubeRepository {
        return YoutubeRepositoryImpl(retrofit)
    }

    @Singleton
    @Provides
    fun provideYoutubeUseCase(
        youtubeRepository: YoutubeRepository
    ): YoutubeUseCase {
        return YoutubeUseCase(
            searchVideo = SearchVideo(youtubeRepository),
            sendVideoUrl = SendVideoUrl(youtubeRepository)
        )
    }
}