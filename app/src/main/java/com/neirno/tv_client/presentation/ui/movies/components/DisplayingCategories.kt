package com.neirno.tv_client.presentation.ui.movies.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.neirno.tv_client.core.ui.UiStatus
import kotlin.random.Random

@Composable
fun DisplayingCategories(
    status: UiStatus,
    categoryName: List<String>,
    openCategory: (String) -> Unit,
) {

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
                items(categoryName) { category ->
                    FilmCategoryCard(
                        categoryName = category,
                        openCategory = { openCategory(category) }
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
fun FilmCategoryCard(
    color: Color = randomColor(), // Замените imageUrl на color
    categoryName: String, // Название категори
    openCategory: () -> Unit
) {
    // Обертка для карточки
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .background(color) // Примените рандомный цвет
            .clickable { openCategory() }
    ) {
        // Градиентный фон
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black
                        ),
                        startY = 0.8f
                    )
                )
        )

        // Текст
        Text(
            text = categoryName,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}


fun randomColor(): Color {
    return Color(
        red = Random.nextInt(256),
        green = Random.nextInt(256),
        blue = Random.nextInt(256)
    )
}