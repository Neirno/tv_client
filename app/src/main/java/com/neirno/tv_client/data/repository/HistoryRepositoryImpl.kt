package com.neirno.tv_client.data.repository

import com.neirno.tv_client.core.extension.toData
import com.neirno.tv_client.core.extension.toDomain
import com.neirno.tv_client.data.data_source.HistoryDao
import com.neirno.tv_client.domain.entity.History as DomainHistory
import com.neirno.tv_client.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HistoryRepositoryImpl(
    private val historyDao: HistoryDao
) : HistoryRepository {

    override fun getHistories(): Flow<List<DomainHistory>> {
        return historyDao.getHistories()
            .map { dataList -> dataList.map { it.toDomain() } }
    }

    override suspend fun saveHistory(history: DomainHistory) {
        historyDao.insertHistory(history.toData())
    }

    override suspend fun getHistoryById(id: Long): DomainHistory? {
        val dataHistory = historyDao.getHistoryById(id)
        return dataHistory?.toDomain()
    }

    override suspend fun deleteHistory(id: Long) {
        return historyDao.deleteHistory(id)
    }
}
