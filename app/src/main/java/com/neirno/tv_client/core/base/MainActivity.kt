package com.neirno.tv_client.core.base

import Video
import YoutubeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Power
import androidx.compose.material.icons.filled.QueryBuilder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neirno.tv_client.R
import com.neirno.tv_client.core.constants.Tabs
import com.neirno.tv_client.core.navigation.NavigationManager
import com.neirno.tv_client.core.navigation.NavigationRoutes
import com.neirno.tv_client.presentation.theme.Tv_clientTheme
import com.neirno.tv_client.presentation.ui.connection.ConnectionScreen
import com.neirno.tv_client.presentation.ui.connection.ConnectionViewModel
import com.neirno.tv_client.presentation.ui.history.HistoryScreen
import com.neirno.tv_client.presentation.ui.movies.Category
import com.neirno.tv_client.presentation.ui.movies.MoviesScreen
import com.neirno.tv_client.presentation.ui.panel.PanelScreen
import com.neirno.tv_client.presentation.ui.query.QueryScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.compose.collectSideEffect

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tv_clientTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigator()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val navigationManager = NavigationManager(navController)
    NavHost(navController = navController, startDestination = NavigationRoutes.CONNECTION_SCREEN) {
        composable(NavigationRoutes.CONNECTION_SCREEN) {
            val connectionViewModel: ConnectionViewModel = hiltViewModel()
            ConnectionScreen(
                modifier = Modifier,
                navigationManager = navigationManager,
                viewState = connectionViewModel.container.stateFlow.collectAsState().value,
                onEvent = connectionViewModel::onEvent,
                sideEffectFlow = connectionViewModel.container.sideEffectFlow
            )
        }
        composable(NavigationRoutes.MAIN_SCREEN) {
            val mainViewModel: MainViewModel = hiltViewModel()
            MainScreen(
                modifier = Modifier,
                viewState = mainViewModel.container.stateFlow.collectAsState().value,
                onEvent = mainViewModel::onEvent,
                navigationManager = navigationManager
            )
        }
        composable(NavigationRoutes.HISTORY_SCREEN) {
            //val historyVM: HistoryVM = hiltViewModel()
            HistoryScreen(
                modifier = Modifier,
                navigationManager = navigationManager
            )
        }
        composable(NavigationRoutes.QUERY_SCREEN) {
            //val historyVM: HistoryVM = hiltViewModel()
            QueryScreen(
                modifier = Modifier,
                navigationManager = navigationManager
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier,
    viewState: MainState,
    onEvent: (MainEvent) -> Unit,
    navigationManager: NavigationManager
) {
    val pagerState = rememberPagerState{Tabs.SIZE}

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress)
            onEvent(MainEvent.SetCurrentTab(pagerState.currentPage))
    }

    LaunchedEffect(viewState.currentTab) {
        withContext(Dispatchers.Default) {
            pagerState.animateScrollToPage(viewState.currentTab)
        }
    }

    Scaffold(
        topBar = {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
                Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)) {
                    Row (horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        IconButton(onClick = { navigationManager.toHistoryScreen() }) {
                            Icon(imageVector = Icons.Default.History, contentDescription = null)
                        }
                        IconButton(onClick = { navigationManager.toQueryScreen() }) {
                            Icon(imageVector = Icons.Default.QueryBuilder, contentDescription = null)
                        }
                    }
                    Row (horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        IconButton(onClick = {navigationManager.toHistoryScreen()}) {
                            Icon(imageVector = Icons.Default.Lightbulb, contentDescription = null)
                        }
                        IconButton(onClick = {navigationManager.toHistoryScreen()}) {
                            Icon(imageVector = Icons.Default.Power, contentDescription = null)
                        }
                    }
                }
                TabRow(selectedTabIndex = viewState.currentTab, containerColor = MaterialTheme.colorScheme.surface, /*modifier = Modifier.padding(top = 8.dp)*/) {
                    Tab(
                        text = { Text("YouTube") },
                        selected = viewState.currentTab == Tabs.YOUTUBE,
                        onClick = { onEvent(MainEvent.SetCurrentTab(Tabs.YOUTUBE)) },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface
                    )
                    Tab(text = { Text("Пульт") },
                        selected = viewState.currentTab == Tabs.PANEL,
                        onClick = { onEvent(MainEvent.SetCurrentTab(Tabs.PANEL)) },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface
                    )
                    Tab(text = { Text("Фильмы") },
                        selected = viewState.currentTab == Tabs.MOVIES,
                        onClick = { onEvent(MainEvent.SetCurrentTab(Tabs.MOVIES)) },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        bottomBar = {

        }
    ) { paddingValue ->
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
        ) { index ->
            when (index) {
                Tabs.YOUTUBE -> {
                    val video = Video(
                        title = "Example Video",
                        channel = "Example Channel",
                        views = "1M",
                        duration = "10:30",
                        thumbnail_url = R.drawable.maxresdefault
                    )
                    val video1 = Video(
                        title = "Example Video",
                        channel = "Example Channel",
                        views = "1M",
                        duration = "10:30",
                        thumbnail_url = R.drawable.maxresdefault
                    )
                    val list = listOf<Video>(video, video1)
                    YoutubeScreen(Modifier.padding(paddingValue),navigationManager, list)

                }
                Tabs.PANEL -> {
                    PanelScreen(Modifier.padding(paddingValue), navigationManager)
                }
                Tabs.MOVIES -> {
                    val category = listOf(Category(
                        title = "Category",
                        imageUrl = ""
                    ))
                    MoviesScreen(Modifier.padding(paddingValue), navigationManager, category)
                }
            }
        }
    }
}
