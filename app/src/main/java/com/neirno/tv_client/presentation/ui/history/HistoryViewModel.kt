package com.neirno.tv_client.presentation.ui.history

import androidx.lifecycle.ViewModel
import com.neirno.tv_client.domain.entity.History
import com.neirno.tv_client.domain.use_case.history.HistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val historyUseCase: HistoryUseCase
): ViewModel(), ContainerHost<HistoryState, HistorySideEffect> {

    override val container: Container<HistoryState, HistorySideEffect> = container(HistoryState())

    init {
        onEvent(HistoryEvent.GetHistories)
    }

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.GetHistories -> getHistories()
            is HistoryEvent.DeleteHistory -> deleteHistory(event.history)
            is HistoryEvent.DeleteAllHistory -> deleteAllHistory()
        }
    }

    private fun getHistories() = intent {
        historyUseCase.getHistories().collect { histories ->
            reduce { state.copy(histories = histories)  }
        }
    }

    private fun deleteHistory(history: History) = intent {
        historyUseCase.deleteHistory(history.id)
    }

    private fun deleteAllHistory() = intent {
        state.histories.map {
            deleteHistory(it)
        }
    }
}

data class HistoryState(
    val histories: List<History> = emptyList()
)

sealed class HistoryEvent {
    data class DeleteHistory(val history: History) : HistoryEvent()
    object DeleteAllHistory : HistoryEvent()
    object GetHistories : HistoryEvent()
}

sealed class HistorySideEffect