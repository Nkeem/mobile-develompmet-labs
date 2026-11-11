package com.example.myapplication.ui.plant

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun PlantDetailsScreen(
    uiState: PlantDetailsUiState,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onWater: () -> Unit,
    onDelete: () -> Unit,
    onRetry: () -> Unit,
) {
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val locale = LocalConfiguration.current.locales[0]
    val dateFormatter = remember(locale) {
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale)
    }
    BackHandler(enabled = uiState.isBusy) { }
    PlantScaffold(
        title = stringResource(R.string.plant_details_title),
        onBack = onBack,
        actions = {
            TextButton(onClick = onEdit, enabled = uiState.canAct) {
                Text(stringResource(R.string.edit_plant))
            }
        },
    ) { padding ->
        val details = uiState.details
        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            uiState.loadFailed -> Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(stringResource(R.string.load_plant_failed))
                TextButton(onClick = onRetry, enabled = !uiState.isBusy) { Text(stringResource(R.string.retry)) }
            }
            details != null -> {
                val plant = details.plant
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding)
                        .verticalScroll(scrollState).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        PlantSymbol(Modifier.size(88.dp))
                        Text(
                            plant.name,
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                        )
                    }
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            Text(
                                stringResource(R.string.next_watering),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Text(
                                details.watering.nextWateringDate.format(dateFormatter),
                                style = MaterialTheme.typography.titleLarge,
                            )
                            WateringStatusLabel(details.watering)
                        }
                    }
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            DetailValue(
                                label = stringResource(R.string.watering_interval),
                                value = pluralStringResource(
                                    R.plurals.watering_every, plant.wateringIntervalDays, plant.wateringIntervalDays,
                                ),
                            )
                            DetailValue(
                                label = stringResource(R.string.last_watered),
                                value = LocalDate.ofEpochDay(plant.lastWateredEpochDay).format(dateFormatter),
                            )
                            DetailValue(
                                label = stringResource(R.string.details_note),
                                value = plant.note.ifEmpty { stringResource(R.string.no_note) },
                            )
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onWater,
                            enabled = uiState.canAct && !details.watering.wateredToday,
                            modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                            shape = RoundedCornerShape(16.dp),
                        ) {
                            Text(
                                stringResource(
                                    when {
                                        uiState.isBusy -> R.string.working
                                        details.watering.wateredToday -> R.string.already_watered_today
                                        else -> R.string.watered_today
                                    },
                                ),
                            )
                        }
                        TextButton(
                            onClick = { showDeleteDialog = true },
                            enabled = uiState.canAct,
                            modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp),
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        ) {
                            Text(stringResource(R.string.delete_plant))
                        }
                    }
                }
            }
        }
    }
    if (showDeleteDialog && uiState.details != null && !uiState.isDeleted && !uiState.notFound) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            shape = RoundedCornerShape(28.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            title = {
                Text(
                    stringResource(R.string.delete_plant_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp,
                )
            },
            text = {
                Text(
                    stringResource(R.string.delete_plant_message, uiState.details.plant.name),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
            confirmButton = {
                TextButton(
                    enabled = uiState.canAct,
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text(stringResource(R.string.cancel)) }
            },
        )
    }
}

@Composable
private fun DetailValue(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}
