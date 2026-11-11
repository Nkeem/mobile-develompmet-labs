package com.example.myapplication.domain.plant

import java.time.LocalDate

data class PlantInput(
    val name: String,
    val wateringIntervalDays: Int,
    val lastWateredDate: LocalDate,
    val note: String,
)

enum class PlantNameError {
    REQUIRED,
    TOO_LONG,
}

data class PlantValidationErrors(
    val name: PlantNameError?,
    val intervalInvalid: Boolean,
    val dateInFuture: Boolean,
    val noteTooLong: Boolean,
) {
    val hasErrors: Boolean
        get() = name != null || intervalInvalid || dateInFuture || noteTooLong
}

sealed interface PlantValidationResult {
    data class Valid(val input: PlantInput) : PlantValidationResult
    data class Invalid(val errors: PlantValidationErrors) : PlantValidationResult
}
