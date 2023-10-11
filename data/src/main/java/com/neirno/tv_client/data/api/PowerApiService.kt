package com.neirno.tv_client.data.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST

interface PowerApiService {
    @POST("/display_on")
    suspend fun displayOn() : Response<ResponseBody>

    @POST("/display_off")
    suspend fun displayOff() : Response<ResponseBody>

    @POST("/light_on")
    suspend fun lightOn() : Response<ResponseBody>

    @POST("/light_off")
    suspend fun lightOff() : Response<ResponseBody>
}