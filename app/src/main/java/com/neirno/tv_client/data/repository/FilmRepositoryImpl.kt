package com.neirno.tv_client.data.repository

import com.neirno.tv_client.data.api.FilmApiService
import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.data.api.PasswordRequest
import com.neirno.tv_client.data.api.model.toDomain
import com.neirno.tv_client.domain.entity.FilmInfo
import com.neirno.tv_client.domain.repository.FilmRepository
import retrofit2.Retrofit

class FilmRepositoryImpl(private val retrofit: Retrofit) : FilmRepository {

    private val apiService: FilmApiService by lazy {
        retrofit.create(FilmApiService::class.java)
    }

    override suspend fun getCategories(): Result<List<String>> {
        return try {
            val response = apiService.getCategories()
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(Exception("Error fetching categories: ${response.message()}"))
            }
        } catch (e: Throwable) {
            Result.Error(e)
        }
    }

    override suspend fun getFilmsByCategory(category: String): Result<List<String>> {
        return try {
            val response = apiService.getFilmsByCategory(category)
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(Exception("Error fetching films by category: ${response.message()}"))
            }
        } catch (e: Throwable) {
            Result.Error(e)
        }
    }

    override suspend fun playAndGetFilmInfo(category: String, film: String): Result<FilmInfo> {
        return try {
            val response = apiService.playAndGetFilmInfo(category, film)
            if (response.isSuccessful) {
                val filmInfoResponse = response.body() ?: throw Exception("No film info returned")
                Result.Success(filmInfoResponse.toDomain())
            } else {
                Result.Error(Exception("Error fetching film info: ${response.message()}"))
            }
        } catch (e: Throwable) {
            Result.Error(e)
        }
    }

    override suspend fun getPrivateFilms(password: String): Result<List<String>> {
        return try {
            val response = apiService.getPrivateFilms(PasswordRequest(password))
            if (response.isSuccessful) {
                Result.Success(response.body() ?: emptyList())
            } else {
                Result.Error(Exception("Error fetching private films: ${response.message()}"))
            }
        } catch (e: Throwable) {
            Result.Error(e)
        }
    }

    override suspend fun playPrivateFilm(filmName: String): Result<FilmInfo> {
        return try {
            val response = apiService.playPrivateFilm(filmName)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!.toDomain())
            } else {
                Result.Error(Exception(response.message()))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
