package com.neirno.tv_client.data.api

import com.neirno.tv_client.data.api.model.FilmInfoResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface FilmApiService {

    @POST("/search_film")
    suspend fun getCategories(): Response<List<String>>

    @POST("/search_film/{category}")
    suspend fun getFilmsByCategory(
        @Path("category") category: String
    ): Response<List<String>>

    @POST("/search_film/{category}/{film}")
    suspend fun playAndGetFilmInfo(
        @Path("category") category: String,
        @Path("film") film: String
    ): Response<FilmInfoResponse>

}
