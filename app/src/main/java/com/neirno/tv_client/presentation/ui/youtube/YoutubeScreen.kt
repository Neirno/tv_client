import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.neirno.tv_client.core.navigation.NavigationManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import coil.compose.rememberImagePainter
import com.neirno.tv_client.R
import com.neirno.tv_client.domain.entity.Youtube
import com.neirno.tv_client.presentation.ui.connection.ConnectionEvent
import com.neirno.tv_client.presentation.ui.youtube.YoutubeEvent
import com.neirno.tv_client.presentation.ui.youtube.YoutubeSideEffect
import com.neirno.tv_client.presentation.ui.youtube.YoutubeState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YoutubeScreen(
    modifier: Modifier = Modifier,
    navigationManager: NavigationManager,
    viewState: YoutubeState,
    onEvent: (YoutubeEvent) -> Unit,
    sideEffectFlow: Flow<YoutubeSideEffect>
) {
    var textFieldValue by remember { mutableStateOf(viewState.query) }
    val context = LocalContext.current

    Column(
        modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onEvent(YoutubeEvent.GetVideos(textFieldValue))
                }
            ),
            shape = CircleShape,
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        )
        Button(onClick = {onEvent(YoutubeEvent.GetVideos(textFieldValue))}) {
            Text(text = "q")
        }
        LazyColumn {
            items(viewState.videos) { video ->
                VideoCard(
                    video = video,
                    onClick = {onEvent(YoutubeEvent.SendVideo(video))}
                )
            }
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
        modifier = Modifier
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

