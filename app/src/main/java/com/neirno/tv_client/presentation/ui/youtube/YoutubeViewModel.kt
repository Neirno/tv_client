package com.neirno.tv_client.presentation.ui.youtube

import androidx.lifecycle.ViewModel
import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.entity.Youtube
import com.neirno.tv_client.domain.use_case.youtube.YoutubeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class YoutubeViewModel @Inject constructor(
    private val youtubeUseCase: YoutubeUseCase
): ViewModel(), ContainerHost<YoutubeState, YoutubeSideEffect> {

    override val container: Container<YoutubeState, YoutubeSideEffect> = container(YoutubeState())

    fun onEvent(event: YoutubeEvent) {
        when (event) {
            is YoutubeEvent.GetVideos -> {
                getVideos(event.query)
            }
            is YoutubeEvent.SendVideo -> {
                sendVideo(event.video)
            }
        }
    }

    private fun getVideos(query: String) = intent {
        val offset = 0
        val limit = 5
        val result = youtubeUseCase.searchVideo(query, offset, limit)
        when (result) {
            is Result.Success -> {
                reduce { state.copy(videos = result.data) }
            }
            is Result.Error -> {

            }
        }
    }

    private fun sendVideo(video: Youtube) = intent {
        val result = youtubeUseCase.sendVideoUrl(video)
        when (result) {
            is Result.Success -> {

            }
            is Result.Error -> {

            }
        }
    }
}

data class YoutubeState(
    val videos: List<Youtube> = emptyList(),
    val query: String = "",
    val video: Youtube? = null
)

sealed class YoutubeEvent {
    data class GetVideos(val query: String) : YoutubeEvent()
    data class SendVideo(val video: Youtube) : YoutubeEvent()
}

sealed class YoutubeSideEffect {}