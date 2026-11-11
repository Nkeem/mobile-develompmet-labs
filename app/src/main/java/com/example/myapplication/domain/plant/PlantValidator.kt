package com.example.myapplication.domain.plant

import java.time.LocalDate

fun validatePlantInput(
    name: String,
    wateringIntervalInput: String,
    lastWateredDate: LocalDate,
    note: String,
    today: LocalDate,
): PlantValidationResult {
    val trimmedName = name.trim()
    val trimmedNote = note.trim()
    val interval = wateringIntervalInput.trim().toIntOrNull()
    val errors = PlantValidationErrors(
        name = when {
            trimmedName.isEmpty() -> PlantNameError.REQUIRED
            trimmedName.length > 60 -> PlantNameError.TOO_LONG
            else -> null
        },
        intervalInvalid = interval == null || interval !in 1..365,
        dateInFuture = lastWateredDate.isAfter(today),
        noteTooLong = trimmedNote.length > 500,
    )

    if (errors.hasErrors) {
        return PlantValidationResult.Invalid(errors)
    }

    return PlantValidationResult.Valid(
        PlantInput(
            name = trimmedName,
            wateringIntervalDays = requireNotNull(interval),
            lastWateredDate = lastWateredDate,
            note = trimmedNote,
        ),
    )
}
