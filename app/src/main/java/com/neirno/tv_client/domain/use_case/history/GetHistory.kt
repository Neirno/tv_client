package com.neirno.tv_client.domain.use_case.history

import com.neirno.tv_client.domain.entity.History
import com.neirno.tv_client.domain.repository.HistoryRepository

class GetHistory(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(id: Long): History? {
        return historyRepository.getHistoryById(id)
    }
}
