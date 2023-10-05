package com.neirno.tv_client.data.repository

import com.neirno.tv_client.data.api.PanelApiService
import com.neirno.tv_client.domain.repository.PanelRepository
import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.data.api.SetPanelData
import retrofit2.Retrofit

class PanelRepositoryImpl(private val retrofit: Retrofit) : PanelRepository {

    private val apiService: PanelApiService by lazy {
        retrofit.create(PanelApiService::class.java)
    }

    override suspend fun pause() = safeApiCall { apiService.pause() }
    override suspend fun resume() = safeApiCall { apiService.resume() }
    override suspend fun stop() = safeApiCall { apiService.stop() }
    override suspend fun setVolume(volume: String) = safeApiCall { apiService.setVolume(SetPanelData(volume)) }
    override suspend fun volumeShiftForward() = safeApiCall { apiService.volumeShiftForward() }
    override suspend fun volumeShiftBackward() = safeApiCall { apiService.volumeShiftBackward() }
    override suspend fun skipForward() = safeApiCall { apiService.skipForward() }
    override suspend fun skipBackward() = safeApiCall { apiService.skipBackward() }
    override suspend fun setTime(time: String) = safeApiCall { apiService.setTime(SetPanelData(time)) }
    override suspend fun skip() = safeApiCall { apiService.skip() }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<Boolean> {
        return try {
            apiCall.invoke()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}





