package com.neirno.tv_client.presentation.ui.panel

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.neirno.tv_client.core.navigation.NavigationManager

@Composable
fun PanelScreen(
    modifier: Modifier,
    navigationManager: NavigationManager
) {
    val scrollState = rememberScrollState()
    Column (modifier.fillMaxSize().verticalScroll(scrollState)) {
        RemoteControlUI()
    }

}



@Composable
fun RemoteControlUI() {
    Row (
        Modifier
            .fillMaxWidth()
            .padding(32.dp), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.End){
        ControlButton(text = "Выключить видео", onClick = { /* обработка нажатия */ })
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),  // Примените необходимый отступ
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row (
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ControlButton(text = "||", onClick = { /* обработка нажатия */ })
                    ControlButton(text = "Воспроизведение", onClick = { /* обработка нажатия */ })
                    ControlButton(text = "Следующее видео", onClick = { /* обработка нажатия */ })
                }
            }

            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ControlButton(text = "+ звук", onClick = { /* обработка нажатия */ })
                ControlButton(text = "- звук", onClick = { /* обработка нажатия */ })
                ControlButton(text = "Установить звук", onClick = { /* обработка нажатия */ })
            }

            Row(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ControlButton(text = "- 10с", onClick = { /* обработка нажатия */ })
                ControlButton(text = "+ 10с", onClick = { /* обработка нажатия */ })
                ControlButton(text = "Уст. время", onClick = { /* обработка нажатия */ })
            }
        }
    }
}


@Composable
private fun ControlButton(text: String, onClick: () -> Unit) {
    IconButton(
        onClick = {
            onClick()

        },
        modifier = Modifier
            .border(2.dp, MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(8.dp))
            .size(100.dp)
    ) {
        Text(text)
    }
}