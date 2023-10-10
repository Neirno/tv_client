package com.neirno.tv_client.di

import android.content.Context
import androidx.room.Room
import com.neirno.tv_client.data.data_source.ConnectionDatabase
import com.neirno.tv_client.data.data_source.HistoryDatabase
import com.neirno.tv_client.data.repository.HistoryRepositoryImpl
import com.neirno.tv_client.domain.repository.HistoryRepository
import com.neirno.tv_client.domain.use_case.history.DeleteHistory
import com.neirno.tv_client.domain.use_case.history.GetHistories
import com.neirno.tv_client.domain.use_case.history.GetHistory
import com.neirno.tv_client.domain.use_case.history.HistoryUseCase
import com.neirno.tv_client.domain.use_case.history.InsertHistory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HistoryModule {

    @Singleton
    @Provides
    fun provideHistoryRepository(db: HistoryDatabase): HistoryRepository {
        return HistoryRepositoryImpl(db.historyDao)
    }

    @Singleton
    @Provides
    fun provideHistoryDatabase(@ApplicationContext appContext: Context): HistoryDatabase {
        return Room.databaseBuilder(
            appContext,
            HistoryDatabase::class.java,
            HistoryDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideHistoryUseCases(
        historyRepository: HistoryRepository
    ): HistoryUseCase {
        return HistoryUseCase(
            insertHistory = InsertHistory(historyRepository),
            getHistory = GetHistory(historyRepository),
            getHistories = GetHistories(historyRepository),
            deleteHistory = DeleteHistory(historyRepository)
        )
    }
}