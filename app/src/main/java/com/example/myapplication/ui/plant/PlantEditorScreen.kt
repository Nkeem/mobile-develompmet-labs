package com.example.myapplication.ui.plant

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.domain.plant.PlantNameError
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun PlantEditorScreen(
    isEditing: Boolean,
    uiState: PlantEditorUiState,
    onNameChange: (String) -> Unit,
    onIntervalChange: (String) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onNoteChange: (String) -> Unit,
    onSave: () -> Unit,
    onRetry: () -> Unit,
    onClose: () -> Unit,
) {
    var showExitDialog by rememberSaveable { mutableStateOf(false) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val requestClose = {
        if (!uiState.isSaving) {
            if (uiState.hasChanges) showExitDialog = true else onClose()
        }
    }
    BackHandler(onBack = requestClose)

    PlantScaffold(
        title = stringResource(if (isEditing) R.string.edit_plant_title else R.string.new_plant_title),
        onBack = requestClose,
    ) { padding ->
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
                TextButton(onClick = onRetry) { Text(stringResource(R.string.retry)) }
            }
            else -> {
                val form = uiState.form
                val errors = uiState.errors
                val enabled = !uiState.isSaving && !uiState.isSaved && !uiState.notFound
                val locale = LocalConfiguration.current.locales[0]
                val dateFormatter = remember(locale) {
                    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale)
                }
                Column(
                    modifier = Modifier.fillMaxSize()
                        .padding(padding)
                        .consumeWindowInsets(padding)
                        .imePadding()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text(
                        text = stringResource(R.string.editor_hint),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    EditorTextField(
                        value = form.name,
                        onValueChange = onNameChange,
                        label = R.string.plant_name,
                        error = when (errors?.name) {
                            PlantNameError.REQUIRED -> R.string.name_required
                            PlantNameError.TOO_LONG -> R.string.name_too_long
                            null -> null
                        },
                        enabled = enabled,
                    )
                    EditorTextField(
                        value = form.interval,
                        onValueChange = onIntervalChange,
                        label = R.string.watering_interval_days,
                        error = if (errors?.intervalInvalid == true) R.string.interval_invalid else null,
                        keyboardType = KeyboardType.Number,
                        enabled = enabled,
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        OutlinedCard(
                            onClick = { focusManager.clearFocus(); showDatePicker = true },
                            enabled = enabled,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        stringResource(R.string.last_watered),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                    Text(form.lastWateredDate.format(dateFormatter))
                                }
                                Icon(painterResource(R.drawable.ic_calendar), contentDescription = null)
                            }
                        }
                        if (errors?.dateInFuture == true) {
                            Text(
                                stringResource(R.string.date_in_future),
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )
                        }
                    }
                    EditorTextField(
                        value = form.note,
                        onValueChange = onNoteChange,
                        label = R.string.plant_note,
                        error = if (errors?.noteTooLong == true) R.string.note_too_long else null,
                        singleLine = false,
                        enabled = enabled,
                    )
                    if (uiState.saveFailed) {
                        Text(stringResource(R.string.save_plant_failed), color = MaterialTheme.colorScheme.error)
                    }
                    Button(
                        onClick = { focusManager.clearFocus(); onSave() },
                        enabled = enabled,
                        modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(stringResource(if (uiState.isSaving) R.string.saving else R.string.save))
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        PlantDatePicker(
            selectedDate = uiState.form.lastWateredDate,
            onConfirm = { onDateChange(it); showDatePicker = false },
            onDismiss = { showDatePicker = false },
        )
    }
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text(stringResource(R.string.discard_changes)) },
            confirmButton = {
                TextButton(onClick = onClose) { Text(stringResource(R.string.exit_editor)) }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) { Text(stringResource(R.string.continue_editing)) }
            },
        )
    }
}

@Composable
private fun EditorTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes label: Int,
    @StringRes error: Int?,
    enabled: Boolean,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = singleLine,
        minLines = if (singleLine) 1 else 3,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        isError = error != null,
        supportingText = if (error != null) { { Text(stringResource(error)) } } else null,
        shape = RoundedCornerShape(16.dp),
    )
}
