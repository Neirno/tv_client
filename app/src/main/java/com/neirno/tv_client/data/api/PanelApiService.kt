package com.neirno.tv_client.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PanelApiService {
    @POST("/pause")
    suspend fun pause() : Response<ResponseBody>

    @POST("/resume")
    suspend fun resume() : Response<ResponseBody>

    @POST("/stop")
    suspend fun stop() : Response<ResponseBody>

    @POST("/set_volume")
    suspend fun setVolume(@Body request: SetPanelData) : Response<ResponseBody>

    @POST("/volume_shift_forward")
    suspend fun volumeShiftForward() : Response<ResponseBody>

    @POST("/volume_shift_backward")
    suspend fun volumeShiftBackward() : Response<ResponseBody>

    @POST("/skip_forward")
    suspend fun skipForward() : Response<ResponseBody>

    @POST("/skip_backward")
    suspend fun skipBackward() : Response<ResponseBody>

    @POST("/set_time")
    suspend fun setTime(@Body request: SetPanelData) : Response<ResponseBody>

    @POST("/skip")
    suspend fun skip() : Response<ResponseBody>
}

data class SetPanelData(
    val data: String
)
