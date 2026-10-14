package com.example.model

enum class TaskPriority(val level: Int, val label: String, val weight: Double) {
    CRITICAL(5, "Critical", 10.0),
    HIGH(4, "High", 5.0),
    MEDIUM(3, "Medium", 2.5),
    LOW(2, "Low", 1.0),
    ROUTINE(1, "Routine", 0.5)
}

enum class MissionType(val displayName: String, val requiredRole: ResourceRole) {
    COMBAT_AIR_PATROL("Combat Air Patrol (CAP)", ResourceRole.MULTIROLE_FIGHTER),
    TACTICAL_RECONNAISSANCE("Tactical Reconnaissance (ISR)", ResourceRole.RECON_UAV),
    AIRLIFT_LOGISTICS("Airlift & Cargo Transport", ResourceRole.HEAVY_TRANSPORT),
    QUICK_REACTION_ALERT("Quick Reaction Alert (QRA)", ResourceRole.MULTIROLE_FIGHTER),
    AERIAL_REFUELING("Aerial Refueling Orbit", ResourceRole.AIR_REFUELER),
    DISASTER_RELIEF("Humanitarian / Relief Lift", ResourceRole.HEAVY_TRANSPORT)
}

enum class TaskStatus(val label: String) {
    SCHEDULED("Scheduled (Optimal)"),
    BASELINE_DELAYED("Delayed in Baseline"),
    DELAYED("Delayed"),
    BOTTLENECK("Unfeasible / Bottleneck"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed")
}

data class Task(
    val id: String,
    val code: String,              // e.g. "Task A", "Task B", or "Task Alpha"
    val name: String,              // e.g. "Air Space Patrol Grid A"
    val missionType: MissionType,
    val priority: TaskPriority,
    val requiredCapacity: Int = 1, // capacity units needed (e.g. 1 to 3)
    val durationMinutes: Int,      // e.g. 60 min, 90 min, 120 min
    val earliestStartTime: Int,    // minutes from simulation T+0 (e.g. 0, 30, 60)
    val deadlineMinutes: Int,      // absolute deadline from T+0 (e.g. 240 min)
    val isEmergencyDisruption: Boolean = false
)
