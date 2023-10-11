package com.neirno.tv_client.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST

interface ConnectionApiService {
    @POST("/")
    suspend fun checkConnection(): Response<ResponseBody>
}
