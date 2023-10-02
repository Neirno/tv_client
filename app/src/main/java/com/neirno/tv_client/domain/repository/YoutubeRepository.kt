package com.neirno.tv_client.domain.repository

import com.neirno.tv_client.domain.entity.Youtube
import kotlinx.coroutines.flow.Flow

interface YoutubeRepository {
    fun getYoutube(query: String): Flow<List<Youtube>>

    suspend fun sendVideo(videoUrl: String): Boolean
}