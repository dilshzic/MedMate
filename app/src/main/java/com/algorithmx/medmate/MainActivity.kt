package com.algorithmx.medmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

// Make sure these imports match where you actually saved the files I gave you earlier
// If you put them all in the same package, you might not need some of these.
import com.algorithmx.medmate.basic.ContentBlock
import com.algorithmx.medmate.basic.ListItem
import com.algorithmx.medmate.basic.UniversalRenderer
import com.algorithmx.medmate.ui.theme.MedMateTheme
import com.algorithmx.medmate.utils.JsonLoader
import com.algorithmx.medmate.basic.UniversalRenderer

// --- 1. MOVE DATA HERE (Top Level) ---
// Moving this outside the class makes it accessible to both MainActivity and the Preview

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // 1. Load the data from JSON
        mainViewModel
        setContent {
            MedMateTheme {
               AppNavigation()
                }
            }
        }
    }


// --- 3. PREVIEW ---
@Preview(showBackground = true)
@Composable
fun AppPreview() {
    MedMateTheme {
        // We can access sampleData here because it is defined at the top

    }
}