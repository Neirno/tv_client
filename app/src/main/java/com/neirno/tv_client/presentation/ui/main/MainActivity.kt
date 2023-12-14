 package com.neirno.tv_client.presentation.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.biometric.BiometricPrompt
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.neirno.tv_client.core.constants.Tabs
import com.neirno.tv_client.navigation.NavigationManager
import com.neirno.tv_client.navigation.NavigationRoutes
import com.neirno.tv_client.presentation.theme.Tv_clientTheme
import com.neirno.tv_client.presentation.ui.connection.ConnectionScreen
import com.neirno.tv_client.presentation.ui.connection.ConnectionViewModel
import com.neirno.tv_client.presentation.ui.history.HistoryScreen
import com.neirno.tv_client.presentation.ui.history.HistoryViewModel
import com.neirno.tv_client.presentation.ui.movies.FilmsScreen
import com.neirno.tv_client.presentation.ui.movies.FilmsViewModel
import com.neirno.tv_client.presentation.ui.panel.PanelScreen
import com.neirno.tv_client.presentation.ui.panel.PanelViewModel
import com.neirno.tv_client.presentation.ui.youtube.YoutubeScreen
import com.neirno.tv_client.presentation.ui.youtube.YoutubeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    fun showBiometricPrompt(activity: FragmentActivity, onSuccess: () -> Unit) {
        val executor = ContextCompat.getMainExecutor(activity)
        val biometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess()
            }

            // Добавьте обработку других случаев при необходимости.
        })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Биометрическая аутентификация")
            .setSubtitle("Пожалуйста, аутентифицируйтесь")
            .setNegativeButtonText("Отмена")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Tv_clientTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val navigationManager = NavigationManager(navController)
                    var backgroundedTime: Long = 0

                    val observer = LifecycleEventObserver { _, event ->
                        when (event) {
                            Lifecycle.Event.ON_STOP -> {
                                // Приложение свернуто, сохраняем текущее время
                                backgroundedTime = System.currentTimeMillis()
                            }
                            Lifecycle.Event.ON_START -> {
                                // Приложение восстановлено, сравниваем время
                                if (System.currentTimeMillis() - backgroundedTime > 60000) {
                                    // Если приложение было свернуто больше минуты, переходим на экран ввода IP
                                    navigationManager.toConnectionScreen()
                                }
                            }
                            else -> {
                                // Для других событий ничего не делаем
                            }
                        }
                    }
                    lifecycle.addObserver(observer)
                    AppNavigator(navController, navigationManager)
                }
            }
        }
    }
}

@Composable
fun AppNavigator(navController: NavHostController, navigationManager: NavigationManager) {

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
                navigationManager = navigationManager,
                viewState = mainViewModel.container.stateFlow.collectAsState().value,
                onEvent = mainViewModel::onEvent,
                sideEffectFlow = mainViewModel.container.sideEffectFlow
            )
        }
        composable(NavigationRoutes.HISTORY_SCREEN) {
            val historyViewModel: HistoryViewModel = hiltViewModel()
            HistoryScreen(
                modifier = Modifier,
                navigationManager = navigationManager,
                viewState = historyViewModel.container.stateFlow.collectAsState().value,
                onEvent = historyViewModel::onEvent,
                sideEffectFlow = historyViewModel.container.sideEffectFlow
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier,
    navigationManager: NavigationManager,
    viewState: MainState,
    onEvent: (MainEvent) -> Unit,
    sideEffectFlow: Flow<MainSideEffect>
) {
    val pagerState = rememberPagerState{ Tabs.SIZE}

    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        withContext(Dispatchers.IO) {
            if (!pagerState.isScrollInProgress)
                onEvent(MainEvent.SetCurrentTab(pagerState.currentPage))
        }
    }

    LaunchedEffect(viewState.currentTab) {
        withContext(Dispatchers.IO) {
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
                        IconButton(onClick = {
                            navigationManager.toHistoryScreen()
                        }) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                            )
                        }

                    }

                    Row (horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                        IconButton(onClick = {
                            if (viewState.isDisplayOn)
                                onEvent(MainEvent.DisplayOffEvent)
                            else
                                onEvent(MainEvent.DisplayOnEvent)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Power,
                                contentDescription = null,
                                tint = if (viewState.isDisplayOn) Color.Yellow else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(onClick = {
                            if (viewState.isLightOn)
                                onEvent(MainEvent.LightOffEvent)
                            else
                                onEvent(MainEvent.LightOnEvent)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = if (viewState.isLightOn) Color.Yellow else MaterialTheme.colorScheme.onSurface
                            )
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
        }
    ) { paddingValue ->
        val youtubeViewModel: YoutubeViewModel = hiltViewModel()
        val panelViewModel: PanelViewModel = hiltViewModel()
        val filmsViewModel: FilmsViewModel = hiltViewModel()

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
        ) { index ->
            when (index) {
                Tabs.YOUTUBE -> {
                    YoutubeScreen(
                        modifier = Modifier.padding(paddingValue),
                        viewState = youtubeViewModel.container.stateFlow.collectAsState().value,
                        onEvent = youtubeViewModel::onEvent,
                        sideEffectFlow = youtubeViewModel.container.sideEffectFlow
                        )
                }
                Tabs.PANEL -> {
                    PanelScreen(
                        modifier = Modifier.padding(paddingValue),
                        viewState = panelViewModel.container.stateFlow.collectAsState().value,
                        onEvent = panelViewModel::onEvent,
                        sideEffectFlow = panelViewModel.container.sideEffectFlow
                    )
                }
                Tabs.MOVIES -> {
                    FilmsScreen(
                        modifier = Modifier.padding(paddingValue),
                        viewState = filmsViewModel.container.stateFlow.collectAsState().value,
                        onEvent = filmsViewModel::onEvent,
                        sideEffectFlow = filmsViewModel.container.sideEffectFlow
                    )
                }
            }
        }
    }
}
