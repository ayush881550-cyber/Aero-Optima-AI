package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.PredefinedScenarios
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.SimulationViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AeroOptimaApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AeroOptimaApp(
    viewModel: SimulationViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val comparison by viewModel.comparisonResult.collectAsStateWithLifecycle()
    val resources by viewModel.resources.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val currentPreset by viewModel.currentPreset.collectAsStateWithLifecycle()
    val disruptionLogs by viewModel.disruptionLogs.collectAsStateWithLifecycle()

    // BackHandler: return to Dashboard if on another sub-screen
    BackHandler(enabled = uiState.currentScreen != AppScreen.DASHBOARD) {
        viewModel.selectScreen(AppScreen.DASHBOARD)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(NavySurfaceVariant)
                                .border(1.dp, CyanAccent.copy(alpha = 0.5f), RoundedCornerShape(6.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlightTakeoff,
                                contentDescription = "Logo",
                                tint = CyanAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "AEROOPTIMA AI",
                                    color = TextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.5.sp
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(CyanAccent.copy(alpha = 0.2f))
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text("SIH26250", color = CyanAccent, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Text(
                                text = "Dynamic Air Operations & Resource Optimisation",
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                },
                actions = {
                    // Quick Scramble / Disruption Button in Top Bar
                    IconButton(
                        onClick = { viewModel.injectEmergencySortie() },
                        modifier = Modifier.testTag("topbar_emergency_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = "Inject Emergency QRA",
                            tint = StatusDanger
                        )
                    }

                    // Recalculate button
                    IconButton(
                        onClick = { viewModel.triggerOptimization() },
                        modifier = Modifier.testTag("topbar_optimize_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Run Optimization",
                            tint = CyanAccent
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NavySurface,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = NavySurface,
                tonalElevation = 4.dp,
                modifier = Modifier.border(1.dp, NavyCardBorder.copy(alpha = 0.6f), RoundedCornerShape(0.dp))
            ) {
                AppScreen.entries.forEach { screen ->
                    val isSelected = uiState.currentScreen == screen
                    val icon = when (screen) {
                        AppScreen.DASHBOARD -> Icons.Default.Dashboard
                        AppScreen.TIMELINE -> Icons.Default.Schedule
                        AppScreen.ALLOCATIONS -> Icons.Default.ListAlt
                        AppScreen.OPTIMIZER -> Icons.Default.AutoGraph
                        AppScreen.SCENARIO_LAB -> Icons.Default.Tune
                        AppScreen.REPORTS -> Icons.Default.Assessment
                    }

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectScreen(screen) },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = screen.title,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = {
                            Text(
                                text = screen.iconLabel,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = CyanAccent,
                            indicatorColor = CyanAccent,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextMuted
                        ),
                        modifier = Modifier.testTag("nav_item_${screen.name.lowercase()}")
                    )
                }
            }
        },
        floatingActionButton = {
            if (uiState.currentScreen == AppScreen.DASHBOARD || uiState.currentScreen == AppScreen.TIMELINE) {
                FloatingActionButton(
                    onClick = { viewModel.triggerOptimization() },
                    containerColor = ElectricBlue,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("fab_optimize_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.AutoGraph, "Optimize", modifier = Modifier.size(18.dp))
                        Text("Solve Plan", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(NavyBackground)
        ) {
            AnimatedContent(
                targetState = uiState.currentScreen,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "screen_transition"
            ) { targetScreen ->
                when (targetScreen) {
                    AppScreen.DASHBOARD -> DashboardScreen(
                        comparison = comparison,
                        resources = resources,
                        currentPreset = currentPreset,
                        onRunOptimization = { viewModel.triggerOptimization() },
                        onInjectEmergency = { viewModel.injectEmergencySortie() },
                        onSelectPreset = { viewModel.loadPreset(it) },
                        allPresets = PredefinedScenarios.allPresets,
                        bannerMessage = uiState.showDisruptionSuccessBanner,
                        onDismissBanner = { viewModel.clearBanner() }
                    )

                    AppScreen.TIMELINE -> TimelineScreen(
                        comparison = comparison,
                        resources = resources
                    )

                    AppScreen.ALLOCATIONS -> AllocationTableScreen(
                        comparison = comparison,
                        searchQuery = uiState.taskSearchQuery,
                        onSearchChange = { viewModel.setTaskSearchQuery(it) },
                        selectedPriority = uiState.selectedPriorityFilter,
                        onSelectPriority = { viewModel.setPriorityFilter(it) }
                    )

                    AppScreen.OPTIMIZER -> OptimizerEngineScreen(
                        comparison = comparison,
                        disruptionLogs = disruptionLogs
                    )

                    AppScreen.SCENARIO_LAB -> ScenarioLabScreen(
                        currentPreset = currentPreset,
                        allPresets = PredefinedScenarios.allPresets,
                        onSelectPreset = { viewModel.loadPreset(it) },
                        tasks = tasks,
                        resources = resources,
                        onToggleGrounding = { viewModel.toggleResourceGrounding(it) },
                        onInjectEmergency = { viewModel.injectEmergencySortie() },
                        onResetScenario = { viewModel.resetScenario() },
                        onAddTask = { viewModel.addTask(it) },
                        onRemoveTask = { viewModel.removeTask(it) },
                        onAddResource = { viewModel.addResource(it) },
                        onRemoveResource = { viewModel.removeResource(it) },
                        showAddTaskDialog = uiState.showAddTaskDialog,
                        onShowAddTaskDialogChange = { viewModel.setShowAddTaskDialog(it) },
                        showAddResourceDialog = uiState.showAddResourceDialog,
                        onShowAddResourceDialogChange = { viewModel.setShowAddResourceDialog(it) }
                    )

                    AppScreen.REPORTS -> ReportScreen(
                        comparison = comparison,
                        currentPreset = currentPreset,
                        jsonReport = viewModel.getJsonReport(),
                        csvReport = viewModel.getCsvReport()
                    )
                }
            }
        }
    }
}
