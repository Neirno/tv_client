import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.neirno.tv_client.core.navigation.NavigationManager
import androidx.compose.ui.res.painterResource
import com.neirno.tv_client.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YoutubeScreen(
    modifier: Modifier = Modifier,
    navigationManager: NavigationManager,
    videoList: List<Video>,
) {
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
            value = "",
            onValueChange = {},
            shape = CircleShape,
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            }
        )
        LazyColumn {
            items(videoList) { video ->
                VideoCard(video)
            }
        }
    }
}

@Composable
fun VideoCard(video: Video) {
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
                .clickable {  }
        ) {
            Image(
                painter = painterResource(id = video.thumbnail_url),
                contentDescription = "Thumbnail",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
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

@Preview
@Composable
fun PreviewVideoCard() {
    VideoCard(
        video = Video(
            title = "Example Video",
            channel = "Example Channel",
            views = "1M",
            duration = "10:30",
            thumbnail_url = R.drawable.ic_launcher_foreground
        )
    )
}

data class Video(
    val title: String,
    val channel: String,
    val views: String,
    val duration: String,
    val thumbnail_url: Int // as Drawable resource for simplicity
)
