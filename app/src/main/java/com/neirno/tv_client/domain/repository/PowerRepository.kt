package com.neirno.tv_client.domain.repository

import com.neirno.tv_client.core.network.Result

interface PowerRepository {
    suspend fun displayOn(): Result<Boolean>
    suspend fun displayOff(): Result<Boolean>
    suspend fun lightOn(): Result<Boolean>
    suspend fun lightOff(): Result<Boolean>
}