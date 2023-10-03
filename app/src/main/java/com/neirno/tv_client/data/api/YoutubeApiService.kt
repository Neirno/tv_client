package com.neirno.tv_client.data.api

import com.neirno.tv_client.data.api.model.YoutubeSearchResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface YoutubeApiService {
    @POST("/search")
    suspend fun searchVideos(@Body request: SearchRequest): Response<List<YoutubeSearchResponse>>

    @POST("/play")
    suspend fun sendVideoUrl(@Body request: PlayRequest): Response<ResponseBody>
}

data class SearchRequest(
    val query: String,
    val offset: Int = 0,
    val limit: Int = 10
)

data class PlayRequest(
    val url: String
)
