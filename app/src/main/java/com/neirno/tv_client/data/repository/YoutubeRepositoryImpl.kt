package com.neirno.tv_client.data.repository

import android.util.Log
import com.neirno.tv_client.data.api.PlayRequest
import com.neirno.tv_client.data.api.SearchRequest
import com.neirno.tv_client.data.api.YoutubeApiService
import com.neirno.tv_client.data.api.model.toDomain
import com.neirno.tv_client.domain.entity.Youtube
import com.neirno.tv_client.domain.repository.YoutubeRepository
import retrofit2.Retrofit
import java.io.IOException
import com.neirno.tv_client.core.network.Result

class YoutubeRepositoryImpl (
    private val retrofit: Retrofit,
): YoutubeRepository {
    private val youtubeApiService = retrofit.create(YoutubeApiService::class.java)

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