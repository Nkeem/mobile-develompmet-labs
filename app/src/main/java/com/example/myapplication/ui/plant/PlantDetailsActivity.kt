package com.example.myapplication.ui.plant

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.myapplication.R
import com.example.myapplication.ui.theme.PlantTheme

internal const val EXTRA_PLANT_ID = "plant_id"

class PlantDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val plantId = intent.getLongExtra(EXTRA_PLANT_ID, 0)
        if (plantId <= 0) {
            Toast.makeText(this, R.string.plant_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        enableEdgeToEdge()
        setContent {
            PlantTheme {
                PlantDetailsScreen(
                    onBack = { onBackPressedDispatcher.onBackPressed() },
                    onEdit = {
                        startActivity(
                            Intent(this, PlantEditorActivity::class.java)
                                .putExtra(EXTRA_PLANT_ID, plantId),
                        )
                    },
                )
            }
        }
    }
}
