package com.neirno.tv_client.core.base

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.neirno.tv_client.core.constants.Tabs
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel(), ContainerHost<MainState, MainSideEffect> {

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
    val currentTab: Int = Tabs.YOUTUBE // по умолчанию открывается вкладка YouTube
)

sealed class MainSideEffect {
    data class UpdateCurrentPage(val currentPage: Int) : MainSideEffect()
}

sealed class MainEvent {
    data class SetCurrentTab(val tab: Int) : MainEvent()
    object RequestCurrentPage : MainEvent()
}
