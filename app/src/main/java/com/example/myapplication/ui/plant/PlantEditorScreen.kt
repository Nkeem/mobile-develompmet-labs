package com.example.myapplication.ui.plant

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun PlantEditorScreen(isEditing: Boolean, onBack: () -> Unit) {
    PlantScaffold(
        title = stringResource(if (isEditing) R.string.edit_plant_title else R.string.new_plant_title),
        onBack = onBack,
    ) { padding ->
        Text(
            text = stringResource(R.string.editor_hint),
            modifier = Modifier.padding(padding).padding(16.dp),
        )
    }
}
