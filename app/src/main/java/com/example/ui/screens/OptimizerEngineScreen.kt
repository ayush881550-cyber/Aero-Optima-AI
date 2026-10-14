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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OptimizationComparison
import com.example.ui.theme.*

@Composable
fun OptimizerEngineScreen(
    comparison: OptimizationComparison?,
    disruptionLogs: List<String>,
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
                    text = "AI & Mathematical Optimisation Engine",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Transparent mathematical modeling and constraint satisfaction architecture for SIH26250 operations research.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }
        }

        // Objective Function Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("objective_function_card"),
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
                            text = "MATHEMATICAL OBJECTIVE FUNCTION",
                            color = CyanAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CyanAccent.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("MIP Formulation", color = CyanAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Formula Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavyBackground)
                            .border(1.dp, NavyCardBorder, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "min Z = Σ [ w_i · Delay_i + P_unassigned · u_i ] + λ Σ Idle_Waste_j - μ Σ (w_i · s_i)",
                                color = TextPrimary,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // Variable breakdown
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        FormulaVariableExplanation(
                            symbol = "w_i",
                            meaning = "Priority Weight coefficient (Critical: 10.0x, High: 5.0x, Medium: 2.5x, Low: 1.0x)."
                        )
                        FormulaVariableExplanation(
                            symbol = "Delay_i",
                            meaning = "Simulated departure delay: max(0, t_start - earliest_start_i)."
                        )
                        FormulaVariableExplanation(
                            symbol = "P_unassigned · u_i",
                            meaning = "Heavy penalty factor (120x) applied if mission cannot be scheduled before deadline."
                        )
                        FormulaVariableExplanation(
                            symbol = "λ · Idle_Waste_j",
                            meaning = "Penalizes fragmented gaps between missions on active fleet assets."
                        )
                    }
                }
            }
        }

        // Constraints Checklist Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("constraints_checklist_card"),
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(NavyCardBorder))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "OPERATIONAL CONSTRAINTS ENFORCED",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    ConstraintItem(
                        title = "1. Role & Capability Compatibility",
                        math = "x_ijt = 1 ⟹ Role(j) == RequiredRole(i)",
                        desc = "Combat Air Patrols require multirole fighters; ISR sweeps require recon UAV wings. Incompatible assignments strictly rejected."
                    )

                    ConstraintItem(
                        title = "2. Turnaround & Maintenance Rest Interval",
                        math = "t_start(b) ≥ t_end(a) + Turnaround_Minutes(j)",
                        desc = "Mandatory 20-45 min ground preparation, weapons loading, and refueling buffer before an asset can fly its next sortie."
                    )

                    ConstraintItem(
                        title = "3. Temporal Feasibility Envelope",
                        math = "t_start ≥ earliest_start AND t_end ≤ deadline",
                        desc = "Sorties cannot launch prior to intelligence availability and must return before target airspace closure / weather front."
                    )

                    ConstraintItem(
                        title = "4. Fleet Capacity Envelope",
                        math = "Concurrent_Active_Sorties(j, t) ≤ Total_Capacity(j)",
                        desc = "Simultaneous missions assigned to a squadron cannot exceed operational aircraft availability."
                    )
                }
            }
        }

        // Algorithm Comparison Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("algorithm_comparison_card"),
                colors = CardDefaults.cardColors(containerColor = NavySurface),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(NavyCardBorder))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "SOLVER COMPARISON (BASELINE VS OPTIMIZED)",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    AlgorithmRow(
                        dimension = "Scheduling Strategy",
                        baseline = "Greedy FIFO (First In First Out)",
                        optimized = "Priority Slack Ratio + MIP Heuristic"
                    )

                    AlgorithmRow(
                        dimension = "Priority Inversions",
                        baseline = "Frequent (Low priority blocks Critical)",
                        optimized = "Zero (Critical protected first)"
                    )

                    AlgorithmRow(
                        dimension = "Turnaround Packing",
                        baseline = "Uncoordinated fragmented gaps",
                        optimized = "Synchronized minimal delay gaps"
                    )

                    AlgorithmRow(
                        dimension = "Bottleneck Handling",
                        baseline = "Silent drops without alternatives",
                        optimized = "Automatic bottleneck alert & rationale"
                    )
                }
            }
        }

        // Live Disruption & Solver Logs
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("solver_logs_card"),
                colors = CardDefaults.cardColors(containerColor = NavyBackground),
                shape = RoundedCornerShape(12.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(NavyCardBorder))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SOLVER EXECUTION STREAM",
                            color = CyanAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "LIVE",
                            color = StatusSuccess,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    disruptionLogs.take(6).forEach { log ->
                        Text(
                            text = log,
                            color = TextSecondary,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 10.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FormulaVariableExplanation(symbol: String, meaning: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = symbol,
            color = CyanAccent,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = meaning,
            color = TextSecondary,
            fontSize = 11.sp,
            lineHeight = 15.sp
        )
    }
}

@Composable
private fun ConstraintItem(title: String, math: String, desc: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(NavySurfaceVariant.copy(alpha = 0.5f))
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(title, color = TextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text(math, color = CyanAccent, fontFamily = FontFamily.Monospace, fontSize = 10.sp)
        Text(desc, color = TextMuted, fontSize = 10.sp, lineHeight = 13.sp)
    }
}

@Composable
private fun AlgorithmRow(dimension: String, baseline: String, optimized: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(dimension, color = TextPrimary, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Baseline: $baseline", color = StatusWarning, fontSize = 10.sp, modifier = Modifier.weight(1f))
            Text("Optimized: $optimized", color = StatusSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        }
        HorizontalDivider(color = NavyCardBorder.copy(alpha = 0.4f), modifier = Modifier.padding(top = 4.dp))
    }
}
