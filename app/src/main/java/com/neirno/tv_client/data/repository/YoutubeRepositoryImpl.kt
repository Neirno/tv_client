package com.neirno.tv_client.data.repository

import com.neirno.tv_client.data.api.SearchRequest
import com.neirno.tv_client.data.api.YoutubeApiService
import com.neirno.tv_client.data.api.model.toDomain
import com.neirno.tv_client.domain.entity.Youtube
import com.neirno.tv_client.domain.repository.YoutubeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class YoutubeRepositoryImpl (
    private val retrofit: Retrofit,
): YoutubeRepository {
    override fun getYoutube(query: String): Flow<List<Youtube>> = flow {
        val youtubeApiService = retrofit.create(YoutubeApiService::class.java)

        val response = youtubeApiService.searchVideos(SearchRequest(query = query))

        if (response.isSuccessful) {
            val youtubeResponses = response.body() ?: emptyList()
            val youtubes = youtubeResponses.map { it.toDomain() }
            emit(youtubes)
        } else {
            // Обработка ошибки
            throw IOException("Ошибка при получении данных с сервера: ${response.errorBody()?.string()}")
        }
    }



    override suspend fun sendVideo(videoUrl: String): Boolean {
        TODO("Not yet implemented")
    }
}