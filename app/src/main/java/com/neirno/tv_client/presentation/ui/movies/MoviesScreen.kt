package com.neirno.tv_client.presentation.ui.movies

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.neirno.tv_client.R
import com.neirno.tv_client.core.navigation.NavigationManager

@Composable
fun MoviesScreen(
    modifier: Modifier,
    navigationManager: NavigationManager,
    movieCategory: List<Category>
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(Modifier.fillMaxSize().padding(top = 16.dp)) {
            items(movieCategory) { category ->
                MovieCategoryCard(
                    imageUrl = R.drawable.maxresdefault,
                    categoryName = category.title
                )
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun MovieCategoryCard(
    imageUrl: Int, // URL или ресурс изображения
    categoryName: String // Название категории
) {
    // Обертка для карточки
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp) // Установите нужные вам размеры
            .clip(shape = RoundedCornerShape(8.dp)) // Скругление углов
    ) {
        // Загрузка и отображение изображения
        Image(
            painter = rememberImagePainter(data = imageUrl), // Используем Coil для загрузки изображения
            contentDescription = null,
            contentScale = ContentScale.Crop, // Обрезка изображения
            modifier = Modifier.fillMaxSize()
        )

        // Градиентный фон
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black // Вы можете изменить цвет на нужный вам
                        ),
                        startY = 0.8f // Начало градиента
                    )
                )
        )

        // Текст
        Text(
            text = categoryName,
            color = Color.White, // Установите нужный цвет текста
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}


/*@Composable
fun MovieCategoryCard(
    imageUrl: Int,  // ID ресурса изображения
    categoryName: String
) {
    Box(modifier = Modifier .fillMaxWidth()
        //.aspectRatio(16f / 9f)  // соотношение сторон 16:9, как у большинства видео
        .height(75.dp)
        .clip(MaterialTheme.shapes.medium)
        .clickable {  }) {
        Image(
            painter = painterResource(id = imageUrl),
            contentDescription = "Category image",
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.surface
                        ),
                        startY = 0.85f * 100.dp.value
                    )
                )
        )

        Text(
            text = categoryName,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 8.dp, bottom = 8.dp)
        )
    }
}*/


@Composable
fun FilmsCategory(category: Category) {
    Column {

    }
}

data class Category(
    val title: String,
    val imageUrl: String //str
)