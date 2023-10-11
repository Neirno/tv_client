package com.neirno.tv_client.domain.use_case.history

data class HistoryUseCase (
    val insertHistory: InsertHistory,
    val getHistory: GetHistory,
    val getHistories: GetHistories,
    val deleteHistory: DeleteHistory
)
