package com.neirno.tv_client.domain.repository

import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.entity.FilmInfo

interface FilmRepository {

    suspend fun getCategories(): Result<List<String>>

    suspend fun getFilmsByCategory(category: String): Result<List<String>>

    suspend fun playAndGetFilmInfo(category: String, film: String): Result<FilmInfo>
}