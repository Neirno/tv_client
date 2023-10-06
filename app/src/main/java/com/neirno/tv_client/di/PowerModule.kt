package com.neirno.tv_client.di

import com.neirno.tv_client.data.repository.PowerRepositoryImpl
import com.neirno.tv_client.domain.repository.PowerRepository
import com.neirno.tv_client.domain.use_case.power.DisplayOff
import com.neirno.tv_client.domain.use_case.power.DisplayOn
import com.neirno.tv_client.domain.use_case.power.LightOff
import com.neirno.tv_client.domain.use_case.power.LightOn
import com.neirno.tv_client.domain.use_case.power.PowerUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PowerModule {

    @Provides
    @Singleton
    fun providePowerRepository(retrofit: Retrofit) : PowerRepository {
        return PowerRepositoryImpl(retrofit)
    }

    @Provides
    @Singleton
    fun providePowerUseCase(
        powerRepository: PowerRepository
    ): PowerUseCase {
        return PowerUseCase(
            displayOn = DisplayOn(powerRepository),
            displayOff = DisplayOff(powerRepository),
            lightOn = LightOn(powerRepository),
            lightOff = LightOff(powerRepository)
        )
    }
}