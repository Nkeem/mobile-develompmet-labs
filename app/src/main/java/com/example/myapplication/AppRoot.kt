package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.screens.TextProcessingScreen

@Composable
fun AppRoot(modifier: Modifier = Modifier) {
    TextProcessingScreen(modifier = modifier)
}
