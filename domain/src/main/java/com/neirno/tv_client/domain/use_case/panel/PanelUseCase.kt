package com.neirno.tv_client.domain.use_case.panel

data class PanelUseCases(
    val pause: Pause,
    val resume: Resume,
    val stop: Stop,
    val setVolume: SetVolume,
    val volumeShiftForward: VolumeShiftForward,
    val volumeShiftBackward: VolumeShiftBackward,
    val skipForward: SkipForward,
    val skipBackward: SkipBackward,
    val setTime: SetTime,
    val skipVideo: SkipVideo,
    val getVideoStatus: GetVideoStatus
)
