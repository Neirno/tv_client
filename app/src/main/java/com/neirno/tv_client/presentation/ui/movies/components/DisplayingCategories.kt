package com.neirno.tv_client.presentation.ui.movies.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    openPrivateCategory: (String) -> Unit
) {

    when(status) {
        UiStatus.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        UiStatus.Success -> {
            var showDialog by remember { mutableStateOf(false) } // состояние для отображения диалога

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { showDialog = true },
                ) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                }
            }
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

            if (showDialog) {
                PasswordDialog(
                    onPasswordEntered = { password ->
                        // Обработка введенного пароля
                        openPrivateCategory(password)
                        showDialog = false // закрыть диалог после обработки пароля
                    },
                    onDismiss = { showDialog = false }
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordDialog(onPasswordEntered: (String) -> Unit, onDismiss: () -> Unit) {
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Введите пароль") },
        text = {
            TextField(
                value = password,
                onValueChange = {newValue -> password = newValue},
                placeholder = { Text(text = "Пароль") }
            )
        },
        confirmButton = {
            Button(onClick = { onPasswordEntered(password) }) {
                Text(text = "Подтвердить")
            }
        }
    )
}


fun randomColor(): Color {
    return Color(
        red = Random.nextInt(256),
        green = Random.nextInt(256),
        blue = Random.nextInt(256)
    )
}