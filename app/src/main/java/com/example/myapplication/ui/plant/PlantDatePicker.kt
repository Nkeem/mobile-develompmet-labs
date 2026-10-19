package com.example.myapplication.ui.plant

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantDatePicker(
    selectedDate: LocalDate,
    onConfirm: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val selectableDates = remember {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return !utcTimeMillis.toUtcDate().isAfter(LocalDate.now())
            }

            override fun isSelectableYear(year: Int): Boolean = year <= LocalDate.now().year
        }
    }
    val state = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
            .takeIf { it.year in DatePickerDefaults.YearRange }
            ?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli(),
        selectableDates = selectableDates,
    )
    val date = state.selectedDateMillis?.toUtcDate()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                enabled = date != null && !date.isAfter(LocalDate.now()),
                onClick = {
                    state.selectedDateMillis?.toUtcDate()?.let { chosenDate ->
                        if (!chosenDate.isAfter(LocalDate.now())) onConfirm(chosenDate)
                    }
                },
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        },
    ) {
        DatePicker(state = state, showModeToggle = false)
    }
}

private fun Long.toUtcDate(): LocalDate = Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
