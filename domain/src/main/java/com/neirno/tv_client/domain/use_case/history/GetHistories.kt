package com.neirno.tv_client.domain.use_case.history

import com.neirno.tv_client.domain.entity.History
import kotlinx.coroutines.flow.Flow
import com.neirno.tv_client.domain.repository.HistoryRepository

class GetHistories(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(): Flow<List<History>> {
        return historyRepository.getHistories()
    }
}
