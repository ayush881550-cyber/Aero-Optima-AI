package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Tune
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
fun TimelineGanttChart(
    resources: List<Resource>,
    optimizedSlots: List<AllocationSlot>,
    baselineSlots: List<AllocationSlot>,
    modifier: Modifier = Modifier
) {
    var showOptimized by remember { mutableStateOf(true) }
    var selectedSlot by remember { mutableStateOf<AllocationSlot?>(null) }

    val currentSlots = if (showOptimized) optimizedSlots else baselineSlots
    val totalTimelineMinutes = 360
    val minuteWidthDp = 1.35.dp // scale factor for timeline width (360m ≈ 486dp)
    val totalWidthDp = (totalTimelineMinutes * 1.35).dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(NavySurface)
            .border(1.dp, NavyCardBorder, RoundedCornerShape(12.dp))
            .padding(14.dp)
            .testTag("timeline_gantt_container")
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "OPERATIONS TIMELINE (GANTT)",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = if (showOptimized) "Optimized: Zero overlaps, minimal turnaround downtime" else "Baseline FIFO: Uncoordinated gaps & delay cascade",
                        color = if (showOptimized) CyanAccent else StatusWarning,
                        fontSize = 11.sp
                    )
                }

                // Schedule Switcher
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NavySurfaceVariant)
                        .padding(2.dp)
                ) {
                    FilterChip(
                        selected = showOptimized,
                        onClick = { showOptimized = true },
                        label = { Text("Optimal", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricBlue,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.height(28.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    FilterChip(
                        selected = !showOptimized,
                        onClick = { showOptimized = false },
                        label = { Text("Baseline", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NavyCardBorder,
                            selectedLabelColor = TextPrimary
                        ),
                        modifier = Modifier.height(28.dp)
                    )
                }
            }

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                PriorityLegendPill(color = StatusDanger, label = "Critical")
                PriorityLegendPill(color = StatusWarning, label = "High")
                PriorityLegendPill(color = CyanAccent, label = "Medium")
                PriorityLegendPill(color = SkyBlue, label = "Low")
                PriorityLegendPill(color = Color(0xFF64748B), label = "Cooldown (Turnaround)")
            }

            // Horizontal Scrollable Gantt Canvas
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NavyBackground)
                    .border(1.dp, NavyCardBorder.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.horizontalScroll(scrollState)
                ) {
                    // Resource Lane Labels (Sticky-like left column)
                    Column(
                        modifier = Modifier.width(96.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Header spacer
                        Text(
                            text = "ASSET",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.height(20.dp)
                        )

                        resources.forEach { res ->
                            Column(
                                modifier = Modifier
                                    .height(48.dp)
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = res.code,
                                    color = TextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = res.role.shortTag,
                                    color = CyanAccent,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }

                    // Timeline ruler and track
                    Column(
                        modifier = Modifier.width(totalWidthDp)
                    ) {
                        // Ruler row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            for (t in 0..totalTimelineMinutes step 60) {
                                Text(
                                    text = "T+${t}m",
                                    color = TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Divider rule
                        HorizontalDivider(color = NavyCardBorder, thickness = 1.dp)

                        Spacer(modifier = Modifier.height(6.dp))

                        // Lanes
                        resources.forEach { res ->
                            val slotsForRes = currentSlots.filter { it.resource?.id == res.id && it.isFeasible }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NavySurfaceVariant.copy(alpha = 0.4f))
                            ) {
                                if (res.status == OperationalStatus.MAINTENANCE_STANDBY) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(StatusDanger.copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "MAINTENANCE STANDBY / GROUNDED",
                                            color = StatusDanger,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    slotsForRes.forEach { slot ->
                                        val startOffsetDp = (slot.scheduledStart * 1.35).dp
                                        val missionWidthDp = (slot.task.durationMinutes * 1.35).dp
                                        val turnaroundWidthDp = (res.turnaroundMinutes * 1.35).dp

                                        val blockColor = when (slot.task.priority) {
                                            TaskPriority.CRITICAL -> StatusDanger
                                            TaskPriority.HIGH -> StatusWarning
                                            TaskPriority.MEDIUM -> CyanAccent
                                            TaskPriority.LOW -> SkyBlue
                                            TaskPriority.ROUTINE -> Color(0xFF6EE7B7)
                                        }

                                        // Mission Block
                                        Box(
                                            modifier = Modifier
                                                .offset(x = startOffsetDp)
                                                .width(missionWidthDp)
                                                .fillMaxHeight()
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(blockColor)
                                                .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                                                .clickable { selectedSlot = slot }
                                                .padding(horizontal = 4.dp, vertical = 2.dp),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            Column {
                                                Text(
                                                    text = slot.task.code,
                                                    color = Color.Black,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    maxLines = 1
                                                )
                                                Text(
                                                    text = "${slot.task.durationMinutes}m",
                                                    color = Color.Black.copy(alpha = 0.8f),
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }
                                        }

                                        // Turnaround cooldown block
                                        Box(
                                            modifier = Modifier
                                                .offset(x = startOffsetDp + missionWidthDp)
                                                .width(turnaroundWidthDp)
                                                .fillMaxHeight()
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(Color(0xFF334155).copy(alpha = 0.6f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "TA",
                                                color = Color.White.copy(alpha = 0.6f),
                                                fontSize = 8.sp
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }
                }
            }

            // Selected Slot Inspection Panel
            if (selectedSlot != null) {
                val slot = selectedSlot!!
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("timeline_inspector_card"),
                    colors = CardDefaults.cardColors(containerColor = NavySurfaceVariant),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${slot.task.code}: ${slot.task.name}",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            TextButton(
                                onClick = { selectedSlot = null },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text("Close", color = CyanAccent, fontSize = 11.sp)
                            }
                        }

                        Text(
                            text = "Assigned: ${slot.resource?.code ?: "None"} • Priority: ${slot.task.priority.label} • Window: T+${slot.scheduledStart}m → T+${slot.scheduledEnd}m (Delay: ${slot.delayMinutes}m)",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )

                        Text(
                            text = "Rationale: ${slot.rationale}",
                            color = TextMuted,
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PriorityLegendPill(color: Color, label: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Text(text = label, color = TextMuted, fontSize = 9.sp)
    }
}
