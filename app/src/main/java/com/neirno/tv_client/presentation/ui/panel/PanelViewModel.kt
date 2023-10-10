package com.neirno.tv_client.presentation.ui.panel

import androidx.lifecycle.ViewModel
import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.entity.VideoStatus
import com.neirno.tv_client.domain.use_case.panel.PanelUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PanelViewModel @Inject constructor(
    private val panelUseCases: PanelUseCases
) : ViewModel(), ContainerHost<PanelState, PanelSideEffect> {

    override val container: Container<PanelState, PanelSideEffect> = container(PanelState())

    fun onEvent(event: PanelEvent) {
        when (event) {
            is PanelEvent.Pause -> pause()
            is PanelEvent.Resume -> resume()
            is PanelEvent.Stop -> stop()
            is PanelEvent.SetVolume -> setVolume(event.volume)
            is PanelEvent.VolumeShiftForward -> volumeShiftForward()
            is PanelEvent.VolumeShiftBackward -> volumeShiftBackward()
            is PanelEvent.SkipForward -> skipForward()
            is PanelEvent.SkipBackward -> skipBackward()
            is PanelEvent.SetTime -> setTime(event.h, event.m, event.s)
            is PanelEvent.SkipVideo -> skipVideo()
            is PanelEvent.GetStatus -> getStatus()
        }
    }

    private fun pause() = intent {
        when (val result = panelUseCases.pause()) {
            is Result.Success -> reduce { state.copy(isPaused = true) }
            is Result.Error -> postSideEffect(PanelSideEffect.ErrorMessage(result.exception.message ?: "Error pausing"))
        }
    }

    private fun resume() = intent {
        when (val result = panelUseCases.resume()) {
            is Result.Success -> reduce { state.copy(isPaused = false) }
            is Result.Error -> postSideEffect(PanelSideEffect.ErrorMessage(result.exception.message ?: "Error resuming"))
        }
    }

    private fun stop() = intent {
        when (val result = panelUseCases.stop()) {
            is Result.Success -> reduce { state.copy(isStopped = true, isPaused = false) }
            is Result.Error -> postSideEffect(PanelSideEffect.ErrorMessage(result.exception.message ?: "Error stopping"))
        }
    }

    private fun setVolume(volume: String) = intent {
        val intVolume = volume.toIntOrNull() ?: 0

        when (val result = panelUseCases.setVolume(intVolume)) {
            is Result.Success -> reduce { state.copy(volumeSet = true) }
            is Result.Error -> postSideEffect(PanelSideEffect.ErrorMessage(result.exception.message ?: "Error setting volume"))
        }
    }

    private fun volumeShiftForward() = intent {
        when (val result = panelUseCases.volumeShiftForward()) {
            is Result.Success -> reduce { state.copy(volumeShifted = "forward") }
            is Result.Error -> postSideEffect(PanelSideEffect.ErrorMessage(result.exception.message ?: "Error shifting volume forward"))
        }
    }

    private fun volumeShiftBackward() = intent {
        when (val result = panelUseCases.volumeShiftBackward()) {
            is Result.Success -> reduce { state.copy(volumeShifted = "backward") }
            is Result.Error -> postSideEffect(PanelSideEffect.ErrorMessage(result.exception.message ?: "Error shifting volume backward"))
        }
    }

    private fun skipForward() = intent {
        when (val result = panelUseCases.skipForward()) {
            is Result.Success -> reduce { state.copy(skipped = "forward") }
            is Result.Error -> postSideEffect(PanelSideEffect.ErrorMessage(result.exception.message ?: "Error skipping forward"))
        }
    }

    private fun skipBackward() = intent {
        when (val result = panelUseCases.skipBackward()) {
            is Result.Success -> reduce { state.copy(skipped = "backward") }
            is Result.Error -> postSideEffect(PanelSideEffect.ErrorMessage(result.exception.message ?: "Error skipping backward"))
        }
    }

    private fun setTime(h: String, m: String, s: String) = intent {
        val intHours = h.toIntOrNull() ?: 0
        val intMinutes = m.toIntOrNull() ?: 0
        val intSeconds = s.toIntOrNull() ?: 0

        when (val result = panelUseCases.setTime(intHours, intMinutes, intSeconds)) {
            is Result.Success -> reduce { state.copy(timeSet = true) }
            is Result.Error -> postSideEffect(PanelSideEffect.ErrorMessage(result.exception.message ?: "Error setting time"))
        }
    }

    private fun skipVideo() = intent {
        when (val result = panelUseCases.skipVideo()) {
            is Result.Success -> reduce { state.copy(skipVideo = true) }
            is Result.Error -> postSideEffect(PanelSideEffect.ErrorMessage(result.exception.message ?: "Error skipping video"))
        }
    }

    private fun getStatus() = intent {
        when (val result = panelUseCases.getVideoStatus()) {
            is Result.Success -> {
                val status = result.data
                //postSideEffect(PanelSideEffect.StatusMessage(status))
                reduce { state.copy(videoStatus = status) }
            }
            is Result.Error -> postSideEffect(PanelSideEffect.ErrorMessage(result.exception.message ?: "Error getting status"))
        }
    }

}

data class PanelState(
    val isPaused: Boolean = false,
    val isStopped: Boolean = false,
    val volumeSet: Boolean = false,
    val volumeShifted: String? = null, // can be "forward" or "backward"
    val skipped: String? = null, // can be "forward" or "backward"
    val timeSet: Boolean = false,
    val skipVideo: Boolean = false,
    val videoStatus: VideoStatus? = null
)

sealed class PanelEvent {
    object Pause : PanelEvent()
    object Resume : PanelEvent()
    object Stop : PanelEvent()
    data class SetVolume(val volume: String) : PanelEvent()
    object VolumeShiftForward : PanelEvent()
    object VolumeShiftBackward : PanelEvent()
    object SkipForward : PanelEvent()
    object SkipBackward : PanelEvent()
    data class SetTime(val h: String, val m: String, val s: String) : PanelEvent()
    object SkipVideo : PanelEvent()
    object GetStatus : PanelEvent()
}

sealed class PanelSideEffect {
    data class StatusMessage(val status: VideoStatus) : PanelSideEffect()
    data class ErrorMessage(val message: String) : PanelSideEffect()
}
