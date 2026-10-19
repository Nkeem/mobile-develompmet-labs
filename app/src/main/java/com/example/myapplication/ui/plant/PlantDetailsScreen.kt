package com.example.myapplication.ui.plant

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R

@Composable
fun PlantDetailsScreen(onBack: () -> Unit, onEdit: () -> Unit) {
    PlantScaffold(
        title = stringResource(R.string.plant_details_title),
        onBack = onBack,
        actions = {
            TextButton(onClick = onEdit) {
                Text(stringResource(R.string.edit_plant))
            }
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding))
    }
}
