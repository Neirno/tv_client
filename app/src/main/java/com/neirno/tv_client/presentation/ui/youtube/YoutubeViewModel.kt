package com.neirno.tv_client.presentation.ui.youtube

import android.util.Log
import androidx.lifecycle.ViewModel
import com.neirno.tv_client.core.constants.Limit.PAGE_LIMIT
import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.core.ui.UiStatus
import com.neirno.tv_client.domain.entity.Youtube
import com.neirno.tv_client.domain.entity.YoutubeSearch
import com.neirno.tv_client.domain.use_case.history.HistoryUseCase
import com.neirno.tv_client.domain.use_case.youtube.YoutubeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class YoutubeViewModel @Inject constructor(
    private val youtubeUseCase: YoutubeUseCase,
    private val historyUseCase: HistoryUseCase
): ViewModel(), ContainerHost<YoutubeState, YoutubeSideEffect> {

    override val container: Container<YoutubeState, YoutubeSideEffect> = container(YoutubeState())

    init {
        clearOldQueries()
    }

    fun onEvent(event: YoutubeEvent) {
        when (event) {
            is YoutubeEvent.GetVideos -> getVideos(event.query)
            is YoutubeEvent.SendVideo -> sendVideo(event.video)
            is YoutubeEvent.GetLastQueries -> getLastQueries()
            is YoutubeEvent.SaveQuery -> saveOrUpdateQuery(event.query)
            is YoutubeEvent.DeleteYoutubeSearch -> deleteQuery(event.query)
        }
    }

    private fun getVideos(query: String) = intent {
        // Если уже идет загрузка или достигнут конец списка для текущего запроса, прерываем выполнение
        if (state.status == UiStatus.Loading || (state.hasReachedEndOfList && state.query == query)) return@intent

        // Сбросим состояние при новом запросе
        if (state.query != query) {
            reduce { state.copy(videos = emptyList(), offset = 0, query = query, hasReachedEndOfList = false) }
        }

        reduce { state.copy(status = UiStatus.Loading) }

        val currentOffset = state.offset
        val limit = PAGE_LIMIT

        when (val result = youtubeUseCase.searchVideo(query, currentOffset, limit)) {
            is Result.Success -> {
                if (result.data.isEmpty() || result.data.size < limit) {
                    reduce { state.copy(hasReachedEndOfList = true, status = UiStatus.Success) }
                } else {
                    reduce {
                        state.copy(
                            videos = state.videos + result.data,
                            status = UiStatus.Success,
                            offset = currentOffset + 5
                        )
                    }
                }
            }
            is Result.Error -> {
                reduce { state.copy(status = UiStatus.Failed("Произашла ошибка.")) }
                Log.i("Error VM (get)", result.exception.message.toString())
            }
        }
    }

    private fun sendVideo(video: Youtube) = intent {
        when (val result = youtubeUseCase.sendVideoUrl(video)) {
            is Result.Success -> {
                postSideEffect(YoutubeSideEffect.VideoIsSend(video.title))
                historyUseCase.insertHistory(video.title)
            }
            is Result.Error -> {
                postSideEffect(YoutubeSideEffect.ErrorMessage("Произашла ошибка при отправке видео."))
                Log.i("Error VM (send)", result.exception.message.toString())
            }
        }
    }

    private fun saveOrUpdateQuery(query: String) = intent {
        val youtubeSearch = YoutubeSearch(query = query)
        youtubeUseCase.insertYoutubeSearch(youtubeSearch)
        getLastQueries()
    }

    private fun getLastQueries() = intent {
        youtubeUseCase.getYoutubeSearches().collect { searchList ->
            val queries = searchList.take(5)
            reduce { state.copy(lastQueries = queries) }
        }
    }


    private fun clearOldQueries() = intent {
        youtubeUseCase.deleteOldYoutubeSearchies()
    }

    private fun deleteQuery(youtubeSearch: YoutubeSearch) = intent {
        youtubeUseCase.deleteYoutubeSearch(youtubeSearch)
    }
}

data class YoutubeState(
    val videos: List<Youtube> = emptyList(),
    val offset: Int = 0,
    val query: String = "",
    val video: Youtube? = null,
    val status: UiStatus = UiStatus.Success,
    val hasReachedEndOfList: Boolean = false,
    val lastQueries: List<YoutubeSearch> = emptyList(),
)

sealed class YoutubeEvent {
    data class GetVideos(val query: String) : YoutubeEvent()
    data class SendVideo(val video: Youtube) : YoutubeEvent()
    data class SaveQuery(val query: String) : YoutubeEvent()
    data class DeleteYoutubeSearch(val query: YoutubeSearch) : YoutubeEvent()
    object GetLastQueries : YoutubeEvent()
}

sealed class YoutubeSideEffect {
    data class VideoIsSend(val title: String) : YoutubeSideEffect()
    data class ErrorMessage(val msg: String) : YoutubeSideEffect()
}