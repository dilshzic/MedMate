package com.algorithmx.medmate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.algorithmx.medmate.basic.ListItem
import coil.compose.AsyncImage
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.algorithmx.medmate.basic.ContentBlock
import com.algorithmx.medmate.basic.RenderSingleBlock
import com.algorithmx.medmate.basic.TabItem

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
fun BulletListBlock(items: List<ListItem>, depth: Int = 0) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        items.forEach { item ->
            Row(modifier = Modifier.padding(start = (depth * 16).dp, bottom = 4.dp)) {
                Text(
                    text = "•",
                    modifier = Modifier.padding(end = 8.dp),
                    fontWeight = FontWeight.Bold
                )
                Column {
                    Text(text = item.text, style = MaterialTheme.typography.bodyLarge)

                    // RECURSION: The function calls itself for sub-items
                    item.subItems?.let { subs ->
                        BulletListBlock(items = subs, depth = depth + 1)
                    }
                }
            }
        }
    }
}

@Composable
fun ImageBlock(url: String, caption: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        // The Image Loader
        AsyncImage(
            // We use a helper to load from assets: file:///android_asset/...
            model = "file:///android_asset/$url",
            contentDescription = caption ?: "Medical Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 250.dp) // Limit height so it doesn't take whole screen
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )

        // The Caption (Optional)
        if (!caption.isNullOrEmpty()) {
            Text(
                text = caption,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}


@Composable
fun TabGroupBlock(
    tabs: List<TabItem>,
    onRenderContent: @Composable (ContentBlock) -> Unit // <--- Ensure this exists
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth()) {
        // 1. The Tab Row (Scrollable so 12 CNs fit)
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 16.dp,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                // Default indicator
                androidx.compose.material3.TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                )
            }
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = tab.title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        // 2. The Content of the Selected Tab
        // We reuse the UniversalRenderer to render the blocks inside this tab!
        // Note: We wrap it in a Column because UniversalRenderer usually has a LazyColumn,
        // but nesting LazyColumns is bad.
        // TRICK: We will make a "SimpleRenderer" or just loop through blocks here.

        Column(modifier = Modifier.padding(top = 16.dp)) {
            val currentBlocks = tabs[selectedTabIndex].content

            // Render each block manually to avoid nested LazyColumn crash
            currentBlocks.forEach { block ->
                // Copy-paste your 'when' logic here or extract it to a function
                // Ideally, refactor 'UniversalRenderer' to call a 'RenderBlock' function.
                RenderSingleBlock(block)
            }
        }
    }
}