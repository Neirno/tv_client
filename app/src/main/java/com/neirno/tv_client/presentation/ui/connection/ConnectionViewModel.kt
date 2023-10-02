package com.neirno.tv_client.presentation.ui.connection

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.neirno.tv_client.core.extension.showToast
import com.neirno.tv_client.domain.entity.Connection

import com.neirno.tv_client.domain.use_case.connection.ConnectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        getConnections()
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
            is ConnectionEvent.Loading -> {
                changeLoading()
            }
        }
    }

    private fun changeLoading() = intent {
        reduce { state.copy(loading = !state.loading) }
    }

    private fun getConnections() = intent {
            repeatOnSubscription {connectionUseCase.getConnections().collect{ connection ->
                reduce {
                    state.copy(oldIP = connection)
                }
            }
        }
    }

    private fun enterConnection(ip: String) = intent {
        if (ip.isBlank()) {
            postSideEffect(ConnectionSideEffect.ConnectionError("IP адрес пуст"))
            return@intent
        }
        reduce { state.copy(loading = true) }
        val alreadyExists = state.oldIP.any { connection -> connection.ip == ip }

        val success = if (alreadyExists) {
            connectionUseCase.checkConnection(ip)
        } else {
            connectionUseCase.checkAndSaveConnection(ip)
        }

        if (success) {
            postSideEffect(ConnectionSideEffect.ConnectionAccept)
        } else {
            postSideEffect(ConnectionSideEffect.ConnectionError("Ошибка при соединении"))
        }
        delay(1000L) // Костыль. Придумать тут надо чета, чтобы перерисовки не было
        reduce { state.copy(loading = false) }

    }

    private fun deleteConnection(id: Long) = intent {
        connectionUseCase.deleteConnection(id)
    }


}

data class ConnectionState(
    val loading: Boolean = false,
    val ip: String = "",
    val oldIP: List<Connection> = emptyList(),
)

sealed class ConnectionEvent {
    object GetConnections : ConnectionEvent()
    object Loading : ConnectionEvent ()
    data class EnterConnection(val ip: String) : ConnectionEvent()
    data class DeleteConnection(val id: Long) : ConnectionEvent()
}

sealed class ConnectionSideEffect {
    object ConnectionAccept : ConnectionSideEffect()
    data class ConnectionError (val error: String) : ConnectionSideEffect()
}