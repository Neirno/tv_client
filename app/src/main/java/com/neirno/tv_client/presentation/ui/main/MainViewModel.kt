package com.neirno.tv_client.presentation.ui.main

import androidx.lifecycle.ViewModel
import com.neirno.tv_client.core.constants.Tabs
import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.use_case.power.PowerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val powerUseCase: PowerUseCase
) : ViewModel(), ContainerHost<MainState, MainSideEffect> {

    // Инициализация начального состояния контейнера
    override val container: Container<MainState, MainSideEffect> = container(MainState())

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.SetCurrentTab -> {
                setCurrentTab(event.tab)
            }
            is MainEvent.RequestCurrentPage -> {
                getCurrentPage()
            }
            is MainEvent.DisplayOnEvent -> {
                turnDisplayOn()
            }
            is MainEvent.DisplayOffEvent -> {
                turnDisplayOff()
            }
            is MainEvent.LightOnEvent -> {
                turnLightOn()
            }
            is MainEvent.LightOffEvent -> {
                turnLightOff()
            }
        }
    }

    private fun turnDisplayOn() = intent {
        when (val result = powerUseCase.displayOn.invoke()) {
            is Result.Success -> reduce { state.copy(isDisplayOn = true) }
            is Result.Error -> postSideEffect(
                MainSideEffect.ErrorMessage(
                    result.exception.message ?: "Error turning display on"
                )
            )
        }
    }

    private fun turnDisplayOff() = intent {
        when (val result = powerUseCase.displayOff.invoke()) {
            is Result.Success -> reduce { state.copy(isDisplayOn = false) }
            is Result.Error -> postSideEffect(
                MainSideEffect.ErrorMessage(
                    result.exception.message ?: "Error turning display off"
                )
            )
        }
    }

    private fun turnLightOn() = intent {
        when (val result = powerUseCase.lightOn.invoke()) {
            is Result.Success -> reduce { state.copy(isLightOn = true) }
            is Result.Error -> postSideEffect(
                MainSideEffect.ErrorMessage(
                    result.exception.message ?: "Error turning light on"
                )
            )
        }
    }

    private fun turnLightOff() = intent {
        when (val result = powerUseCase.lightOff.invoke()) {
            is Result.Success -> reduce { state.copy(isLightOn = false) }
            is Result.Error -> postSideEffect(
                MainSideEffect.ErrorMessage(
                    result.exception.message ?: "Error turning light off"
                )
            )
        }
    }


    private fun setCurrentTab(tab: Int) = intent {
        reduce { state.copy(currentTab = tab) }
    }

    private fun getCurrentPage() = intent {
        postSideEffect(MainSideEffect.UpdateCurrentPage(state.currentTab))
    }
}

data class MainState(
    val currentTab: Int = Tabs.YOUTUBE, // по умолчанию открывается вкладка YouTube
    val isDisplayOn: Boolean = false,
    val isLightOn: Boolean = false
)

sealed class MainSideEffect {
    data class UpdateCurrentPage(val currentPage: Int) : MainSideEffect()
    data class ErrorMessage(val message: String) : MainSideEffect()
}

sealed class MainEvent {
    data class SetCurrentTab(val tab: Int) : MainEvent()
    object RequestCurrentPage : MainEvent()
    object DisplayOnEvent : MainEvent()
    object DisplayOffEvent : MainEvent()
    object LightOnEvent : MainEvent()
    object LightOffEvent : MainEvent()
}
