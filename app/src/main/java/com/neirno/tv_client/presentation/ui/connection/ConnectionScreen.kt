package com.neirno.tv_client.presentation.ui.connection

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.neirno.tv_client.presentation.extension.showToast
import com.neirno.tv_client.navigation.NavigationManager
import kotlinx.coroutines.flow.Flow
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import com.neirno.tv_client.core.ui.UiStatus
import com.neirno.tv_client.domain.entity.Connection


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ConnectionScreen(
    modifier: Modifier,
    navigationManager: NavigationManager,
    viewState: ConnectionState,
    onEvent: (ConnectionEvent) -> Unit,
    sideEffectFlow: Flow<ConnectionSideEffect>
) {
    Scaffold {
        var textFieldValue by rememberSaveable { mutableStateOf(viewState.ip) }
        val context = LocalContext.current
        val keyboardController = LocalSoftwareKeyboardController.current

        LaunchedEffect(sideEffectFlow) {
            sideEffectFlow.collect { sideEffect ->
                handleSideEffect(
                    sideEffect = sideEffect,
                    context = context,
                    navigationManager = navigationManager
                )
            }
        }

        Column (modifier = modifier
            .padding(it)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (viewState.status) {
                is UiStatus.Loading -> Loading()
                is UiStatus.Success -> {
                    Spacer(modifier = Modifier.weight(1f))
                    ConnectionInputField(
                        value = textFieldValue,
                        onValueChange = { textFieldValue = it.replace(" ", ".") },
                        onDone = {
                            keyboardController?.hide()
                            onEvent(ConnectionEvent.EnterConnection(textFieldValue))
                        }
                    )
                    LazyColumn(
                        Modifier
                            .padding(32.dp)
                            .weight(1f) // Оставшееся пространство, которое займет LazyColumn
                            .align(Alignment.CenterHorizontally)
                    ) {
                        items(viewState.previousConnections) { connection ->
                            ConnectionItem(
                                connection,
                                { ip -> onEvent(ConnectionEvent.EnterConnection(ip))
                                    keyboardController?.hide()
                                },
                                { id -> onEvent(ConnectionEvent.DeleteConnection(id)) }
                            )
                        }
                    }

                }
                else -> { }
            }
        }
    }
}

@Composable
fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize()  // Заполняет весь экран
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionInputField(
    value: String,
    onValueChange: (String) -> Unit,
    onDone: () -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .border(1.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium),
        value = value,
        onValueChange = { newValue ->
            // Заменяем пробелы на точки
            onValueChange(newValue.replace(" ", "."))
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDone()
            }
        ),
        singleLine = false, // Разрешает многострочный ввод
        textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        shape = MaterialTheme.shapes.medium
    )
}

@Composable
fun ConnectionItem(
    connection: Connection,
    onEnter: (String) -> Unit,
    onDelete: (Long) -> Unit
) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RectangleShape
            )
            .clickable {
                onEnter(connection.ip)
            }
            .fillMaxWidth(0.7f)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = connection.ip, color = Color.White)
            IconButton(
                modifier = Modifier.size(25.dp),
                onClick = { onDelete(connection.id) }
            ) {
                Icon(
                    modifier = Modifier.size(25.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }
        }
    }
}


fun handleSideEffect(sideEffect: ConnectionSideEffect, context: Context, navigationManager: NavigationManager) {
    when (sideEffect) {
        is ConnectionSideEffect.ConnectionError -> {
            context.showToast("Ошибка подключения!")
        }
        is ConnectionSideEffect.ConnectionAccept -> {
            navigationManager.toMainScreen()
        }
    }
}