package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.SimulationRepository
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AppScreen(val title: String, val iconLabel: String) {
    DASHBOARD("Dashboard", "Dash"),
    TIMELINE("Timeline", "Gantt"),
    ALLOCATIONS("Allocations", "Tasks"),
    OPTIMIZER("AI & Math Engine", "Solver"),
    SCENARIO_LAB("Scenario Lab", "Sim"),
    REPORTS("Audit Reports", "Export")
}

data class SimulationUiState(
    val currentScreen: AppScreen = AppScreen.DASHBOARD,
    val isOptimizing: Boolean = false,
    val taskSearchQuery: String = "",
    val selectedPriorityFilter: TaskPriority? = null,
    val showAddTaskDialog: Boolean = false,
    val showAddResourceDialog: Boolean = false,
    val showDisruptionSuccessBanner: String? = null
)

class SimulationViewModel(
    private val repository: SimulationRepository = SimulationRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SimulationUiState())
    val uiState: StateFlow<SimulationUiState> = _uiState.asStateFlow()

    val currentPreset: StateFlow<ScenarioPreset> = repository.currentPreset
    val tasks: StateFlow<List<Task>> = repository.tasks
    val resources: StateFlow<List<Resource>> = repository.resources
    val comparisonResult: StateFlow<OptimizationComparison?> = repository.comparisonResult
    val disruptionLogs: StateFlow<List<String>> = repository.disruptionLogs

    fun selectScreen(screen: AppScreen) {
        _uiState.value = _uiState.value.copy(currentScreen = screen)
    }

    fun triggerOptimization() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isOptimizing = true)
            kotlinx.coroutines.delay(200) // Brief animation frame
            repository.recalculate()
            _uiState.value = _uiState.value.copy(isOptimizing = false)
        }
    }

    fun loadPreset(preset: ScenarioPreset) {
        repository.loadPreset(preset)
        _uiState.value = _uiState.value.copy(
            showDisruptionSuccessBanner = "Loaded preset: ${preset.title}"
        )
    }

    fun toggleResourceGrounding(resourceId: String) {
        repository.toggleResourceGrounding(resourceId)
        _uiState.value = _uiState.value.copy(
            showDisruptionSuccessBanner = "Resource status updated. Live dynamic rerouting applied!"
        )
    }

    fun injectEmergencySortie() {
        repository.injectEmergencySortie()
        _uiState.value = _uiState.value.copy(
            showDisruptionSuccessBanner = "Critical Scramble QRA Sortie Injected! Plan re-optimized."
        )
    }

    fun addTask(task: Task) {
        repository.addTask(task)
        _uiState.value = _uiState.value.copy(showAddTaskDialog = false)
    }

    fun removeTask(taskId: String) {
        repository.removeTask(taskId)
    }

    fun addResource(resource: Resource) {
        repository.addResource(resource)
        _uiState.value = _uiState.value.copy(showAddResourceDialog = false)
    }

    fun removeResource(resourceId: String) {
        repository.removeResource(resourceId)
    }

    fun resetScenario() {
        repository.resetToCurrentPreset()
        _uiState.value = _uiState.value.copy(
            showDisruptionSuccessBanner = "Scenario reset to default state."
        )
    }

    fun setTaskSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(taskSearchQuery = query)
    }

    fun setPriorityFilter(priority: TaskPriority?) {
        _uiState.value = _uiState.value.copy(selectedPriorityFilter = priority)
    }

    fun setShowAddTaskDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showAddTaskDialog = show)
    }

    fun setShowAddResourceDialog(show: Boolean) {
        _uiState.value = _uiState.value.copy(showAddResourceDialog = show)
    }

    fun clearBanner() {
        _uiState.value = _uiState.value.copy(showDisruptionSuccessBanner = null)
    }

    fun getJsonReport(): String = repository.generateJsonReport()

    fun getCsvReport(): String = repository.generateCsvReport()
}
