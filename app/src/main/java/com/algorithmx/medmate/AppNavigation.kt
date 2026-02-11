package com.algorithmx.medmate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.algorithmx.medmate.basic.UniversalRenderer
import com.algorithmx.medmate.screens.HomeScreen
import com.algorithmx.medmate.utils.JsonLoader
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.algorithmx.medmate.basic.ContentBlock
import com.algorithmx.medmate.screens.CaseListScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "home") {

        // 1. Home Screen
        composable("home") {
            HomeScreen(onCategorySelected = { category ->
                navController.navigate("caseList/$category")
            })
        }
        composable("caseList/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: "short"
            CaseListScreen(
                category = category,
                onCaseClicked = { fileName, title ->
                    navController.navigate("details/$fileName/$title")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("details/{fileName}/{title}") { backStackEntry ->
            val fileName = backStackEntry.arguments?.getString("fileName") ?: "error.json"
            val title = backStackEntry.arguments?.getString("title") ?: "Topic"
            val context = LocalContext.current

            // FIX: Load in background, start with empty list to prevent blocking
            val blocks by produceState(initialValue = emptyList<ContentBlock>(), key1 = fileName) {
                value = withContext(Dispatchers.IO) {
                    JsonLoader.loadChapter(context, fileName)
                }
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(title) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        }
                    )
                }
            ) { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    // Optional: Show loading spinner while blocks are empty
                    if (blocks.isEmpty()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        UniversalRenderer(blocks = blocks)
                    }
                }
            }
        }
    }
}