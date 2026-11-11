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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
        val viewModel = ViewModelProvider(this, PlantEditorViewModel.Factory)[PlantEditorViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            PlantTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                LaunchedEffect(uiState.isSaved, uiState.notFound) {
                    if (uiState.notFound) {
                        Toast.makeText(this@PlantEditorActivity, R.string.plant_not_found, Toast.LENGTH_SHORT).show()
                        startActivity(
                            Intent(this@PlantEditorActivity, PlantListActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP),
                        )
                        finish()
                    } else if (uiState.isSaved) {
                        finish()
                    }
                }
                PlantEditorScreen(
                    isEditing = plantId != null,
                    uiState = uiState,
                    onNameChange = viewModel::changeName,
                    onIntervalChange = viewModel::changeInterval,
                    onDateChange = viewModel::changeDate,
                    onNoteChange = viewModel::changeNote,
                    onSave = viewModel::save,
                    onRetry = viewModel::loadPlant,
                    onClose = { finish() },
                )
            }
        }
    }
}
