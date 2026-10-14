package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun ScenarioLabScreen(
    currentPreset: ScenarioPreset,
    allPresets: List<ScenarioPreset>,
    onSelectPreset: (ScenarioPreset) -> Unit,
    tasks: List<Task>,
    resources: List<Resource>,
    onToggleGrounding: (String) -> Unit,
    onInjectEmergency: () -> Unit,
    onResetScenario: () -> Unit,
    onAddTask: (Task) -> Unit,
    onRemoveTask: (String) -> Unit,
    onAddResource: (Resource) -> Unit,
    onRemoveResource: (String) -> Unit,
    showAddTaskDialog: Boolean,
    onShowAddTaskDialogChange: (Boolean) -> Unit,
    showAddResourceDialog: Boolean,
    onShowAddResourceDialogChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(0) } // 0: Tasks, 1: Resources

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Scenario Simulation Lab",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Customize operational parameters, test stress surges, and inject dynamic disruptions into the simulator.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Preset selector cards
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("scenario_presets_card"),
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(NavyCardBorder))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "SELECT SCENARIO PRESET",
                        color = CyanAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    allPresets.forEach { preset ->
                        val isSelected = preset.id == currentPreset.id
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) NavySurfaceVariant else Color.Transparent)
                                .border(
                                    1.dp,
                                    if (isSelected) CyanAccent else NavyCardBorder.copy(alpha = 0.4f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = preset.title,
                                        color = TextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = preset.subtitle,
                                        color = if (isSelected) CyanAccent else TextMuted,
                                        fontSize = 10.sp
                                    )
                                }

                                Button(
                                    onClick = { onSelectPreset(preset) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSelected) ElectricBlue else NavyCardBorder
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text(
                                        text = if (isSelected) "Active" else "Load",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Live Disruption Injector Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("disruption_controls_card"),
                colors = CardDefaults.cardColors(containerColor = NavySurfaceVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "DYNAMIC DISRUPTION SIMULATOR",
                        color = StatusWarning,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onInjectEmergency,
                            colors = ButtonDefaults.buttonColors(containerColor = StatusDanger),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("scramble_qra_button")
                        ) {
                            Icon(Icons.Default.FlashOn, "Emergency", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Inject QRA Alert", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onResetScenario,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Refresh, "Reset", modifier = Modifier.size(16.dp), tint = TextSecondary)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reset Preset", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Tabs to toggle Task Manager vs Resource Manager
        item {
            TabRow(
                selectedTabIndex = activeTab,
                containerColor = NavySurface,
                contentColor = CyanAccent,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .border(1.dp, NavyCardBorder, RoundedCornerShape(10.dp))
            ) {
                Tab(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    text = { Text("Abstract Tasks (${tasks.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    text = { Text("Fleet Resources (${resources.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                )
            }
        }

        if (activeTab == 0) {
            // Task List Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Configured Tasks", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Button(
                        onClick = { onShowAddTaskDialogChange(true) },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(30.dp)
                            .testTag("add_task_button")
                    ) {
                        Icon(Icons.Default.Add, "Add", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Task", fontSize = 11.sp)
                    }
                }
            }

            items(tasks, key = { it.id }) { task ->
                TaskConfigRow(
                    task = task,
                    onDelete = { onRemoveTask(task.id) }
                )
            }
        } else {
            // Resource List Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Fleet Resources", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Button(
                        onClick = { onShowAddResourceDialogChange(true) },
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(30.dp)
                            .testTag("add_resource_button")
                    ) {
                        Icon(Icons.Default.Add, "Add", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Asset", fontSize = 11.sp)
                    }
                }
            }

            items(resources, key = { it.id }) { resource ->
                ResourceConfigRow(
                    resource = resource,
                    onToggleGrounding = { onToggleGrounding(resource.id) },
                    onDelete = { onRemoveResource(resource.id) }
                )
            }
        }
    }

    // Add Task Dialog Modal
    if (showAddTaskDialog) {
        AddTaskDialog(
            nextCodeNumber = tasks.size + 1,
            onDismiss = { onShowAddTaskDialogChange(false) },
            onConfirm = onAddTask
        )
    }

    // Add Resource Dialog Modal
    if (showAddResourceDialog) {
        AddResourceDialog(
            nextCodeNumber = resources.size + 1,
            onDismiss = { onShowAddResourceDialogChange(false) },
            onConfirm = onAddResource
        )
    }
}

@Composable
private fun TaskConfigRow(task: Task, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(NavyCardBorder))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(task.code, color = CyanAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(task.name, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
                Text(
                    text = "${task.missionType.displayName} • Pri: ${task.priority.label} • Dur: ${task.durationMinutes}m • Max: T+${task.deadlineMinutes}m",
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }

            IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Delete, "Delete", tint = StatusDanger.copy(alpha = 0.7f), modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun ResourceConfigRow(
    resource: Resource,
    onToggleGrounding: () -> Unit,
    onDelete: () -> Unit
) {
    val isGrounded = resource.status == OperationalStatus.MAINTENANCE_STANDBY
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NavySurface),
        shape = RoundedCornerShape(8.dp),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = androidx.compose.ui.graphics.SolidColor(
                if (isGrounded) StatusDanger.copy(alpha = 0.5f) else NavyCardBorder
            )
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(resource.code, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("• ${resource.name}", color = TextSecondary, fontSize = 12.sp)
                }
                Text(
                    text = "Role: ${resource.role.shortTag} • Cap: ${resource.totalCapacity} • Turnaround: ${resource.turnaroundMinutes}m",
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onToggleGrounding,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isGrounded) StatusSuccess else StatusDanger
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    modifier = Modifier.height(26.dp)
                ) {
                    Text(if (isGrounded) "Restore" else "Ground", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                IconButton(onClick = onDelete, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Delete, "Delete", tint = TextMuted, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun AddTaskDialog(
    nextCodeNumber: Int,
    onDismiss: () -> Unit,
    onConfirm: (Task) -> Unit
) {
    val codeLetter = ('A' + (nextCodeNumber - 1).coerceAtMost(25)).toString()
    var taskName by remember { mutableStateOf("Strategic CAP Sector $codeLetter") }
    var selectedType by remember { mutableStateOf(MissionType.COMBAT_AIR_PATROL) }
    var selectedPriority by remember { mutableStateOf(TaskPriority.HIGH) }
    var duration by remember { mutableStateOf("90") }
    var deadline by remember { mutableStateOf("240") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Configure New Task", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = taskName,
                    onValueChange = { taskName = it },
                    label = { Text("Task Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it.filter { c -> c.isDigit() } },
                        label = { Text("Duration (min)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = deadline,
                        onValueChange = { deadline = it.filter { c -> c.isDigit() } },
                        label = { Text("Deadline (T+m)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Text("Mission Type & Role Required:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    MissionType.entries.take(3).forEach { type ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedType == type,
                                onClick = { selectedType = type }
                            )
                            Text(type.displayName, color = TextPrimary, fontSize = 11.sp)
                        }
                    }
                }

                Text("Task Priority:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(TaskPriority.CRITICAL, TaskPriority.HIGH, TaskPriority.MEDIUM).forEach { pri ->
                        FilterChip(
                            selected = selectedPriority == pri,
                            onClick = { selectedPriority = pri },
                            label = { Text(pri.label, fontSize = 10.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val durInt = duration.toIntOrNull() ?: 90
                    val deadInt = deadline.toIntOrNull() ?: 240
                    val newTask = Task(
                        id = "custom_${System.currentTimeMillis()}",
                        code = "Task $codeLetter",
                        name = taskName.ifBlank { "Custom Air Mission $codeLetter" },
                        missionType = selectedType,
                        priority = selectedPriority,
                        requiredCapacity = 1,
                        durationMinutes = durInt,
                        earliestStartTime = 0,
                        deadlineMinutes = deadInt
                    )
                    onConfirm(newTask)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
            ) {
                Text("Add Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextMuted) }
        },
        containerColor = NavySurface
    )
}

@Composable
private fun AddResourceDialog(
    nextCodeNumber: Int,
    onDismiss: () -> Unit,
    onConfirm: (Resource) -> Unit
) {
    var resName by remember { mutableStateOf("Squadron $nextCodeNumber - Falcon") }
    var selectedRole by remember { mutableStateOf(ResourceRole.MULTIROLE_FIGHTER) }
    var capacity by remember { mutableStateOf("2") }
    var turnaround by remember { mutableStateOf("30") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Commission Fleet Asset", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = resName,
                    onValueChange = { resName = it },
                    label = { Text("Asset / Squadron Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = capacity,
                        onValueChange = { capacity = it.filter { c -> c.isDigit() } },
                        label = { Text("Capacity") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = turnaround,
                        onValueChange = { turnaround = it.filter { c -> c.isDigit() } },
                        label = { Text("Turnaround (m)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Text("Asset Role:", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ResourceRole.entries.forEach { role ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(
                                selected = selectedRole == role,
                                onClick = { selectedRole = role }
                            )
                            Text(role.title, color = TextPrimary, fontSize = 11.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val capInt = capacity.toIntOrNull() ?: 2
                    val taInt = turnaround.toIntOrNull() ?: 30
                    val newRes = Resource(
                        id = "res_${System.currentTimeMillis()}",
                        code = "Resource $nextCodeNumber",
                        name = resName.ifBlank { "Squadron $nextCodeNumber" },
                        role = selectedRole,
                        totalCapacity = capInt,
                        turnaroundMinutes = taInt,
                        status = OperationalStatus.AVAILABLE
                    )
                    onConfirm(newRes)
                },
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
            ) {
                Text("Commission Asset")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextMuted) }
        },
        containerColor = NavySurface
    )
}
