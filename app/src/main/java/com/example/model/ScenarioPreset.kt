package com.example.model

data class ScenarioPreset(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val defaultTasks: List<Task>,
    val defaultResources: List<Resource>
)

object PredefinedScenarios {

    val sih26250Demo = ScenarioPreset(
        id = "sih_demo",
        title = "SIH-26250 Demo Scenario",
        subtitle = "Balanced Multi-Mission Fleet Simulation",
        description = "A standard tactical operations day with 7 abstract missions (CAP, Recon, Airlift, QRA) and 4 diverse resources. Tests priority handling, turnaround cooldowns, and role compatibility.",
        defaultResources = listOf(
            Resource(
                id = "r1",
                code = "Resource 1",
                name = "Squadron Alpha (Fighter)",
                role = ResourceRole.MULTIROLE_FIGHTER,
                totalCapacity = 2,
                turnaroundMinutes = 30,
                status = OperationalStatus.AVAILABLE,
                baseStation = "Forward FOB Alpha"
            ),
            Resource(
                id = "r2",
                code = "Resource 2",
                name = "Flight Bravo (Fighter)",
                role = ResourceRole.MULTIROLE_FIGHTER,
                totalCapacity = 2,
                turnaroundMinutes = 30,
                status = OperationalStatus.AVAILABLE,
                baseStation = "Air Base Central"
            ),
            Resource(
                id = "r3",
                code = "Resource 3",
                name = "Squadron Gamma (Recon UAV)",
                role = ResourceRole.RECON_UAV,
                totalCapacity = 1,
                turnaroundMinutes = 20,
                status = OperationalStatus.AVAILABLE,
                baseStation = "Surveillance Node North"
            ),
            Resource(
                id = "r4",
                code = "Resource 4",
                name = "Unit Delta (Heavy Airlift)",
                role = ResourceRole.HEAVY_TRANSPORT,
                totalCapacity = 2,
                turnaroundMinutes = 45,
                status = OperationalStatus.AVAILABLE,
                baseStation = "Logistics Hub Zulu"
            )
        ),
        defaultTasks = listOf(
            Task(
                id = "t1",
                code = "Task A",
                name = "Combat Air Patrol Sector 1",
                missionType = MissionType.COMBAT_AIR_PATROL,
                priority = TaskPriority.HIGH,
                requiredCapacity = 1,
                durationMinutes = 90,
                earliestStartTime = 0,
                deadlineMinutes = 180
            ),
            Task(
                id = "t2",
                code = "Task B",
                name = "Tactical Border ISR Sweep",
                missionType = MissionType.TACTICAL_RECONNAISSANCE,
                priority = TaskPriority.HIGH,
                requiredCapacity = 1,
                durationMinutes = 120,
                earliestStartTime = 15,
                deadlineMinutes = 240
            ),
            Task(
                id = "t3",
                code = "Task C",
                name = "Strategic Cargo Resupply",
                missionType = MissionType.AIRLIFT_LOGISTICS,
                priority = TaskPriority.MEDIUM,
                requiredCapacity = 1,
                durationMinutes = 150,
                earliestStartTime = 0,
                deadlineMinutes = 300
            ),
            Task(
                id = "t4",
                code = "Task D",
                name = "Emergency Intercept Alert",
                missionType = MissionType.QUICK_REACTION_ALERT,
                priority = TaskPriority.CRITICAL,
                requiredCapacity = 1,
                durationMinutes = 60,
                earliestStartTime = 30,
                deadlineMinutes = 120
            ),
            Task(
                id = "t5",
                code = "Task E",
                name = "High-Res Imagery Survey",
                missionType = MissionType.TACTICAL_RECONNAISSANCE,
                priority = TaskPriority.MEDIUM,
                requiredCapacity = 1,
                durationMinutes = 90,
                earliestStartTime = 90,
                deadlineMinutes = 270
            ),
            Task(
                id = "t6",
                code = "Task F",
                name = "Escort Sector Patrol 2",
                missionType = MissionType.COMBAT_AIR_PATROL,
                priority = TaskPriority.LOW,
                requiredCapacity = 1,
                durationMinutes = 90,
                earliestStartTime = 60,
                deadlineMinutes = 240
            ),
            Task(
                id = "t7",
                code = "Task G",
                name = "Humanitarian Medical Lift",
                missionType = MissionType.DISASTER_RELIEF,
                priority = TaskPriority.HIGH,
                requiredCapacity = 1,
                durationMinutes = 90,
                earliestStartTime = 120,
                deadlineMinutes = 300
            )
        )
    )

