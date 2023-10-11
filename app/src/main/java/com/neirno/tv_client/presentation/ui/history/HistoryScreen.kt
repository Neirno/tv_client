package com.neirno.tv_client.presentation.ui.history

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.neirno.tv_client.domain.entity.History
import com.neirno.tv_client.navigation.NavigationManager
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    modifier: Modifier,
    navigationManager: NavigationManager,
    viewState: HistoryState,
    onEvent: (HistoryEvent) -> Unit,
    sideEffectFlow: Flow<HistorySideEffect>
) {
    val scrollState = rememberLazyListState()

    BackHandler {
        navigationManager.back()
    }

    Scaffold (
        topBar = {
            Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navigationManager.back() }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
                Button(
                    onClick = { onEvent(HistoryEvent.DeleteAllHistory) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(text = "Delete All")
                }
            }
        }
    ) {
        LazyColumn (
            state = scrollState,
            modifier = modifier
                .fillMaxSize()
                .padding(it).padding(16.dp)
        )   {
                items(viewState.histories) { history ->
                    HistoryItem(history) { it -> onEvent(HistoryEvent.DeleteHistory(it)) }
                }
        }
    }
}

@Composable
fun HistoryItem(history: History, deleteHistory: (History) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = history.title,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { deleteHistory(history) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete History")
        }
    }
}
