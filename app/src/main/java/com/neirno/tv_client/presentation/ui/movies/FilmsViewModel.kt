package com.neirno.tv_client.presentation.ui.movies

import androidx.lifecycle.ViewModel
import com.neirno.tv_client.core.ui.UiStatus
import com.neirno.tv_client.core.network.Result
import com.neirno.tv_client.domain.entity.FilmInfo
import com.neirno.tv_client.domain.use_case.film.FilmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class FilmsViewModel @Inject constructor(
    private val filmsUseCase: FilmUseCase
) : ViewModel(), ContainerHost<FilmsState, FilmsSideEffect> {

    override val container: Container<FilmsState, FilmsSideEffect> = container(FilmsState.DisplayCategories())

    init {
        onEvent(FilmsEvent.LoadCategories)
    }

    fun onEvent(event: FilmsEvent) {
        when (event) {
            is FilmsEvent.LoadCategories -> loadCategories()
            is FilmsEvent.CategorySelected -> showFilms(event.category)
            is FilmsEvent.FilmSelected -> showFilmDetails(event.film)
            is FilmsEvent.BackPressed -> backToCategories()
            is FilmsEvent.SendFilm -> sendFilm(event.category, event.film)
            is FilmsEvent.LoadPrivateFilms -> loadPrivateFilms(event.password)
            is FilmsEvent.PlayPrivateFilm -> playPrivateFilm(event.filmName)
        }
    }

    private fun loadCategories() = intent {
        when (val result = filmsUseCase.getCategories()) {
            is Result.Success -> {
                reduce { FilmsState.DisplayCategories(status = UiStatus.Success, categories = result.data) }
            }
            is Result.Error -> {
                postSideEffect(FilmsSideEffect.Error(result.exception.message ?: "Unknown error"))
                reduce { FilmsState.DisplayCategories(status = UiStatus.Failed("Ошибка вывода категорий")) }
            }
        }
    }

    private fun showFilms(category: String) = intent {
        when (val result = filmsUseCase.getFilmsByCategory(category)) {
            is Result.Success -> {
                reduce { FilmsState.DisplayFilms(status = UiStatus.Success, category = category, films = result.data) }
            }
            is Result.Error -> {
                postSideEffect(FilmsSideEffect.Error(result.exception.message ?: "Unknown error"))
                reduce { FilmsState.DisplayFilms(status = UiStatus.Failed("Ошибка вывода фильмов"), category = category) }
            }
        }
    }

    private fun showFilmDetails(film: FilmInfo) = intent {
        // здесь нет вызовов к репозиторию, поэтому нечего ловить
        postSideEffect(FilmsSideEffect.ShowFilmDetails(film))
    }

    private fun sendFilm(category: String, film: String) = intent {
        when (val result = filmsUseCase.playAndGetFilmInfo(category, film)) {
            is Result.Success -> {
                postSideEffect(FilmsSideEffect.ShowFilmDetails(result.data))
            }
            is Result.Error -> {
                postSideEffect(FilmsSideEffect.Error(result.exception.message ?: "Unknown error"))
            }
        }
    }

    private fun backToCategories() = intent {
        loadCategories()
        reduce { FilmsState.DisplayCategories() }
    }

    private fun loadPrivateFilms(password: String) = intent {
        when (val result = filmsUseCase.getPrivateFilms(password)) {
            is Result.Success -> {
                reduce { FilmsState.DisplayFilms(status = UiStatus.Success, category = "Private", films = result.data) }
            }
            is Result.Error -> {
                postSideEffect(FilmsSideEffect.Error(result.exception.message ?: "Unknown error"))
                reduce { FilmsState.DisplayFilms(status = UiStatus.Failed("Ошибка вывода фильмов"), category = "Private") }
            }
        }
    }

    private fun playPrivateFilm(filmName: String) = intent {
        when (val result = filmsUseCase.playPrivateFilm(filmName)) {
            is Result.Success -> {
                postSideEffect(FilmsSideEffect.ShowFilmDetails(result.data))
            }
            is Result.Error -> {
                postSideEffect(FilmsSideEffect.Error(result.exception.message ?: "Unknown error"))
            }
        }
    }

}


sealed class FilmsState {
    data class DisplayCategories(val status: UiStatus = UiStatus.Loading, val categories: List<String> = listOf()) : FilmsState()
    data class DisplayFilms(val status: UiStatus = UiStatus.Loading, val category: String, val films: List<String> = listOf()) : FilmsState()
}

sealed class FilmsEvent {
    object LoadCategories : FilmsEvent()
    data class CategorySelected(val category: String) : FilmsEvent()
    data class FilmSelected(val film: FilmInfo) : FilmsEvent()
    data class SendFilm(val category: String, val film: String) : FilmsEvent()
    data class LoadPrivateFilms(val password: String) : FilmsEvent()
    data class PlayPrivateFilm(val filmName: String) : FilmsEvent()
    object BackPressed : FilmsEvent()
}


sealed class FilmsSideEffect {
    data class ShowFilmDetails(val film: FilmInfo) : FilmsSideEffect()
    data class Error(val error: String) : FilmsSideEffect()
}
