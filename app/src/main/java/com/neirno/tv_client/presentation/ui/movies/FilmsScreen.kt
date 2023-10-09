package com.neirno.tv_client.presentation.ui.movies

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.*
import com.neirno.tv_client.core.extension.showToast
import com.neirno.tv_client.presentation.ui.movies.components.DisplayingCategories
import com.neirno.tv_client.presentation.ui.movies.components.DisplayingFilms
import kotlinx.coroutines.flow.Flow

@Composable
fun FilmsScreen(
    modifier: Modifier,
    viewState: FilmsState,
    onEvent: (FilmsEvent) -> Unit,
    sideEffectFlow: Flow<FilmsSideEffect>
) {
    val context = LocalContext.current

    LaunchedEffect(sideEffectFlow) {
        sideEffectFlow.collect { sideEffect ->
            handleSideEffect(
                sideEffect = sideEffect,
                context = context,
                backToCategory = { onEvent(FilmsEvent.BackPressed) }
            )
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(viewState) {
            is FilmsState.DisplayCategories -> DisplayingCategories(
                status = viewState.status,
                categoryName = viewState.categories,
                openCategory = { it -> onEvent(FilmsEvent.CategorySelected(it)) }
            )
            is FilmsState.DisplayFilms -> DisplayingFilms(
                status = viewState.status,
                category = viewState.category,
                filmsName = viewState.films,
                openFilm = { category, film -> onEvent(FilmsEvent.SendFilm(category, film))},
                backToCategory = { onEvent(FilmsEvent.BackPressed) }
            )
        }
        
    }
}

fun handleSideEffect(sideEffect: FilmsSideEffect, context: Context, backToCategory: () -> Unit) {
    when (sideEffect) {
        is FilmsSideEffect.ShowFilmDetails -> {
            context.showToast(sideEffect.film.filmName)
        }
        is FilmsSideEffect.Error -> {
            context.showToast(sideEffect.error)
            backToCategory()
        }
    }
}