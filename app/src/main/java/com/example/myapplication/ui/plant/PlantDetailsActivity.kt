package com.example.myapplication.ui.plant

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.R
import com.example.myapplication.ui.theme.PlantTheme

internal const val EXTRA_PLANT_ID = "plant_id"

class PlantDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val plantId = intent.getLongExtra(EXTRA_PLANT_ID, 0)
        if (plantId <= 0) {
            Toast.makeText(this, R.string.plant_not_found, Toast.LENGTH_SHORT).show()
            returnToList()
            return
        }
        val viewModel = ViewModelProvider(this, PlantDetailsViewModel.Factory)[PlantDetailsViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            PlantTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                LifecycleResumeEffect(viewModel) {
                    viewModel.refreshToday()
                    onPauseOrDispose { }
                }
                LaunchedEffect(uiState.isDeleted, uiState.notFound) {
                    if (uiState.isDeleted) {
                        finish()
                    } else if (uiState.notFound) {
                        Toast.makeText(this@PlantDetailsActivity, R.string.plant_not_found, Toast.LENGTH_SHORT).show()
                        returnToList()
                    }
                }
                LaunchedEffect(uiState.message) {
                    val message = uiState.message
                    if (message != null) {
                        viewModel.clearMessage()
                        val text = when (message) {
                            PlantDetailsMessage.WATERED -> R.string.watering_recorded
                            PlantDetailsMessage.WATER_FAILED -> R.string.water_plant_failed
                            PlantDetailsMessage.DELETE_FAILED -> R.string.delete_plant_failed
                        }
                        Toast.makeText(this@PlantDetailsActivity, text, Toast.LENGTH_SHORT).show()
                    }
                }
                PlantDetailsScreen(
                    uiState = uiState,
                    onBack = { onBackPressedDispatcher.onBackPressed() },
                    onEdit = {
                        startActivity(
                            Intent(this, PlantEditorActivity::class.java)
                                .putExtra(EXTRA_PLANT_ID, plantId),
                        )
                    },
                    onWater = viewModel::markWatered,
                    onDelete = viewModel::deletePlant,
                    onRetry = viewModel::loadPlant,
                )
            }
        }
    }

    private fun returnToList() {
        startActivity(
            Intent(this, PlantListActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP),
        )
        finish()
    }
}
