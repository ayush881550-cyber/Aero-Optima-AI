package com.example.model

enum class ResourceRole(val title: String, val shortTag: String) {
    MULTIROLE_FIGHTER("Multirole Fighter Fleet", "MRF"),
    RECON_UAV("Tactical Recon UAV Wing", "UAV"),
    HEAVY_TRANSPORT("Heavy Tactical Airlift", "AIRLIFT"),
    AIR_REFUELER("Aerial Tanker Squad", "TANKER")
}

enum class OperationalStatus(val displayName: String, val isDeployable: Boolean) {
    AVAILABLE("Available / Combat Ready", true),
    ON_MISSION("Active Sortie", true),
    MAINTENANCE_STANDBY("Maintenance Grounded", false),
    RESTRICTED("Restricted Ops", true)
}

data class Resource(
    val id: String,
    val code: String,             // e.g. "Resource 1", "Resource 2"
    val name: String,             // e.g. "Squadron 101 - Falcon"
    val role: ResourceRole,
    val totalCapacity: Int = 2,   // simultaneous sortie limit or capacity points
    val turnaroundMinutes: Int = 30, // turnaround / maintenance cooldown after mission
    val status: OperationalStatus = OperationalStatus.AVAILABLE,
    val baseStation: String = "Base Alpha"
)
