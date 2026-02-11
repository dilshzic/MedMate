package com.algorithmx.medmate.basic

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize

@Composable
fun FlowchartRenderer(data: FlowchartData) {
    // 1. Group nodes by level (Row)
    val rows = data.nodes.groupBy { it.level }.toSortedMap()

    // Store positions of each node to draw lines later
    val nodePositions = remember { mutableStateMapOf<String, Offset>() }

    Box(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // LAYER 1: The Arrows (Canvas)
        // We draw this *behind* the nodes
        Canvas(modifier = Modifier.matchParentSize()) {
            data.connections.forEach { conn ->
                val start = nodePositions[conn.from]
                val end = nodePositions[conn.to]

                if (start != null && end != null) {
                    val path = Path().apply {
                        moveTo(start.x, start.y + 25.dp.toPx()) // Bottom of start node
                        // Draw a smooth cubic bezier curve
                        cubicTo(
                            start.x, start.y + 60.dp.toPx(), // Control point 1 (down)
                            end.x, end.y - 60.dp.toPx(),     // Control point 2 (up)
                            end.x, end.y - 25.dp.toPx()      // Top of end node
                        )
                    }
                    drawPath(
                        path = path,
                        color = Color.Gray,
                        style = Stroke(width = 3.dp.toPx())
                    )
                }
            }
        }

        // LAYER 2: The Nodes (Boxes)
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(60.dp), // Gap between rows
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            rows.forEach { (_, nodesInRow) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Sort by 'order' to keep left-to-right consistent
                    nodesInRow.sortedBy { it.order }.forEach { node ->
                        FlowchartNodeBox(node) { position ->
                            // Save center position for drawing lines
                            nodePositions[node.id] = position
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlowchartNodeBox(node: FlowchartNode, onPositioned: (Offset) -> Unit) {
    val backgroundColor = when (node.type) {
        "start" -> Color(0xFFE3F2FD) // Light Blue
        "decision" -> Color(0xFFFFF3E0) // Light Orange
        "end" -> Color(0xFFFFEBEE) // Light Red
        else -> Color.White
    }

    val borderColor = when (node.type) {
        "start" -> Color(0xFF2196F3)
        "decision" -> Color(0xFFFF9800)
        "end" -> Color(0xFFF44336)
        else -> Color.LightGray
    }

    Box(
        modifier = Modifier
            .width(100.dp) // Fixed width for uniform look
            .height(50.dp) // Fixed height
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .border(2.dp, borderColor, RoundedCornerShape(8.dp))
            .onGloballyPositioned { coordinates ->
                // Calculate the center point of this node relative to the parent Canvas
                val parentPosition = coordinates.positionInParent()
                val size = coordinates.size.toSize()
                val center = Offset(
                    x = parentPosition.x + size.width / 2,
                    y = parentPosition.y + size.height / 2
                )
                onPositioned(center)
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = node.label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            lineHeight = 12.sp,
            modifier = Modifier.padding(4.dp)
        )
    }
}