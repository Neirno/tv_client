package com.neirno.tv_client.domain.repository

import com.neirno.tv_client.core.network.Result

interface PanelRepository {
    suspend fun pause(): Result<Boolean>
    suspend fun resume(): Result<Boolean>
    suspend fun stop(): Result<Boolean>
    suspend fun setVolume(volume: String): Result<Boolean>
    suspend fun volumeShiftForward(): Result<Boolean>
    suspend fun volumeShiftBackward(): Result<Boolean>
    suspend fun skipForward(): Result<Boolean>
    suspend fun skipBackward(): Result<Boolean>
    suspend fun setTime(time: String): Result<Boolean>
    suspend fun skip() : Result<Boolean>
}