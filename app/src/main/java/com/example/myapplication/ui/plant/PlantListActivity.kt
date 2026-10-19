package com.example.myapplication.ui.plant

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.ui.theme.PlantTheme

class PlantListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlantTheme {
                PlantListScreen(
                    onAddPlant = { startActivity(Intent(this, PlantEditorActivity::class.java)) },
                )
            }
        }
    }
}
