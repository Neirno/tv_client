package com.neirno.tv_client.presentation.ui.panel

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.neirno.tv_client.presentation.ui.panel.components.SoundSetupDialog
import com.neirno.tv_client.presentation.ui.panel.components.TimeSetupDialog
import kotlinx.coroutines.flow.Flow

@Composable
fun PanelScreen(
    modifier: Modifier = Modifier,
    viewState: PanelState,
    onEvent: (PanelEvent) -> Unit,
    sideEffectFlow: Flow<PanelSideEffect>
) {
    val scrollState = rememberScrollState()
    var showTimeDialog by remember { mutableStateOf(false) }
    var showSoundDialog by remember { mutableStateOf(false) }
    var showStatus by remember { mutableStateOf(false) } // состояние для отображения диалога


    TimeSetupDialog(
        showDialog = showTimeDialog,
        onCloseDialog = { showTimeDialog = false },
        onConfirm = { hours, minutes, seconds ->
            onEvent(PanelEvent.SetTime(hours, minutes, seconds))
        }
    )

    SoundSetupDialog(
        showDialog = showSoundDialog,
        onCloseDialog = { showSoundDialog = false },
        onConfirm = { soundValue ->
            onEvent(PanelEvent.SetVolume(soundValue))
        }
    )

    Column (
        modifier
            .fillMaxSize()
            .verticalScroll(scrollState)) {
        Row (
            Modifier
                .fillMaxWidth()
                .padding(32.dp), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceBetween){
            ControlButton(text = "Статус", onClick = {
                onEvent(PanelEvent.GetStatus)
                showStatus = true
            })
            ControlButton(text = "Off", onClick = { onEvent(PanelEvent.Stop) })
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
                        ControlButton(text = "||", onClick = { onEvent(PanelEvent.Pause) })
                        ControlButton(text = "|>", onClick = { onEvent(PanelEvent.Resume) })
                        ControlButton(text = ">>", onClick = { onEvent(PanelEvent.SkipVideo) })
                    }
                }

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ControlButton(text = "+ 10", onClick = { onEvent(PanelEvent.VolumeShiftForward) })
                    ControlButton(text = "- 10", onClick = { onEvent(PanelEvent.VolumeShiftBackward) })
                    ControlButton(text = "Звук", onClick = { showSoundDialog = true })
                }

                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ControlButton(text = "- 10", onClick = { onEvent(PanelEvent.SkipBackward) })
                    ControlButton(text = "+ 10", onClick = { onEvent(PanelEvent.SkipForward) })
                    ControlButton(text = "Время", onClick = { showTimeDialog = true })
                }
                if (showStatus && viewState.videoStatus != null) {
                    AlertDialog(
                        onDismissRequest = { showStatus = false },
                        title = { Text(text = viewState.videoStatus.title)},
                        text = {
                               Column {
                                   Text(text = "Название: ${viewState.videoStatus.title}")
                                   Text(text = "Канал: ${viewState.videoStatus.channel}")
                                   Text(text = "Просмотры: ${viewState.videoStatus.views}")
                                   Text(text = "Продолжительность: ${viewState.videoStatus.duration}")
                                   Text(text = "Текущее время: ${viewState.videoStatus.current_time}")
                                   Text(text = "Громкость: ${viewState.videoStatus.volume}")
                               }
                        },
                        confirmButton = {
                            Button(
                                onClick = { showStatus = false }, colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                )
                            ) {
                                Text(text = "Ок")
                            }
                        })
                }
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