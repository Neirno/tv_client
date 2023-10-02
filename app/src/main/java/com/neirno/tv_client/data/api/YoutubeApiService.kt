package com.neirno.tv_client.data.api

import com.neirno.tv_client.data.api.model.YoutubeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface YoutubeApiService {
    @POST("/search")
    suspend fun searchVideos(@Body request: SearchRequest): Response<List<YoutubeResponse>>
}

data class SearchRequest(
    val query: String,
    val offset: Int = 0,
    val limit: Int = 10
)
