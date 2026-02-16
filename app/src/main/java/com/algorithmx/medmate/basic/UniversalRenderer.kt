package com.algorithmx.medmate.basic

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.algorithmx.medmate.ui.components.*

@Composable
fun UniversalRenderer(blocks: List<ContentBlock>) {
    LazyColumn(contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // FIX: Add a 'key'. This dramatically improves scroll performance.
        items(
            items = blocks,
            key = { block ->
                // Generate a unique ID based on content
                block.text.hashCode() + block.type.hashCode() + (block.items?.size ?: 0)
            }
        ) { block ->
            RenderSingleBlock(block)
        }
    }
}

@Composable
fun RenderSingleBlock(block: ContentBlock) {
    when (block.type) {
        "header" -> HeaderBlock(text = block.text ?: "", level = block.level)

        "callout" -> CalloutBlock(text = block.text ?: "")

        // FIXED: Use 'if' instead of '?.let' to avoid returning Unit?
        "list" -> {
            if (block.items != null) {
                BulletListBlock(items = block.items)
            }
        }

        "table" -> {
            if (block.tableHeaders != null && block.tableRows != null) {
                TableBlock(headers = block.tableHeaders, rows = block.tableRows)
            }
        }

        "image" -> ImageBlock(url = block.imageUrl ?: "", caption = block.text)

        // FIXED: Use 'if' and pass the recursive lambda
        "tab_group" -> {
            if (block.tabs != null) {
                TabGroupBlock(tabs = block.tabs) { childBlock ->
                    RenderSingleBlock(childBlock) // Recursive call
                }
            }
        }
        "flowchart" -> {
            if (block.flowchart != null) {
                FlowchartRenderer(data = block.flowchart)
            }
        }
        "dd_table" -> {
            block.ddItems?.let {
                DifferentialDiagnosisBlock(items = it)
            }
        }

        else -> Text("Unknown block type: ${block.type}")
    }
}