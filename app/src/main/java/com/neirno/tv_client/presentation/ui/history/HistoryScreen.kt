package com.neirno.tv_client.presentation.ui.history

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.neirno.tv_client.core.navigation.NavigationManager

@Composable
fun HistoryScreen(
    modifier: Modifier,
    navigationManager: NavigationManager
) {
    Column (modifier.fillMaxSize()) {
        Text(text = "hs")
        Button(onClick = {navigationManager.back()}) {
            Text(text = "Click")
        }
    }
}