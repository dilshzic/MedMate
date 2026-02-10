package com.algorithmx.medmate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.algorithmx.medmate.basic.ListItem

// 1. HEADER BLOCK
@Composable
fun HeaderBlock(text: String, level: Int) {
    val style = if (level == 1) {
        MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    } else {
        MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
    }

    Text(
        text = text,
        style = style,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

// 2. WARNING / CALLOUT BLOCK
@Composable
fun CalloutBlock(text: String, variant: String = "info") {
    val (bgColor, borderColor) = when (variant) {
        "warning" -> Pair(Color(0xFFFFF3E0), Color(0xFFFFB74D)) // Orange
        else -> Pair(Color(0xFFE3F2FD), Color(0xFF64B5F6))      // Blue
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            // You can add an Icon here later
            Text(text = text, style = MaterialTheme.typography.bodyMedium, color = Color.Black)
        }
    }
}

// 3. TABLE BLOCK
@Composable
fun TableBlock(headers: List<String>, rows: List<List<String>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
    ) {
        // Render Header Row
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp)
        ) {
            headers.forEach { header ->
                Text(
                    text = header,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        // Render Data Rows
        rows.forEachIndexed { index, row ->
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .background(if (index % 2 == 0) Color.Transparent else Color(0xFFF5F5F5)) // Zebra striping
            ) {
                row.forEach { cell ->
                    Text(text = cell, modifier = Modifier.weight(1f), fontSize = 14.sp)
                }
            }
            if (index < rows.size - 1) Divider(color = Color.LightGray, thickness = 0.5.dp)
        }
    }
}

// 4. BULLET LIST BLOCK (Recursive)
@Composable
fun BulletListBlock(items: List<ListItem>) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        items.forEach { item ->
            Row(modifier = Modifier.padding(bottom = 4.dp)) {
                Text(text = "•", modifier = Modifier.padding(end = 8.dp), fontWeight = FontWeight.Bold)
                Column {
                    Text(text = item.text, style = MaterialTheme.typography.bodyLarge)

                    // Render sub-items if they exist
                    item.subItems?.forEach { sub ->
                        Row(modifier = Modifier.padding(top = 2.dp)) {
                            Text(text = "-", modifier = Modifier.padding(end = 8.dp), color = Color.Gray)
                            Text(text = sub, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}