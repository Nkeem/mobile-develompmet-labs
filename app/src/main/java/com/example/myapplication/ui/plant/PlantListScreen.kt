package com.example.myapplication.ui.plant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun PlantListScreen(onAddPlant: () -> Unit) {
    PlantScaffold(title = stringResource(R.string.plants_title)) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = stringResource(R.string.empty_plants_title),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = stringResource(R.string.empty_plants_hint),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Button(onClick = onAddPlant, modifier = Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.add_plant))
                }
            }
        }
    }
}
