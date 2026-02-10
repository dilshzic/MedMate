package com.algorithmx.medmate.basic

// This single class handles Headers, Lists, Tables, and Warnings
data class ContentBlock(
    val type: String,               // "header", "list", "table", "callout", "kv_list"
    val text: String? = null,       // Used for headers, warnings, or simple text
    val level: Int = 1,             // For headers (1=Big, 2=Small)
    val items: List<ListItem>? = null, // For bullet lists
    val tableHeaders: List<String>? = null, // For table columns
    val tableRows: List<List<String>>? = null // For table data
)

// Helper class for nested bullet points
data class ListItem(
    val text: String,
    val subItems: List<String>? = null // Universal recursion: list inside a list
)

// Helper for "Key-Value" definitions (e.g., Cachexia: Weight loss)
data class KeyValueItem(
    val key: String,
    val value: String
)