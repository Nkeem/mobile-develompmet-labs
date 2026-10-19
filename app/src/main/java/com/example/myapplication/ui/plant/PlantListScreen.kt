package com.example.myapplication.ui.plant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun PlantListScreen(
    uiState: PlantListUiState,
    onAddPlant: () -> Unit,
    onOpenPlant: (Long) -> Unit,
    onRetry: () -> Unit,
) {
    val listState = rememberLazyListState()
    val locale = LocalConfiguration.current.locales[0]
    val dateFormatter = remember(locale) {
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(locale)
    }
    PlantScaffold(
        title = stringResource(R.string.plants_title),
        floatingActionButton = {
            if (uiState is PlantListUiState.Content && uiState.plants.isNotEmpty()) {
                FloatingActionButton(
                    onClick = onAddPlant,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = stringResource(R.string.add_plant),
                    )
                }
            }
        },
    ) { padding ->
        val contentModifier = Modifier.fillMaxSize().padding(padding)
        when (uiState) {
            PlantListUiState.Loading -> Box(contentModifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            PlantListUiState.Error -> PlantListMessage(
                title = stringResource(R.string.load_plants_failed),
                actionLabel = stringResource(R.string.retry),
                onAction = onRetry,
                modifier = contentModifier,
            )
            is PlantListUiState.Content -> if (uiState.plants.isEmpty()) {
                PlantListMessage(
                    title = stringResource(R.string.empty_plants_title),
                    message = stringResource(R.string.empty_plants_hint),
                    actionLabel = stringResource(R.string.add_plant),
                    onAction = onAddPlant,
                    modifier = contentModifier,
                )
            } else {
                LazyColumn(
                    modifier = contentModifier,
                    state = listState,
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = stringResource(R.string.upcoming_watering),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Text(
                                text = stringResource(R.string.plant_list_hint),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    items(uiState.plants, key = { it.id }) { item ->
                        PlantListRow(
                            item = item,
                            nextDate = item.watering.nextWateringDate.format(dateFormatter),
                            onClick = { onOpenPlant(item.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlantListRow(item: PlantListItem, nextDate: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = stringResource(R.string.next_watering_on, nextDate),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            WateringStatusLabel(item.watering)
        }
    }
}

@Composable
private fun PlantListMessage(
    title: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier,
    message: String? = null,
) {
    Box(modifier.padding(16.dp), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(title, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
            if (message != null) {
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
            Button(onClick = onAction, modifier = Modifier.fillMaxWidth()) {
                Text(actionLabel)
            }
        }
    }
}
