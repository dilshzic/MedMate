package com.algorithmx.medmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.algorithmx.medmate.ui.theme.MedMateTheme
import com.example.medicalguide.ui.UniversalRenderer

// --- 1. MOVE DATA HERE (Top Level) ---
// Moving this outside the class makes it accessible to both MainActivity and the Preview
val sampleData = listOf(
    ContentBlock(
        type = "header",
        text = "General Exam & Systemic Clues",
        level = 1
    ),
    ContentBlock(
        type = "callout",
        text = "General examination should be brief and highly relevant to the system affected."
    ),
    ContentBlock(
        type = "table",
        tableHeaders = listOf("Systemic Finding", "General Clue"),
        tableRows = listOf(
            listOf("Mitral Regurgitation", "Marfanoid Features"),
            listOf("Pleural Effusion", "Clubbing, Yellow Nails"),
            listOf("Liver Failure", "Icterus, Spider Naevi")
        )
    ),
    ContentBlock(
        type = "header",
        text = "Common Causes of Clubbing",
        level = 2
    ),
    ContentBlock(
        type = "list",
        items = listOf(
            ListItem(
                "Respiratory",
                listOf("Bronchial Ca", "Suppurative Lung Disease", "Fibrosis")
            ),
            ListItem("Cardiovascular", listOf("Infective Endocarditis", "Cyanotic CHD")),
            ListItem("Gastrointestinal", listOf("Cirrhosis", "IBD"))
        )
    )
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MedMateTheme {
                // Scaffold handles the white background and system bar padding
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // --- 2. CALL RENDERER HERE ---
                    // We wrap it in a Box to apply the 'innerPadding' so content
                    // doesn't go behind the status bar/navigation bar
                    Box(modifier = Modifier.padding(innerPadding)) {
                        UniversalRenderer(blocks = sampleData)
                    }
                }
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
        UniversalRenderer(blocks = sampleData)
    }
}