package com.neirno.tv_client.presentation.ui.panel.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundSetupDialog(
    showDialog: Boolean,
    onCloseDialog: () -> Unit,
    onConfirm: (soundValue: String) -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onCloseDialog) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Column {
                    Text(text = "Установить звук")

                    val soundState = remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = soundState.value,
                        onValueChange = { soundState.value = it },
                        label = { Text("Значение звука") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        onConfirm(soundState.value)
                    }) {
                        Text("Подтвердить")
                    }
                }
            }
        }
    }
}
