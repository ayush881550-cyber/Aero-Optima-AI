package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AllocationSlot
import com.example.model.OperationalStatus
import com.example.model.Resource
import com.example.ui.theme.*

@Composable
fun UtilizationChart(
    resources: List<Resource>,
    optimizedSlots: List<AllocationSlot>,
    baselineSlots: List<AllocationSlot>,
    modifier: Modifier = Modifier
) {
    val totalTimeHorizon = 360f // 6 hours

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(NavySurface)
            .border(1.dp, NavyCardBorder, RoundedCornerShape(12.dp))
            .padding(16.dp)
            .testTag("utilization_chart_container")
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "FLEET RESOURCE UTILISATION",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Active sortie hours vs. Turnaround vs. Idle fragmentation",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                // Legend
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LegendItem(color = CyanAccent, label = "Optimized")
                    LegendItem(color = TextMuted.copy(alpha = 0.4f), label = "Baseline")
                }
            }

            // Resource bars
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                resources.forEach { resource ->
                    val optTime = optimizedSlots
                        .filter { it.resource?.id == resource.id && it.isFeasible }
                        .sumOf { it.task.durationMinutes }
                        .toFloat()

                    val baseTime = baselineSlots
                        .filter { it.resource?.id == resource.id && it.isFeasible }
                        .sumOf { it.task.durationMinutes }
                        .toFloat()

                    val optPercent = (optTime / totalTimeHorizon * 100f).coerceIn(0f, 100f)
                    val basePercent = (baseTime / totalTimeHorizon * 100f).coerceIn(0f, 100f)

                    ResourceBarRow(
                        resource = resource,
                        optimizedPercent = optPercent,
                        baselinePercent = basePercent,
                        activeMinutes = optTime.toInt()
                    )
                }
            }
        }
    }
}

@Composable
private fun ResourceBarRow(
    resource: Resource,
    optimizedPercent: Float,
    baselinePercent: Float,
    activeMinutes: Int
) {
    val animatedOptPercent by animateFloatAsState(
        targetValue = optimizedPercent,
        animationSpec = tween(durationMillis = 600),
        label = "opt_progress"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = resource.code,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "• ${resource.name}",
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                if (resource.status == OperationalStatus.MAINTENANCE_STANDBY) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(3.dp))
                            .background(StatusDanger.copy(alpha = 0.2f))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = "GROUNDED",
                            color = StatusDanger,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = "${animatedOptPercent.toInt()}% ($activeMinutes min)",
                color = CyanAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Dual Track progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(NavySurfaceVariant)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cornerRadius = CornerRadius(7.dp.toPx(), 7.dp.toPx())

                // Baseline track (subtle outline/fill)
                if (baselinePercent > 0f) {
                    val baseWidth = (baselinePercent / 100f) * size.width
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.12f),
                        topLeft = Offset.Zero,
                        size = Size(baseWidth, size.height),
                        cornerRadius = cornerRadius
                    )
                }

                // Optimized gradient bar
                if (animatedOptPercent > 0f) {
                    val optWidth = (animatedOptPercent / 100f) * size.width
                    val gradient = Brush.horizontalGradient(
                        colors = if (resource.status == OperationalStatus.MAINTENANCE_STANDBY) {
                            listOf(StatusDanger.copy(alpha = 0.6f), StatusDanger)
                        } else {
                            listOf(ElectricBlue, CyanAccent)
                        }
                    )
                    drawRoundRect(
                        brush = gradient,
                        topLeft = Offset.Zero,
                        size = Size(optWidth, size.height),
                        cornerRadius = cornerRadius
                    )
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 10.sp
        )
    }
}
