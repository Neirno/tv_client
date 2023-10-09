package com.neirno.tv_client.presentation.ui.movies.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.neirno.tv_client.core.ui.UiStatus

@Composable
fun DisplayingFilms(
    status: UiStatus,
    category: String,
    filmsName: List<String>,
    openFilm: (String, String) -> Unit,
    backToCategory: () -> Unit
) {
    BackHandler {
        backToCategory()
    }
    Box(modifier = Modifier.fillMaxWidth()) {
        IconButton(
            onClick = { backToCategory() },
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
        }
    }
    when(status) {
        UiStatus.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        UiStatus.Success -> {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)) {
                items(filmsName) { film ->
                    FilmCard(
                        filmName = film,
                        openCategory = { openFilm(category, film) }
                    )
                }
            }
        }
        is UiStatus.Failed -> {
            /*Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ошибка загрузки", color = Color.Red)
            }*/
        }
    }
}


@Composable
fun FilmCard(
    filmName: String, // Название категори
    openCategory: () -> Unit
) {
    // Обертка для карточки
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { openCategory() }
            .clip(shape = RoundedCornerShape(8.dp))
    ) {
        Text(
            text = filmName,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
        Divider()
    }
}