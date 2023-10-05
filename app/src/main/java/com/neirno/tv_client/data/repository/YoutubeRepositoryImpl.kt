package com.neirno.tv_client.data.repository

import android.util.Log
import com.neirno.tv_client.core.extension.toData
import com.neirno.tv_client.core.extension.toDomain
import com.neirno.tv_client.data.api.PlayRequest
import com.neirno.tv_client.data.api.SearchRequest
import com.neirno.tv_client.data.api.YoutubeApiService
import com.neirno.tv_client.data.api.model.toDomain
import com.neirno.tv_client.domain.entity.Youtube
import com.neirno.tv_client.domain.repository.YoutubeRepository
import retrofit2.Retrofit
import java.io.IOException
import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.data.data_source.YoutubeSearchDao
import com.neirno.tv_client.domain.entity.YoutubeSearch as DomainYoutubeSearch
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class YoutubeRepositoryImpl (
    private val retrofit: Retrofit,
    private val youtubeSearchDao: YoutubeSearchDao
): YoutubeRepository {
    private val youtubeApiService = retrofit.create(YoutubeApiService::class.java)
    override fun getYoutubeSearches(): Flow<List<DomainYoutubeSearch>> {
        return youtubeSearchDao.getYoutubeSearches()
            .map { dataList -> dataList.map { it.toDomain() } }
    }

    override suspend fun saveYoutubeSearch(youtubeSearch: DomainYoutubeSearch) {
        youtubeSearchDao.insertYoutubeSearch(youtubeSearch.toData())
    }

    override suspend fun getYoutubeSearchById(id: Long): DomainYoutubeSearch? {
        val dataYoutubeSearch = youtubeSearchDao.getYoutubeSearchById(id)
        return dataYoutubeSearch?.toDomain()
    }

    override suspend fun deleteYoutubeSearch(id: Long) {
        return youtubeSearchDao.deleteYoutubeSearch(id)
    }

    override suspend fun deleteOldQueries(limit: Int) {
        youtubeSearchDao.deleteOldQueries(limit)
    }


    override suspend fun searchVideo(query: String, offset: Int, limit: Int): Result<List<Youtube>> {
        return try {
            Log.i("INFO OF IP!!!!!", retrofit.baseUrl().host)
            val response = youtubeApiService.searchVideos(SearchRequest(query, offset, limit))
            if (response.isSuccessful) {
                val youtubeResponses = response.body() ?: emptyList()
                Log.d("API_RESPONSE", youtubeResponses.toString())

                val youtubeList = youtubeResponses.mapNotNull { youtubeResponse ->
                    try {
                        youtubeResponse.toDomain()
                    } catch (e: Exception) {
                        Log.e("Mapping error", "Failed to map object: $youtubeResponse", e)
                        null // Возвращает null, чтобы исключить ошибочный объект из результата
                    }
                }
                Result.Success(youtubeList)
            } else {
                Result.Error(Throwable(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Log.e("Youtube video error", e.toString())
            Result.Error(e)
        }
    }


    override suspend fun sendVideoUrl(videoUrl: String): Result<Boolean> {
        return try {
            val response = youtubeApiService.sendVideoUrl(PlayRequest(url = videoUrl))
            Log.i("url", videoUrl)
            if (response.isSuccessful) {
                Result.Success(true)
            } else {
                Result.Error(IOException("Ошибка при отправке url видео на сервер: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}