package com.example.data

import com.example.engine.OptimizationEngine
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class SimulationRepository(
    private val engine: OptimizationEngine = OptimizationEngine()
) {
    private val _currentPreset = MutableStateFlow<ScenarioPreset>(PredefinedScenarios.sih26250Demo)
    val currentPreset: StateFlow<ScenarioPreset> = _currentPreset.asStateFlow()

    private val _tasks = MutableStateFlow<List<Task>>(PredefinedScenarios.sih26250Demo.defaultTasks)
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _resources = MutableStateFlow<List<Resource>>(PredefinedScenarios.sih26250Demo.defaultResources)
    val resources: StateFlow<List<Resource>> = _resources.asStateFlow()

    private val _comparisonResult = MutableStateFlow<OptimizationComparison?>(null)
    val comparisonResult: StateFlow<OptimizationComparison?> = _comparisonResult.asStateFlow()

    private val _disruptionLogs = MutableStateFlow<List<String>>(
        listOf("Initial simulation scenario loaded: SIH-26250 Demo (7 Tasks, 4 Resources).")
    )
    val disruptionLogs: StateFlow<List<String>> = _disruptionLogs.asStateFlow()

    init {
        recalculate()
    }

    fun recalculate() {
        val currentTasks = _tasks.value
        val currentResources = _resources.value
        val result = engine.runFullComparison(currentTasks, currentResources)
        _comparisonResult.value = result
    }

    fun loadPreset(preset: ScenarioPreset) {
        _currentPreset.value = preset
        _tasks.value = preset.defaultTasks
        _resources.value = preset.defaultResources
        logEvent("Loaded preset: ${preset.title}")
        recalculate()
    }

    fun toggleResourceGrounding(resourceId: String) {
        _resources.value = _resources.value.map { res ->
            if (res.id == resourceId) {
                val newStatus = if (res.status == OperationalStatus.MAINTENANCE_STANDBY) {
                    OperationalStatus.AVAILABLE
                } else {
                    OperationalStatus.MAINTENANCE_STANDBY
                }
                val actionName = if (newStatus == OperationalStatus.MAINTENANCE_STANDBY) "Grounded for maintenance" else "Restored to active flightline"
                logEvent("Fleet Disruption: ${res.code} (${res.name}) $actionName.")
                res.copy(status = newStatus)
            } else {
                res
            }
        }
        recalculate()
    }

    fun injectEmergencySortie() {
        val nextIndex = _tasks.value.size + 1
        val emergencyTask = Task(
            id = "emergency_${System.currentTimeMillis()}",
            code = "Task ${('A' + nextIndex - 1)}",
            name = "Emergency Scramble QRA #$nextIndex",
            missionType = MissionType.QUICK_REACTION_ALERT,
            priority = TaskPriority.CRITICAL,
            requiredCapacity = 1,
            durationMinutes = 45,
            earliestStartTime = 15,
            deadlineMinutes = 90,
            isEmergencyDisruption = true
        )
        _tasks.value = _tasks.value + emergencyTask
        logEvent("Dynamic Event: Critical Emergency Task ${emergencyTask.code} injected at T+15m! Recalculating allocations...")
        recalculate()
    }

    fun addTask(task: Task) {
        _tasks.value = _tasks.value + task
        logEvent("Task added: ${task.code} (${task.name})")
        recalculate()
    }

    fun removeTask(taskId: String) {
        val taskToRemove = _tasks.value.find { it.id == taskId }
        _tasks.value = _tasks.value.filterNot { it.id == taskId }
        logEvent("Task removed: ${taskToRemove?.code ?: taskId}")
        recalculate()
    }

    fun addResource(resource: Resource) {
        _resources.value = _resources.value + resource
        logEvent("Resource commissioned: ${resource.code} (${resource.name})")
        recalculate()
    }

    fun removeResource(resourceId: String) {
        val resToRemove = _resources.value.find { it.id == resourceId }
        _resources.value = _resources.value.filterNot { it.id == resourceId }
        logEvent("Resource decommissioned: ${resToRemove?.code ?: resourceId}")
        recalculate()
    }

    fun resetToCurrentPreset() {
        val preset = _currentPreset.value
        _tasks.value = preset.defaultTasks
        _resources.value = preset.defaultResources
        logEvent("Reset scenario to preset defaults: ${preset.title}")
        recalculate()
    }

    private fun logEvent(message: String) {
        val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        _disruptionLogs.value = listOf("[$timestamp] $message") + _disruptionLogs.value.take(19)
    }

    /**
     * Generates downloadable JSON simulation report
     */
    fun generateJsonReport(): String {
        val comparison = _comparisonResult.value ?: return "{}"
        val opt = comparison.optimizedMetrics
        val base = comparison.baselineMetrics

        val sb = StringBuilder()
        sb.append("{\n")
        sb.append("  \"project\": \"AeroOptima AI\",\n")
        sb.append("  \"problemStatement\": \"SIH26250 - Air Power: Dynamic Air Operations & Resource Optimisation\",\n")
        sb.append("  \"disclaimer\": \"Synthetic Educational Simulation Data Only - No Operational Military Systems\",\n")
        sb.append("  \"timestamp\": \"${SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).format(Date())}\",\n")
        sb.append("  \"scenario\": {\n")
        sb.append("    \"id\": \"${_currentPreset.value.id}\",\n")
        sb.append("    \"title\": \"${_currentPreset.value.title}\",\n")
        sb.append("    \"totalTasks\": ${_tasks.value.size},\n")
        sb.append("    \"totalResources\": ${_resources.value.size}\n")
        sb.append("  },\n")
        sb.append("  \"metricsComparison\": {\n")
        sb.append("    \"efficiencyImprovementPercent\": ${comparison.efficiencyImprovementPercent},\n")
        sb.append("    \"delayReductionPercent\": ${comparison.delayReductionPercent},\n")
        sb.append("    \"optimized\": {\n")
        sb.append("      \"scheduledMissions\": ${opt.scheduledCount},\n")
        sb.append("      \"unfeasibleBottlenecks\": ${opt.unfeasibleCount},\n")
        sb.append("      \"totalDelayMinutes\": ${opt.totalDelayMinutes},\n")
        sb.append("      \"avgDelayMinutes\": ${opt.avgDelayMinutes},\n")
        sb.append("      \"utilisationPercentage\": ${opt.resourceUtilisationPercentage},\n")
        sb.append("      \"criticalSuccessRate\": ${opt.criticalMissionsSuccessRate},\n")
        sb.append("      \"objectiveScore\": ${opt.objectiveFunctionScore}\n")
        sb.append("    },\n")
        sb.append("    \"baseline\": {\n")
        sb.append("      \"scheduledMissions\": ${base.scheduledCount},\n")
        sb.append("      \"unfeasibleBottlenecks\": ${base.unfeasibleCount},\n")
        sb.append("      \"totalDelayMinutes\": ${base.totalDelayMinutes},\n")
        sb.append("      \"avgDelayMinutes\": ${base.avgDelayMinutes},\n")
        sb.append("      \"utilisationPercentage\": ${base.resourceUtilisationPercentage},\n")
        sb.append("      \"criticalSuccessRate\": ${base.criticalMissionsSuccessRate},\n")
        sb.append("      \"objectiveScore\": ${base.objectiveFunctionScore}\n")
        sb.append("    }\n")
        sb.append("  },\n")
        sb.append("  \"optimizedAllocationPlan\": [\n")
        val slots = comparison.optimizedPlan
        slots.forEachIndexed { i, slot ->
            val isLast = i == slots.size - 1
            sb.append("    {\n")
            sb.append("      \"taskCode\": \"${slot.task.code}\",\n")
            sb.append("      \"taskName\": \"${slot.task.name}\",\n")
            sb.append("      \"priority\": \"${slot.task.priority.label}\",\n")
            sb.append("      \"assignedResource\": \"${slot.resource?.code ?: "UNASSIGNED"}\",\n")
            sb.append("      \"scheduledStart\": ${slot.scheduledStart},\n")
            sb.append("      \"scheduledEnd\": ${slot.scheduledEnd},\n")
            sb.append("      \"delayMinutes\": ${slot.delayMinutes},\n")
            sb.append("      \"isFeasible\": ${slot.isFeasible}\n")
            sb.append("    }${if (isLast) "" else ","}\n")
        }
        sb.append("  ]\n")
        sb.append("}\n")
        return sb.toString()
    }

    /**
     * Generates downloadable CSV simulation report
     */
    fun generateCsvReport(): String {
        val comparison = _comparisonResult.value ?: return ""
        val sb = StringBuilder()
        sb.append("Task Code,Task Name,Mission Type,Priority,Required Role,Assigned Resource,Start (T+m),End (T+m),Delay (m),Deadline (T+m),Feasible,Rationale\n")
        for (slot in comparison.optimizedPlan) {
            val rCode = slot.resource?.code ?: "UNASSIGNED"
            val rationaleClean = slot.rationale.replace(",", ";").replace("\"", "'")
            sb.append("${slot.task.code},\"${slot.task.name}\",${slot.task.missionType.name},${slot.task.priority.label},${slot.task.missionType.requiredRole.shortTag},$rCode,${slot.scheduledStart},${slot.scheduledEnd},${slot.delayMinutes},${slot.task.deadlineMinutes},${slot.isFeasible},\"$rationaleClean\"\n")
        }
        return sb.toString()
    }
}
