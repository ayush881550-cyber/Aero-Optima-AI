package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.model.ScenarioPreset
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    comparison: OptimizationComparison?,
    resources: List<Resource>,
    currentPreset: ScenarioPreset,
    onRunOptimization: () -> Unit,
    onInjectEmergency: () -> Unit,
    onSelectPreset: (ScenarioPreset) -> Unit,
    allPresets: List<ScenarioPreset>,
    bannerMessage: String?,
    onDismissBanner: () -> Unit,
    modifier: Modifier = Modifier
) {
    val opt = comparison?.optimizedMetrics
    val base = comparison?.baselineMetrics

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        // Educational Disclaimer
        item {
            EducationalDisclaimer()
        }

        // Success / Event Banner
        if (bannerMessage != null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(StatusSuccess.copy(alpha = 0.15f))
                        .border(1.dp, StatusSuccess, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, "Success", tint = StatusSuccess, modifier = Modifier.size(16.dp))
                            Text(bannerMessage, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        }
                        IconButton(onClick = onDismissBanner, modifier = Modifier.size(20.dp)) {
                            Icon(Icons.Default.Close, "Dismiss", tint = TextSecondary, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }
        }

        // Hero Command Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_hero_card"),
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                shape = RoundedCornerShape(14.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(NavyCardBorder))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(StatusSuccess)
                                )
                                Text(
                                    text = "SOLVER ENGINE ACTIVE",
                                    color = StatusSuccess,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 1.sp
                                )
                            }
                            Text(
                                text = "AeroOptima Command Center",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Scenario: ${currentPreset.title}",
                                color = CyanAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Button(
                            onClick = onRunOptimization,
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                            modifier = Modifier.testTag("run_optimization_hero_button")
                        ) {
                            Icon(Icons.Default.PlayArrow, "Run", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Optimize", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    HorizontalDivider(color = NavyCardBorder.copy(alpha = 0.6f))

                    // Preset quick selector chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Presets:", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        allPresets.forEach { preset ->
                            val isSelected = preset.id == currentPreset.id
                            FilterChip(
                                selected = isSelected,
                                onClick = { onSelectPreset(preset) },
                                label = { Text(preset.title.take(16) + if (preset.title.length > 16) ".." else "", fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyanAccent.copy(alpha = 0.2f),
                                    selectedLabelColor = CyanAccent,
                                    containerColor = NavySurfaceVariant,
                                    labelColor = TextSecondary
                                ),
                                modifier = Modifier.height(26.dp)
                            )
                        }
                    }
                }
            }
        }

        // Primary KPI Metric Grid (2x2)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Simulated Missions",
                        value = "${opt?.scheduledCount ?: 0} / ${opt?.totalMissions ?: 0}",
                        subtitle = "Feasible rate: ${opt?.criticalMissionsSuccessRate?.toInt() ?: 100}%",
                        icon = Icons.Default.FlightTakeoff,
                        deltaText = if ((opt?.unfeasibleCount ?: 0) == 0) "100% Feasible" else "${opt?.unfeasibleCount} Bottlenecks",
                        deltaPositive = (opt?.unfeasibleCount ?: 0) == 0,
                        modifier = Modifier.weight(1f),
                        tag = "metric_missions"
                    )

                    MetricCard(
                        title = "Resource Utilisation",
                        value = "${opt?.resourceUtilisationPercentage?.toInt() ?: 0}%",
                        subtitle = "Baseline: ${base?.resourceUtilisationPercentage?.toInt() ?: 0}%",
                        icon = Icons.Default.Speed,
                        deltaText = "+${((opt?.resourceUtilisationPercentage ?: 0.0) - (base?.resourceUtilisationPercentage ?: 0.0)).coerceAtLeast(0.0).toInt()}% load",
                        deltaPositive = true,
                        modifier = Modifier.weight(1f),
                        tag = "metric_utilisation"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard(
                        title = "Delay Reduction",
                        value = "-${comparison?.delayReductionPercent?.toInt() ?: 0}%",
                        subtitle = "${opt?.totalDelayMinutes ?: 0}m total delay",
                        icon = Icons.Default.Timer,
                        deltaText = "Save ${(base?.totalDelayMinutes ?: 0) - (opt?.totalDelayMinutes ?: 0)} min",
                        deltaPositive = true,
                        modifier = Modifier.weight(1f),
                        tag = "metric_delay"
                    )

                    MetricCard(
                        title = "Optimisation Score",
                        value = "${opt?.objectiveFunctionScore?.toInt() ?: 0} pts",
                        subtitle = "Z-cost (Lower is optimal)",
                        icon = Icons.Default.AutoGraph,
                        deltaText = "+${comparison?.efficiencyImprovementPercent?.toInt() ?: 0}% gain",
                        deltaPositive = true,
                        modifier = Modifier.weight(1f),
                        tag = "metric_efficiency"
                    )
                }
            }
        }

        // Before & After Optimization Comparison Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("before_after_comparison_card"),
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(NavyCardBorder))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "BEFORE VS. AFTER OPTIMISATION",
                            color = TextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CyanAccent.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "MIP + Heuristic",
                                color = CyanAccent,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Comparison Rows
                    ComparisonDataRow(
                        label = "Total Sortie Delays",
                        baselineVal = "${base?.totalDelayMinutes ?: 0} mins",
                        optimizedVal = "${opt?.totalDelayMinutes ?: 0} mins",
                        improvement = "-${comparison?.delayReductionPercent?.toInt() ?: 0}%",
                        isGood = true
                    )

                    ComparisonDataRow(
                        label = "Avg Completion Time",
                        baselineVal = "${base?.avgMissionCompletionTime?.toInt() ?: 0} mins",
                        optimizedVal = "${opt?.avgMissionCompletionTime?.toInt() ?: 0} mins",
                        improvement = "Turnaround synchronized",
                        isGood = true
                    )

                    ComparisonDataRow(
                        label = "Critical Priority Success",
                        baselineVal = "${base?.criticalMissionsSuccessRate?.toInt() ?: 0}%",
                        optimizedVal = "${opt?.criticalMissionsSuccessRate?.toInt() ?: 0}%",
                        improvement = if ((opt?.criticalMissionsSuccessRate ?: 0.0) >= (base?.criticalMissionsSuccessRate ?: 0.0)) "100% Protected" else "Bottleneck",
                        isGood = (opt?.criticalMissionsSuccessRate ?: 0.0) >= (base?.criticalMissionsSuccessRate ?: 0.0)
                    )

                    ComparisonDataRow(
                        label = "Objective Penalty (Z)",
                        baselineVal = "${base?.objectiveFunctionScore?.toInt() ?: 0}",
                        optimizedVal = "${opt?.objectiveFunctionScore?.toInt() ?: 0}",
                        improvement = "${comparison?.efficiencyImprovementPercent?.toInt() ?: 0}% better",
                        isGood = true
                    )
                }
            }
        }

        // Fleet Resource Utilisation Bar Chart
        if (comparison != null) {
            item {
                UtilizationChart(
                    resources = resources,
                    optimizedSlots = comparison.optimizedPlan,
                    baselineSlots = comparison.baselinePlan
                )
            }
        }

        // Timeline Preview / Quick Entry
        if (comparison != null) {
            item {
                TimelineGanttChart(
                    resources = resources,
                    optimizedSlots = comparison.optimizedPlan,
                    baselineSlots = comparison.baselinePlan
                )
            }
        }

        // Operational Bottlenecks & Warnings
        item {
            Text(
                text = "OPERATIONAL BOTTLENECKS & CONSTRAINTS",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        if (comparison?.bottlenecks.isNullOrEmpty()) {
            item {
                Text("No bottlenecks detected.", color = TextMuted, fontSize = 12.sp)
            }
        } else {
            items(comparison?.bottlenecks ?: emptyList()) { alert ->
                BottleneckCard(alert = alert)
            }
        }

        // Quick Disruption Test Bar
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quick_disruption_card"),
                colors = CardDefaults.cardColors(containerColor = NavySurfaceVariant),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Dynamic Disruption Simulation", color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("Inject a surprise emergency mission or ground an asset to test dynamic replanning.", color = TextMuted, fontSize = 10.sp)
                    }
                    Button(
                        onClick = onInjectEmergency,
                        colors = ButtonDefaults.buttonColors(containerColor = StatusDanger),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("inject_emergency_button")
                    ) {
                        Icon(Icons.Default.FlashOn, "Emergency", modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Scramble QRA", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ComparisonDataRow(
    label: String,
    baselineVal: String,
    optimizedVal: String,
    improvement: String,
    isGood: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.2f)) {
            Text(label, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
            Text("Baseline: $baselineVal", color = TextMuted, fontSize = 10.sp)
        }

        Text(
            text = optimizedVal,
            color = CyanAccent,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(0.8f)
        )

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(if (isGood) StatusSuccess.copy(alpha = 0.15f) else StatusWarning.copy(alpha = 0.15f))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = improvement,
                color = if (isGood) StatusSuccess else StatusWarning,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
