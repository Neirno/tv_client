package com.neirno.tv_client.domain.use_case.history

import com.neirno.tv_client.domain.repository.HistoryRepository

class DeleteHistory(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(id: Long) {
        historyRepository.deleteHistory(id)
    }
}
