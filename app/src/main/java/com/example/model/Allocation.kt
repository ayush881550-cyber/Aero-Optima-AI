package com.example.model

data class AllocationSlot(
    val task: Task,
    val resource: Resource?,
    val scheduledStart: Int,      // minutes from T+0
    val scheduledEnd: Int,        // scheduledStart + duration
    val delayMinutes: Int,        // max(0, scheduledStart - task.earliestStartTime)
    val meetsDeadline: Boolean,   // scheduledEnd <= task.deadlineMinutes
    val isFeasible: Boolean,
    val unfeasibleReason: String? = null,
    val rationale: String = ""    // why this resource was chosen
)

data class PlanMetrics(
    val totalMissions: Int,
    val scheduledCount: Int,
    val unfeasibleCount: Int,
    val totalDelayMinutes: Int,
    val avgDelayMinutes: Double,
    val avgMissionCompletionTime: Double, // in minutes
    val resourceUtilisationPercentage: Double, // 0.0 - 100.0%
    val resourceIdleWastePercentage: Double,   // fragmented downtime
    val criticalMissionsSuccessRate: Double,   // percentage of Critical/High completed
    val objectiveFunctionScore: Double        // calculated objective value Z
)

data class BottleneckAlert(
    val id: String,
    val severity: AlertSeverity,
    val title: String,
    val description: String,
    val affectedTaskCodes: List<String>,
    val recommendation: String
)

enum class AlertSeverity {
    INFO,
    WARNING,
    CRITICAL
}

data class AllocationRationale(
    val taskCode: String,
    val selectedResourceCode: String,
    val primaryReason: String,
    val alternativesConsidered: List<AlternativeEvaluation>,
    val constraintChecks: List<ConstraintCheckResult>
)

data class AlternativeEvaluation(
    val resourceCode: String,
    val accepted: Boolean,
    val rejectionReason: String? = null
)

data class ConstraintCheckResult(
    val constraintName: String,
    val satisfied: Boolean,
    val detail: String
)

data class OptimizationComparison(
    val baselinePlan: List<AllocationSlot>,
    val optimizedPlan: List<AllocationSlot>,
    val baselineMetrics: PlanMetrics,
    val optimizedMetrics: PlanMetrics,
    val efficiencyImprovementPercent: Double,
    val delayReductionPercent: Double,
    val bottlenecks: List<BottleneckAlert>,
    val rationales: Map<String, AllocationRationale>,
    val simulationTimestamp: Long = System.currentTimeMillis()
)
