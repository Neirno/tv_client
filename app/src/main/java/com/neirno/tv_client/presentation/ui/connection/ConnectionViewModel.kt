package com.neirno.tv_client.presentation.ui.connection

import androidx.lifecycle.ViewModel
import com.neirno.tv_client.domain.entity.Connection
import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.core.ui.UiStatus
import com.neirno.tv_client.domain.use_case.connection.ConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


@HiltViewModel
class ConnectionViewModel @Inject constructor(
    private val connectionUseCase: ConnectionUseCase
): ViewModel(), ContainerHost<ConnectionState, ConnectionSideEffect> {

    override val container: Container<ConnectionState, ConnectionSideEffect> = container(ConnectionState())

    init {
        onEvent(ConnectionEvent.GetConnections)
    }

    fun onEvent(event: ConnectionEvent) {
        when (event) {
            is ConnectionEvent.EnterConnection -> {
                enterConnection(event.ip)
            }
            is ConnectionEvent.GetConnections -> {
                getConnections()
            }
            is ConnectionEvent.DeleteConnection -> {
                deleteConnection(event.id)
            }
        }
    }

    private fun getConnections() = intent {
            repeatOnSubscription {connectionUseCase.getConnections().collect{ connection ->
                reduce {
                    state.copy(
                        previousConnections = connection,
                        status = UiStatus.Success
                    )
                }
            }
        }
    }

    private fun enterConnection(ip: String) = intent {
        if (ip.isBlank()) {
            postSideEffect(ConnectionSideEffect.ConnectionError("IP адрес пуст"))
            return@intent
        }
        reduce { state.copy(status = UiStatus.Loading) }
        val alreadyExists = state.previousConnections.any { connection -> connection.ip == ip }

        val result = if (alreadyExists) {
            connectionUseCase.checkConnection(ip)
        } else {
            connectionUseCase.checkAndSaveConnection(ip)
        }

        when (result) {
            is Result.Success -> {
                if (result.data) {
                    postSideEffect(ConnectionSideEffect.ConnectionAccept)
                } else {
                    postSideEffect(ConnectionSideEffect.ConnectionError("Ошибка при соединении"))
                }
            }
            is Result.Error -> {
                postSideEffect(ConnectionSideEffect.ConnectionError("Ошибка при соединении: ${result.exception.message}"))
            }
        }

        delay(1000L) // Костыль. Придумать тут надо чета, чтобы перерисовки не было
        reduce { state.copy(status = UiStatus.Success) }
    }


    private fun deleteConnection(id: Long) = intent {
        connectionUseCase.deleteConnection(id)
    }


}

data class ConnectionState(
    val status: UiStatus = UiStatus.Loading,
    val ip: String = "",
    val previousConnections: List<Connection> = emptyList(),
)

sealed class ConnectionEvent {
    object GetConnections : ConnectionEvent()
    data class EnterConnection(val ip: String) : ConnectionEvent()
    data class DeleteConnection(val id: Long) : ConnectionEvent()
}

sealed class ConnectionSideEffect {
    object ConnectionAccept : ConnectionSideEffect()
    data class ConnectionError (val error: String) : ConnectionSideEffect()
}