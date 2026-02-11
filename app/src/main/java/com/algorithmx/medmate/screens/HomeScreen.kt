package com.algorithmx.medmate.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(onCategorySelected: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select Case Type", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onCategorySelected("short") },
            modifier = Modifier.fillMaxWidth().height(80.dp)
        ) {
            Text("Short Cases", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onCategorySelected("long") },
            modifier = Modifier.fillMaxWidth().height(80.dp)
        ) {
            Text("Long Cases", fontSize = 20.sp)
        }
    }
}