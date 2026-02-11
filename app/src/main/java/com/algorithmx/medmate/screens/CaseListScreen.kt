package com.algorithmx.medmate.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.algorithmx.medmate.TocItem
import com.algorithmx.medmate.utils.JsonLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseListScreen(
    category: String,
    onCaseClicked: (String, String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val tocFile = if (category == "long") "long_toc.json" else "Examinationstoc.json"
    val menuItems = remember { JsonLoader.loadMenu(context, tocFile) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${category.replaceFirstChar { it.uppercase() }} Cases") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, "Back") }
                }
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(menuItems) { item ->
                MenuCard(item) { onCaseClicked(item.file, item.title) }
            }
        }
    }
}

@Composable
fun MenuCard(item: TocItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = getIcon(item.icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

// Map the JSON strings to Actual Android Icons
fun getIcon(name: String): ImageVector {
    return when (name) {
        "info" -> Icons.Default.Info
        "heart" -> Icons.Default.Favorite
        "lungs" -> Icons.Default.Notifications // Uses bell shape (distinct)
        "stomach" -> Icons.Default.ShoppingCart // Uses cart shape (distinct volume)
        "brain" -> Icons.Default.Settings // Uses gear shape (complex)
        "arm" -> Icons.Default.ThumbUp
        "leg" -> Icons.Default.Person // Standing person
        "run" -> Icons.Default.Send // Arrow/Motion shape
        "star" -> Icons.Default.Star
        // Fallback for any missing icon
        else -> Icons.AutoMirrored.Filled.List
    }
}