package com.neirno.tv_client.domain.repository

import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.entity.Youtube
import com.neirno.tv_client.domain.entity.YoutubeSearch
import kotlinx.coroutines.flow.Flow


interface YoutubeRepository {

    fun getYoutubeSearches(): Flow<List<YoutubeSearch>>

    suspend fun saveYoutubeSearch(youtubeSearch: YoutubeSearch)

    suspend fun getYoutubeSearchById(id: Long): YoutubeSearch?

    suspend fun deleteYoutubeSearch(id: Long)

    suspend fun deleteOldQueries(limit: Int = 50)

    suspend fun searchVideo(query: String, offset: Int, limit: Int): Result<List<Youtube>>

    suspend fun sendVideoUrl(videoUrl: String): Result<Boolean>
}