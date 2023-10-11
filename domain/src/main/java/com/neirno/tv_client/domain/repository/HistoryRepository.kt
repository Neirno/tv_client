package com.neirno.tv_client.domain.repository

import com.neirno.tv_client.domain.entity.History
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {

    fun getHistories(): Flow<List<History>>

    suspend fun saveHistory(history: History)

    suspend fun getHistoryById(id: Long): History?

    suspend fun deleteHistory(id: Long)

}