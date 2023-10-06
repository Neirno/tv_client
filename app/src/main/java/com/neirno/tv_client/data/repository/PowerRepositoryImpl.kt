package com.neirno.tv_client.data.repository

import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.data.api.PowerApiService
import com.neirno.tv_client.domain.repository.PowerRepository
import retrofit2.Retrofit

class PowerRepositoryImpl (private val retrofit: Retrofit): PowerRepository {
    private val apiService: PowerApiService by lazy {
        retrofit.create(PowerApiService::class.java)
    }
    override suspend fun displayOn(): Result<Boolean> = safeApiCall { apiService.displayOn() }

    override suspend fun displayOff(): Result<Boolean> = safeApiCall { apiService.displayOff() }

    override suspend fun lightOn(): Result<Boolean> = safeApiCall { apiService.lightOn() }

    override suspend fun lightOff(): Result<Boolean> = safeApiCall { apiService.lightOff() }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<Boolean> {
        return try {
            apiCall.invoke()
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}