package com.example.myapplication.domain.plant

import java.time.LocalDate
import kotlin.math.abs

enum class WateringStatus {
    OVERDUE,
    TODAY,
    UPCOMING,
}

data class WateringSchedule(
    val nextWateringDate: LocalDate,
    val status: WateringStatus,
    val days: Long,
    val wateredToday: Boolean,
)

fun calculateWateringSchedule(
    lastWateredDate: LocalDate,
    wateringIntervalDays: Int,
    today: LocalDate,
): WateringSchedule {
    require(wateringIntervalDays in 1..365) { "Watering interval must be between 1 and 365 days" }
    val nextDate = lastWateredDate.plusDays(wateringIntervalDays.toLong())
    val difference = nextDate.toEpochDay() - today.toEpochDay()
    val status = when {
        difference < 0 -> WateringStatus.OVERDUE
        difference == 0L -> WateringStatus.TODAY
        else -> WateringStatus.UPCOMING
    }

    return WateringSchedule(
        nextWateringDate = nextDate,
        status = status,
        days = abs(difference),
        wateredToday = lastWateredDate == today,
    )
}
