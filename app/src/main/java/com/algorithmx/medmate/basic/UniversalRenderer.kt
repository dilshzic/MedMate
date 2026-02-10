package com.example.medicalguide.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.algorithmx.medmate.basic.ContentBlock
import com.algorithmx.medmate.ui.components.*

@Composable
fun UniversalRenderer(blocks: List<ContentBlock>) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {
        items(blocks) { block ->
            when (block.type) {
                "header" -> HeaderBlock(text = block.text ?: "", level = block.level)

                "callout" -> CalloutBlock(text = block.text ?: "", variant = "info")

                "list" -> block.items?.let { BulletListBlock(items = it) }

                "table" -> {
                    if (block.tableHeaders != null && block.tableRows != null) {
                        TableBlock(headers = block.tableHeaders, rows = block.tableRows)
                    }
                }

                // Fallback for unknown types (good for debugging)
                else -> Text("Unknown block type: ${block.type}")
            }
        }
    }
}