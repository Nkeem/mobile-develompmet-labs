package com.example.myapplication.ui.plant

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.theme.PlantTheme

class PlantListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this, PlantListViewModel.Factory)[PlantListViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            PlantTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                LifecycleResumeEffect(viewModel) {
                    viewModel.refreshToday()
                    onPauseOrDispose { }
                }
                PlantListScreen(
                    uiState = uiState,
                    onAddPlant = { startActivity(Intent(this, PlantEditorActivity::class.java)) },
                    onOpenPlant = { plantId ->
                        startActivity(
                            Intent(this, PlantDetailsActivity::class.java)
                                .putExtra(EXTRA_PLANT_ID, plantId),
                        )
                    },
                    onRetry = viewModel::retry,
                )
            }
        }
    }
}
