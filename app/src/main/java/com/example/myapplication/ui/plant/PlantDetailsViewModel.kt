package com.example.myapplication.ui.plant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.PlantApplication
import com.example.myapplication.data.plant.Plant
import com.example.myapplication.data.plant.PlantRepository
import com.example.myapplication.domain.plant.WateringSchedule
import com.example.myapplication.domain.plant.calculateWateringSchedule
import java.time.LocalDate
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlantDetails(val plant: Plant, val watering: WateringSchedule)

enum class PlantDetailsOperation { WATERING, DELETING }

enum class PlantDetailsMessage { WATERED, WATER_FAILED, DELETE_FAILED }

data class PlantDetailsUiState(
    val details: PlantDetails? = null,
    val isLoading: Boolean = true,
    val loadFailed: Boolean = false,
    val operation: PlantDetailsOperation? = null,
    val isDeleted: Boolean = false,
    val notFound: Boolean = false,
    val message: PlantDetailsMessage? = null,
) {
    val isBusy: Boolean get() = operation != null
    val canAct: Boolean get() = details != null && !isLoading && !loadFailed && !isBusy && !isDeleted && !notFound
}

class PlantDetailsViewModel(
    private val repository: PlantRepository,
    private val plantId: Long,
) : ViewModel() {
    private val today = MutableStateFlow(LocalDate.now())
    private val mutableUiState = MutableStateFlow(PlantDetailsUiState())
    val uiState = mutableUiState.asStateFlow()
    private var loadJob: Job? = null

    init {
        loadPlant()
    }

    fun loadPlant() {
        loadJob?.cancel()
        mutableUiState.update { it.copy(isLoading = it.details == null, loadFailed = false) }
        loadJob = viewModelScope.launch {
            repository.observePlant(plantId)
                .combine(today) { plant, date -> plant?.toDetails(date) }
                .catch { mutableUiState.update { it.copy(isLoading = false, loadFailed = true) } }
                .collect { details ->
                    mutableUiState.update { state ->
                        state.copy(
                            details = details,
                            isLoading = false,
                            notFound = state.notFound || (
                                details == null && !state.isDeleted &&
                                    state.operation != PlantDetailsOperation.DELETING
                                ),
                        )
                    }
                }
        }
    }

    fun refreshToday() {
        today.value = LocalDate.now()
    }

    fun markWatered() {
        val date = LocalDate.now()
        today.value = date
        if (uiState.value.details?.plant?.lastWateredEpochDay == date.toEpochDay()) return
        runOperation(PlantDetailsOperation.WATERING) {
            val changed = repository.markWatered(plantId, date)
            val plant = repository.observePlant(plantId).first()
            mutableUiState.update {
                it.copy(
                    details = plant?.toDetails(today.value),
                    notFound = plant == null,
                    message = if (changed && plant != null) PlantDetailsMessage.WATERED else null,
                )
            }
        }
    }

    fun deletePlant() {
        runOperation(PlantDetailsOperation.DELETING) {
            val deleted = repository.deletePlant(plantId)
            mutableUiState.update { it.copy(isDeleted = deleted, notFound = !deleted) }
        }
    }

    private fun runOperation(operation: PlantDetailsOperation, action: suspend () -> Unit) {
        if (!uiState.value.canAct) return
        mutableUiState.update { it.copy(operation = operation, message = null) }
        viewModelScope.launch {
            try {
                action()
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                mutableUiState.update {
                    it.copy(
                        message = when (operation) {
                            PlantDetailsOperation.WATERING -> PlantDetailsMessage.WATER_FAILED
                            PlantDetailsOperation.DELETING -> PlantDetailsMessage.DELETE_FAILED
                        },
                    )
                }
            } finally {
                mutableUiState.update { it.copy(operation = null) }
            }
        }
    }

    fun clearMessage() {
        mutableUiState.update { it.copy(message = null) }
    }

    private fun Plant.toDetails(date: LocalDate): PlantDetails = PlantDetails(
        plant = this,
        watering = calculateWateringSchedule(LocalDate.ofEpochDay(lastWateredEpochDay), wateringIntervalDays, date),
    )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = requireNotNull(this[APPLICATION_KEY]) as PlantApplication
                val plantId = requireNotNull(createSavedStateHandle().get<Long>(EXTRA_PLANT_ID))
                PlantDetailsViewModel(application.plantRepository, plantId)
            }
        }
    }
}
