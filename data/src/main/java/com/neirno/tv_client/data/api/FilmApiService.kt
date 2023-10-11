package com.neirno.tv_client.data.api

import com.neirno.tv_client.data.api.model.FilmInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface FilmApiService {

    @POST("/search_film")
    suspend fun getCategories(): Response<List<String>>

    @POST("/search_private_film")
    suspend fun getPrivateFilms(@Body password: PasswordRequest): Response<List<String>>

    @POST("/play_private_film/{film}")
    suspend fun playPrivateFilm(@Path("film") filmName: String): Response<FilmInfoResponse>


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

data class PasswordRequest(val password: String)

