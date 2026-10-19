package com.example.myapplication.ui.plant

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.R
import com.example.myapplication.ui.theme.PlantTheme

class PlantEditorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val plantId = if (intent.hasExtra(EXTRA_PLANT_ID)) {
            intent.getLongExtra(EXTRA_PLANT_ID, 0)
        } else {
            null
        }
        if (plantId != null && plantId <= 0) {
            Toast.makeText(this, R.string.plant_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        enableEdgeToEdge()
        setContent {
            PlantTheme {
                PlantEditorScreen(
                    isEditing = plantId != null,
                    onBack = { onBackPressedDispatcher.onBackPressed() },
                )
            }
        }
    }
}