    val surgeCrisis = ScenarioPreset(
        id = "surge_crisis",
        title = "High-Stress Surge Crisis",
        subtitle = "Over-Capacity Demand & Bottleneck Resolution",
        description = "Simulates an intense operational spike where mission demands exceed total fleet capabilities. Demonstrates how AeroOptima prioritizes Critical assets while flagging bottlenecks for human commander review.",
        defaultResources = listOf(
            Resource(
                id = "r1",
                code = "Resource 1",
                name = "Squadron Alpha (Fighter)",
                role = ResourceRole.MULTIROLE_FIGHTER,
                totalCapacity = 1,
                turnaroundMinutes = 30,
                status = OperationalStatus.AVAILABLE
            ),
            Resource(
                id = "r2",
                code = "Resource 2",
                name = "Squadron Gamma (Recon UAV)",
                role = ResourceRole.RECON_UAV,
                totalCapacity = 1,
                turnaroundMinutes = 20,
                status = OperationalStatus.AVAILABLE
            ),
            Resource(
                id = "r3",
                code = "Resource 3",
                name = "Unit Delta (Heavy Airlift)",
                role = ResourceRole.HEAVY_TRANSPORT,
                totalCapacity = 1,
                turnaroundMinutes = 40,
                status = OperationalStatus.AVAILABLE
            )
        ),
        defaultTasks = listOf(
            Task("st1", "Task A", "Urgent Sector CAP", MissionType.COMBAT_AIR_PATROL, TaskPriority.CRITICAL, 1, 90, 0, 150),
            Task("st2", "Task B", "Airspace Intercept 1", MissionType.QUICK_REACTION_ALERT, TaskPriority.CRITICAL, 1, 60, 15, 120),
            Task("st3", "Task C", "Emergency Recon Sweep", MissionType.TACTICAL_RECONNAISSANCE, TaskPriority.HIGH, 1, 120, 0, 180),
            Task("st4", "Task D", "Relief Material Airlift", MissionType.DISASTER_RELIEF, TaskPriority.HIGH, 1, 120, 30, 240),
            Task("st5", "Task E", "Secondary Escort CAP", MissionType.COMBAT_AIR_PATROL, TaskPriority.MEDIUM, 1, 90, 45, 180),
            Task("st6", "Task F", "Border Monitoring ISR", MissionType.TACTICAL_RECONNAISSANCE, TaskPriority.MEDIUM, 1, 90, 60, 210),
            Task("st7", "Task G", "Low Priority Logistics", MissionType.AIRLIFT_LOGISTICS, TaskPriority.LOW, 1, 150, 0, 300),
            Task("st8", "Task H", "Routine Air Patrol", MissionType.COMBAT_AIR_PATROL, TaskPriority.ROUTINE, 1, 60, 90, 240)
        )
    )

    val disruptionGrounding = ScenarioPreset(
        id = "disruption_grounding",
        title = "Fleet Disruption & Grounding",
        subtitle = "Unscheduled Maintenance & Real-Time Rerouting",
        description = "A primary resource (Recon UAV) is grounded for emergency inspection. Demonstrates real-time plan adaptation and dynamic replanning under sudden operational disruption.",
        defaultResources = listOf(
            Resource(
                id = "r1",
                code = "Resource 1",
                name = "Squadron Alpha (Fighter)",
                role = ResourceRole.MULTIROLE_FIGHTER,
                totalCapacity = 2,
                turnaroundMinutes = 30,
                status = OperationalStatus.AVAILABLE
            ),
            Resource(
                id = "r2",
                code = "Resource 2",
                name = "Squadron Gamma (Recon UAV)",
                role = ResourceRole.RECON_UAV,
                totalCapacity = 1,
                turnaroundMinutes = 20,
                status = OperationalStatus.MAINTENANCE_STANDBY // Grounded!
            ),
            Resource(
                id = "r3",
                code = "Resource 3",
                name = "Unit Delta (Heavy Airlift)",
                role = ResourceRole.HEAVY_TRANSPORT,
                totalCapacity = 2,
                turnaroundMinutes = 45,
                status = OperationalStatus.AVAILABLE
            )
        ),
        defaultTasks = listOf(
            Task("dt1", "Task A", "Critical Air Defense CAP", MissionType.COMBAT_AIR_PATROL, TaskPriority.CRITICAL, 1, 90, 0, 150),
            Task("dt2", "Task B", "Airborne Sensor Recon", MissionType.TACTICAL_RECONNAISSANCE, TaskPriority.HIGH, 1, 120, 30, 240),
            Task("dt3", "Task C", "Ammo & Rations Transport", MissionType.AIRLIFT_LOGISTICS, TaskPriority.HIGH, 1, 90, 0, 180),
            Task("dt4", "Task D", "Scramble Air Intercept", MissionType.QUICK_REACTION_ALERT, TaskPriority.CRITICAL, 1, 45, 45, 120),
            Task("dt5", "Task E", "Medical Evac Transport", MissionType.DISASTER_RELIEF, TaskPriority.HIGH, 1, 90, 60, 240),
            Task("dt6", "Task F", "Routine Sector Patrol", MissionType.COMBAT_AIR_PATROL, TaskPriority.LOW, 1, 90, 90, 270)
        )
    )

    val allPresets = listOf(sih26250Demo, surgeCrisis, disruptionGrounding)
}
