package com.neirno.tv_client.di

import android.content.Context
import androidx.room.Room
import com.neirno.tv_client.data.api.interceptors.DynamicUrlInterceptor
import com.neirno.tv_client.data.data_source.ConnectionDatabase
import com.neirno.tv_client.data.repository.ConnectionRepositoryImpl
import com.neirno.tv_client.domain.repository.ConnectionRepository
import com.neirno.tv_client.domain.use_case.connection.InsertConnection
import com.neirno.tv_client.domain.use_case.connection.CheckAndSaveConnection
import com.neirno.tv_client.domain.use_case.connection.CheckConnection
import com.neirno.tv_client.domain.use_case.connection.ConnectionUseCase
import com.neirno.tv_client.domain.use_case.connection.DeleteConnection
import com.neirno.tv_client.domain.use_case.connection.GetConnection
import com.neirno.tv_client.domain.use_case.connection.GetConnections
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ConnectionModule {

    @Singleton
    @Provides
    fun provideConnectionRepository(db: ConnectionDatabase, retrofit: Retrofit, dynamicUrlInterceptor: DynamicUrlInterceptor): ConnectionRepository {
        return ConnectionRepositoryImpl(retrofit ,db.connectionDao, dynamicUrlInterceptor)
    }

    @Singleton
    @Provides
    fun provideConnectionDatabase(@ApplicationContext appContext: Context): ConnectionDatabase {
        return Room.databaseBuilder(
            appContext,
            ConnectionDatabase::class.java,
            ConnectionDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideConnectionUseCases(
        connectionRepository: ConnectionRepository,
    ): ConnectionUseCase {
        return ConnectionUseCase(
            insertConnection = InsertConnection(connectionRepository),
            deleteConnection = DeleteConnection(connectionRepository),
            getConnection = GetConnection(connectionRepository),
            getConnections = GetConnections(connectionRepository),
            checkAndSaveConnection = CheckAndSaveConnection(connectionRepository),
            checkConnection = CheckConnection(connectionRepository)
        )
    }
}