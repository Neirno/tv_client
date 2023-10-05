package com.neirno.tv_client.di

import android.content.Context
import androidx.room.Room
import com.neirno.tv_client.data.data_source.YoutubeSearchDatabase
import com.neirno.tv_client.data.repository.YoutubeRepositoryImpl
import com.neirno.tv_client.domain.repository.YoutubeRepository
import com.neirno.tv_client.domain.use_case.youtube.DeleteOldYoutubeSearchies
import com.neirno.tv_client.domain.use_case.youtube.DeleteYoutubeSearch
import com.neirno.tv_client.domain.use_case.youtube.GetYoutubeSearch
import com.neirno.tv_client.domain.use_case.youtube.GetYoutubeSearches
import com.neirno.tv_client.domain.use_case.youtube.InsertYoutubeSearch
import com.neirno.tv_client.domain.use_case.youtube.SearchVideo
import com.neirno.tv_client.domain.use_case.youtube.SendVideoUrl
import com.neirno.tv_client.domain.use_case.youtube.YoutubeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object YoutubeModule {

    @Singleton
    @Provides
    fun provideYoutubeRepository(retrofit: Retrofit, db: YoutubeSearchDatabase): YoutubeRepository {
        return YoutubeRepositoryImpl(retrofit, db.youtubeSearchDao)
    }

    @Singleton
    @Provides
    fun provideConnectionDatabase(@ApplicationContext appContext: Context): YoutubeSearchDatabase {
        return Room.databaseBuilder(
            appContext,
            YoutubeSearchDatabase::class.java,
            YoutubeSearchDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideYoutubeUseCase(
        youtubeRepository: YoutubeRepository
    ): YoutubeUseCase {
        return YoutubeUseCase(
            searchVideo = SearchVideo(youtubeRepository),
            sendVideoUrl = SendVideoUrl(youtubeRepository),
            insertYoutubeSearch = InsertYoutubeSearch(youtubeRepository),
            getYoutubeSearches = GetYoutubeSearches(youtubeRepository),
            getYoutubeSearch = GetYoutubeSearch(youtubeRepository),
            deleteYoutubeSearch = DeleteYoutubeSearch(youtubeRepository),
            deleteOldYoutubeSearchies = DeleteOldYoutubeSearchies(youtubeRepository)
        )
    }

}