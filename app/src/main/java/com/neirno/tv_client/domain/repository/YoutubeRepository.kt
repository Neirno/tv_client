package com.neirno.tv_client.domain.repository

import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.entity.Youtube


interface YoutubeRepository {
    suspend fun searchVideo(query: String, offset: Int, limit: Int): Result<List<Youtube>>

    suspend fun sendVideoUrl(videoUrl: String): Result<Boolean>
}