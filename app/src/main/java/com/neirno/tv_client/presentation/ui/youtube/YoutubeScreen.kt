import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.neirno.tv_client.core.navigation.NavigationManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.window.PopupProperties
import coil.compose.rememberImagePainter
import com.neirno.tv_client.core.extension.showToast
import com.neirno.tv_client.core.ui.UiStatus
import com.neirno.tv_client.domain.entity.Youtube
import com.neirno.tv_client.presentation.ui.movies.FilmsSideEffect
import com.neirno.tv_client.presentation.ui.movies.handleSideEffect
import com.neirno.tv_client.presentation.ui.youtube.YoutubeEvent
import com.neirno.tv_client.presentation.ui.youtube.YoutubeSideEffect
import com.neirno.tv_client.presentation.ui.youtube.YoutubeState
import kotlinx.coroutines.flow.Flow


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun YoutubeScreen(
    modifier: Modifier = Modifier,
    viewState: YoutubeState,
    onEvent: (YoutubeEvent) -> Unit,
    sideEffectFlow: Flow<YoutubeSideEffect>
) {
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = viewState.query
            )
        )
    }
    val context = LocalContext.current
    val scrollState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    BackHandler {
        focusManager.clearFocus()
        expanded = false
    }

    LaunchedEffect(sideEffectFlow) {
        sideEffectFlow.collect { sideEffect ->
            handleSideEffect(
                sideEffect = sideEffect,
                context = context,
            )
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier.padding(bottom = 16.dp),
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            val filteringYoutubeSearch = viewState.lastQueries.filter { it.query.contains(textFieldValue.text, ignoreCase = true) }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                value = textFieldValue,
                onValueChange = { newValue ->
                    textFieldValue = newValue
                    if (filteringYoutubeSearch.isNotEmpty()) {
                        expanded = true
                    }
                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onEvent(YoutubeEvent.SaveQuery(textFieldValue.text))
                        onEvent(YoutubeEvent.GetVideos(textFieldValue.text))
                        expanded = false
                        keyboardController?.hide()
                    },
                ),
                shape = CircleShape,
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    onEvent(YoutubeEvent.GetLastQueries)
                },
                label = { Text(text = "Поиск") },
            )

            if (filteringYoutubeSearch.isNotEmpty()) {
                DropdownMenu(
                    modifier = Modifier.exposedDropdownSize(),
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                        //focusManager.clearFocus()
                    },
                    properties = PopupProperties(focusable = false)

                ) {
                    filteringYoutubeSearch.forEach { youtubeSearch ->
                        DropdownMenuItem(
                            text =
                            {
                                Text(text = youtubeSearch.query)
                            },
                            onClick = {
                                textFieldValue = TextFieldValue(
                                    text = youtubeSearch.query,
                                    selection = TextRange(youtubeSearch.query.length)
                                )

                                //textFieldValue = youtubeSearch.query
                                onEvent(YoutubeEvent.GetVideos(youtubeSearch.query))
                                expanded = false
                                keyboardController?.hide()
                            },
                            trailingIcon = {
                                IconButton(onClick = { onEvent(YoutubeEvent.DeleteYoutubeSearch(youtubeSearch)) }) {
                                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                                }
                            },
                            leadingIcon = {
                                IconButton(onClick = {
                                    textFieldValue = TextFieldValue(
                                        text = youtubeSearch.query,
                                        selection = TextRange(youtubeSearch.query.length)
                                    )
                                }) {
                                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                                }
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }
        }
        when {
            viewState.videos.isEmpty() && viewState.status == UiStatus.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                LazyColumn(state = scrollState) {
                    itemsIndexed(viewState.videos) { index, video ->
                        VideoCard(video = video, onClick = { onEvent(YoutubeEvent.SendVideo(video)) })
                    }

                    when (viewState.status) {
                        UiStatus.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxSize().padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                        is UiStatus.Failed -> {
                            item {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = (viewState.status as UiStatus.Failed).message)
                                }
                            }
                        }
                        UiStatus.Success -> {}  // No additional items for success status.
                    }
                }
            }
        }

        val threshold = viewState.videos.size * 0.8  // начнем загрузку при достижении 80% списка
        val lastVisibleIndex = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index

        if (scrollState.isScrollInProgress && lastVisibleIndex != null && lastVisibleIndex >= threshold && !viewState.hasReachedEndOfList) {
            onEvent(YoutubeEvent.GetVideos(viewState.query))
        }
    }
}

@Composable
fun VideoCard(
    modifier: Modifier = Modifier,
    video: Youtube,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)  // соотношение сторон 16:9, как у большинства видео
                .clip(MaterialTheme.shapes.medium)

        ) {
            Image(
                painter = rememberImagePainter(data = video.thumbnailUrl),
                contentDescription = "Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onClick() }
            )
            Text(
                text = video.duration,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(4.dp)  // отступы для текста внутри прямоугольника
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)) // фон текста с прозрачностью
                    .padding(horizontal = 8.dp, vertical = 2.dp)  // отступы текста от краев картинки
            )
        }

        Column (modifier = Modifier.padding(16.dp)) {
            Text(text = video.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "${video.channel}  •  ${video.views} просмотров",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface)
        }

    }
}

fun handleSideEffect(sideEffect: YoutubeSideEffect, context: Context) {
    when (sideEffect) {
        is YoutubeSideEffect.VideoIsSend -> {
            context.showToast(sideEffect.title)
        }
        is YoutubeSideEffect.ErrorMessage -> {
            context.showToast(sideEffect.msg)
        }
    }
}