package com.algorithmx.medmate.screens.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.algorithmx.medmate.basic.ContentBlock
import com.algorithmx.medmate.basic.UniversalRenderer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    viewModel: EditorViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val blocks by viewModel.blocks.collectAsState()
    val title by viewModel.title.collectAsState()
    val isEditing by viewModel.isEditing.collectAsState()

    // --- FIX: Add this state variable ---
    var showAddBlockSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (isEditing) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { viewModel.updateTitle(it) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(0.7f)
                        )
                    } else {
                        Text(title, maxLines = 1)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (isEditing) viewModel.saveNote() else viewModel.toggleEditMode()
                    }) {
                        Icon(
                            if (isEditing) Icons.Default.Save else Icons.Default.Edit,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (isEditing) {
                // Now this variable exists!
                FloatingActionButton(onClick = { showAddBlockSheet = true }) {
                    Icon(Icons.Default.Add, "Add Block")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).padding(16.dp)) {
            if (isEditing) {
                // --- EDIT MODE ---
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(blocks) { index, block ->
                        BlockWrapper(
                            onDelete = { viewModel.deleteBlock(index) },
                            onMoveUp = { viewModel.moveBlock(index, index - 1) },
                            onMoveDown = { viewModel.moveBlock(index, index + 1) }
                        ) {
                            when (block.type) {
                                "header" -> EditHeaderBlock(block) { viewModel.updateBlock(index, it) }
                                "callout" -> EditTextBlock(block, "Callout Text") { viewModel.updateBlock(index, it) }
                                "list" -> EditListBlock(block) { viewModel.updateBlock(index, it) }
                                "table" -> EditTableBlock(block) { viewModel.updateBlock(index, it) }
                                "dd_table" -> EditDDBlock(block) { viewModel.updateBlock(index, it) }
                                else -> EditTextBlock(block, "Text Content") { viewModel.updateBlock(index, it) }
                            }
                        }
                    }

                    // Add Button at the bottom
                    item {
                        Button(
                            onClick = { viewModel.addBlock(ContentBlock(type = "callout", text = "New Block")) },
                            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                        ) {
                            Text("Add Paragraph")
                        }
                    }
                }
            } else {
                // --- VIEW MODE ---
                UniversalRenderer(blocks = blocks)
            }
        }

        // Now this variable exists!
        if (showAddBlockSheet) {
            BlockCreationSheet(
                onDismiss = { showAddBlockSheet = false },
                onBlockSelected = { newBlock ->
                    viewModel.addBlock(newBlock)
                    showAddBlockSheet = false
                }
            )
        }
    }
}