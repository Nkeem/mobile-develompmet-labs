package com.example.myapplication.ui.plant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.domain.plant.WateringSchedule
import com.example.myapplication.domain.plant.WateringStatus

@Composable
fun WateringStatusLabel(watering: WateringSchedule) {
    val quantity = watering.days.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
    val label = when (watering.status) {
        WateringStatus.OVERDUE -> pluralStringResource(R.plurals.watering_overdue, quantity, watering.days)
        WateringStatus.TODAY -> stringResource(R.string.water_today)
        WateringStatus.UPCOMING -> pluralStringResource(R.plurals.watering_in, quantity, watering.days)
    }
    val colors = MaterialTheme.colorScheme
    val foreground = when (watering.status) {
        WateringStatus.OVERDUE -> colors.error
        WateringStatus.TODAY -> colors.tertiary
        WateringStatus.UPCOMING -> colors.primary
    }
    val background = when (watering.status) {
        WateringStatus.OVERDUE -> colors.errorContainer
        WateringStatus.TODAY -> colors.tertiaryContainer
        WateringStatus.UPCOMING -> colors.primaryContainer
    }
    Text(
        text = label,
        color = foreground,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.background(background, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
    )
}
