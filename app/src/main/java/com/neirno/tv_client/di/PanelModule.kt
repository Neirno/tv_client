package com.neirno.tv_client.di

import com.neirno.tv_client.data.repository.PanelRepositoryImpl
import com.neirno.tv_client.domain.repository.PanelRepository
import com.neirno.tv_client.domain.use_case.panel.GetVideoStatus
import com.neirno.tv_client.domain.use_case.panel.PanelUseCases
import com.neirno.tv_client.domain.use_case.panel.Pause
import com.neirno.tv_client.domain.use_case.panel.Resume
import com.neirno.tv_client.domain.use_case.panel.SetTime
import com.neirno.tv_client.domain.use_case.panel.SetVolume
import com.neirno.tv_client.domain.use_case.panel.SkipVideo
import com.neirno.tv_client.domain.use_case.panel.SkipBackward
import com.neirno.tv_client.domain.use_case.panel.SkipForward
import com.neirno.tv_client.domain.use_case.panel.Stop
import com.neirno.tv_client.domain.use_case.panel.VolumeShiftBackward
import com.neirno.tv_client.domain.use_case.panel.VolumeShiftForward
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PanelModule {

    @Provides
    @Singleton
    fun providePanelRepository(retrofit: Retrofit) : PanelRepository {
        return PanelRepositoryImpl(retrofit)
    }
    @Provides
    @Singleton
    fun providePanelUseCase(
        panelRepository: PanelRepository
    ): PanelUseCases {
        return PanelUseCases(
            pause = Pause(panelRepository),
            resume = Resume(panelRepository),
            stop = Stop(panelRepository),
            setVolume = SetVolume(panelRepository),
            volumeShiftForward = VolumeShiftForward(panelRepository),
            volumeShiftBackward = VolumeShiftBackward(panelRepository),
            skipForward = SkipForward(panelRepository),
            skipBackward = SkipBackward(panelRepository),
            setTime = SetTime(panelRepository),
            skipVideo = SkipVideo(panelRepository),
            getVideoStatus = GetVideoStatus(panelRepository)
        )
    }


}