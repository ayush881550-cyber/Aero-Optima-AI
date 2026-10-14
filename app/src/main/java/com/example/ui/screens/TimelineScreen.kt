package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OptimizationComparison
import com.example.model.Resource
import com.example.ui.components.TimelineGanttChart
import com.example.ui.theme.*

@Composable
fun TimelineScreen(
    comparison: OptimizationComparison?,
    resources: List<Resource>,
    modifier: Modifier = Modifier
) {
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
                    text = "Dynamic Operations Timeline",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Synchronized timeline Gantt displaying task allocations, turnaround cooldowns, and role compatibility across simulated assets.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        if (comparison != null) {
            item {
                TimelineGanttChart(
                    resources = resources,
                    optimizedSlots = comparison.optimizedPlan,
                    baselineSlots = comparison.baselinePlan
                )
            }
        }

        // Timeline Operational Insights Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("timeline_insights_card"),
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(NavyCardBorder))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "TIMELINE CONSTRAINTS AUDIT",
                        color = CyanAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )

                    TimelineConstraintItem(
                        title = "Mandatory Turnaround Cooldown",
                        desc = "Ensures 20m - 45m maintenance, safety inspection, and pre-flight refueling interval between consecutive missions on each asset.",
                        status = "Satisfied (100%)",
                        statusColor = StatusSuccess
                    )

                    TimelineConstraintItem(
                        title = "Non-Overlapping Flight Envelopes",
                        desc = "Enforces that simultaneous assignments never exceed the asset's active capacity envelope.",
                        status = "Strictly Enforced",
                        statusColor = StatusSuccess
                    )

                    TimelineConstraintItem(
                        title = "Mission Window Deadlines",
                        desc = "All missions planned to conclude before operational deadline T+X. Delayed tasks flagged with buffer metrics.",
                        status = "Optimized",
                        statusColor = CyanAccent
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineConstraintItem(
    title: String,
    desc: String,
    status: String,
    statusColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(desc, color = TextMuted, fontSize = 10.sp, lineHeight = 14.sp)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(statusColor.copy(alpha = 0.15f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(status, color = statusColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
    }
}
