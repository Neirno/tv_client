package com.neirno.tv_client.domain.use_case.history

import com.neirno.tv_client.domain.entity.History
import com.neirno.tv_client.domain.repository.HistoryRepository

class InsertHistory(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(title: String) {
        historyRepository.saveHistory(History(title = title))
    }
}
