package com.neirno.tv_client.presentation.ui.connection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.neirno.tv_client.core.extension.showToast
import com.neirno.tv_client.core.navigation.NavigationManager
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun ConnectionScreen(
    modifier: Modifier,
    navigationManager: NavigationManager,
    viewState: ConnectionState,
    onEvent: (ConnectionEvent) -> Unit,
    sideEffectFlow: Flow<ConnectionSideEffect>
) {
    Scaffold (
        topBar = {

        }
    ) {
        var textFieldValue by remember { mutableStateOf(viewState.ip) }
        val context = LocalContext.current
        
        LaunchedEffect(sideEffectFlow) {
            sideEffectFlow.collect { sideEffect ->
                when (sideEffect) {
                    is ConnectionSideEffect.ConnectionError -> {
                        context.showToast("Ошибка подключения!")
                    }
                    is ConnectionSideEffect.ConnectionAccept -> {
                        navigationManager.toMainScreen()
                    }
                }
            }
        }
        Column (modifier = Modifier
            .padding(it)
            .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (viewState.loading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()  // Заполняет весь экран
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface, MaterialTheme.shapes.medium),
                value = textFieldValue,
                onValueChange = { newValue ->
                    // Заменяем пробелы на точки
                    textFieldValue = newValue.replace(" ", ".")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onEvent(ConnectionEvent.EnterConnection(textFieldValue))
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
            Spacer(modifier = Modifier.weight(0.5f))

            LazyColumn(
                Modifier
                    .padding(32.dp)
                    .weight(1f) // Оставшееся пространство, которое займет LazyColumn
                    .align(Alignment.CenterHorizontally)
            ) {
                items(viewState.oldIP) { connection ->
                    Box(
                        modifier = Modifier

                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = RectangleShape
                            )
                            .clickable {
                                onEvent(ConnectionEvent.EnterConnection(connection.ip))
                            }
                            .fillMaxWidth(0.7f)
                            .padding(16.dp),

                        contentAlignment = Alignment.Center
                    ) {
                        Row (Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
                            Text(text = connection.ip, color = Color.White)
                            IconButton(modifier = Modifier.size(25.dp),onClick = { onEvent(ConnectionEvent.DeleteConnection(connection.id)) }) {
                                Icon(modifier = Modifier.size(25.dp),imageVector = Icons.Default.Delete, contentDescription = null)
                            }
                        }
                    }

                }
            }

        }
    }
}
