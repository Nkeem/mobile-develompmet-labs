package com.example.myapplication.ui.plant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.PlantApplication
import com.example.myapplication.data.plant.Plant
import com.example.myapplication.data.plant.PlantRepository
import com.example.myapplication.domain.plant.WateringSchedule
import com.example.myapplication.domain.plant.calculateWateringSchedule
import java.time.LocalDate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class PlantListItem(
    val id: Long,
    val name: String,
    val watering: WateringSchedule,
)

sealed interface PlantListUiState {
    data object Loading : PlantListUiState
    data object Error : PlantListUiState
    data class Content(val plants: List<PlantListItem>) : PlantListUiState
}

class PlantListViewModel(repository: PlantRepository) : ViewModel() {
    private val today = MutableStateFlow(LocalDate.now())
    private val reload = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<PlantListUiState> = reload.flatMapLatest {
        combine<List<Plant>, LocalDate, PlantListUiState>(repository.observePlants(), today) { plants, date ->
            PlantListUiState.Content(
                plants.map { plant ->
                    PlantListItem(
                        id = plant.id,
                        name = plant.name,
                        watering = calculateWateringSchedule(
                            LocalDate.ofEpochDay(plant.lastWateredEpochDay),
                            plant.wateringIntervalDays,
                            date,
                        ),
                    )
                },
            )
        }
            .onStart { emit(PlantListUiState.Loading) }
            .catch { emit(PlantListUiState.Error) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PlantListUiState.Loading)

    fun refreshToday() {
        today.value = LocalDate.now()
    }

    fun retry() {
        reload.update { it + 1 }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = requireNotNull(this[APPLICATION_KEY]) as PlantApplication
                PlantListViewModel(application.plantRepository)
            }
        }
    }
}
