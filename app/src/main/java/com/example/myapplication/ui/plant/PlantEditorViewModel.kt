package com.example.myapplication.ui.plant

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.PlantApplication
import com.example.myapplication.data.plant.PlantRepository
import com.example.myapplication.domain.plant.PlantValidationErrors
import com.example.myapplication.domain.plant.PlantValidationResult
import com.example.myapplication.domain.plant.validatePlantInput
import java.time.LocalDate
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlantEditorForm(
    val name: String = "",
    val interval: String = "7",
    val lastWateredDate: LocalDate = LocalDate.now(),
    val note: String = "",
)

data class PlantEditorUiState(
    val form: PlantEditorForm,
    val original: PlantEditorForm? = null,
    val isLoading: Boolean = false,
    val loadFailed: Boolean = false,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val notFound: Boolean = false,
    val errors: PlantValidationErrors? = null,
    val saveFailed: Boolean = false,
) {
    val hasChanges: Boolean get() = original != null && form != original
}

class PlantEditorViewModel(
    private val repository: PlantRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val plantId: Long? = savedStateHandle[EXTRA_PLANT_ID]
    private val original = restoreForm("original")
    private val initialForm = restoreForm("draft") ?: PlantEditorForm()
    private val mutableUiState = MutableStateFlow(
        PlantEditorUiState(
            form = initialForm,
            original = original,
            isLoading = plantId != null && original == null,
            isSaved = savedStateHandle["saved"] ?: false,
        ),
    )
    val uiState = mutableUiState.asStateFlow()
    private var loadJob: Job? = null

    init {
        if (plantId == null) {
            if (original == null) initializeForm(initialForm)
        } else {
            loadPlant()
        }
    }

    fun changeName(value: String) = changeForm(uiState.value.form.copy(name = value))

    fun changeInterval(value: String) = changeForm(uiState.value.form.copy(interval = value))

    fun changeDate(value: LocalDate) = changeForm(uiState.value.form.copy(lastWateredDate = value))

    fun changeNote(value: String) = changeForm(uiState.value.form.copy(note = value))

    private fun changeForm(form: PlantEditorForm) {
        val state = uiState.value
        if (state.isSaving || state.isSaved || state.original == null || state.notFound) return
        storeForm("draft", form)
        mutableUiState.update { it.copy(form = form, errors = null, saveFailed = false) }
    }

    fun loadPlant() {
        val id = plantId ?: return
        loadJob?.cancel()
        mutableUiState.update { it.copy(isLoading = it.original == null, loadFailed = false) }
        loadJob = viewModelScope.launch {
            repository.observePlant(id)
                .catch { mutableUiState.update { it.copy(isLoading = false, loadFailed = true) } }
                .collect { plant ->
                    if (plant == null) {
                        mutableUiState.update { it.copy(isLoading = false, notFound = true) }
                    } else if (uiState.value.original == null) {
                        initializeForm(
                            PlantEditorForm(
                                name = plant.name,
                                interval = plant.wateringIntervalDays.toString(),
                                lastWateredDate = LocalDate.ofEpochDay(plant.lastWateredEpochDay),
                                note = plant.note,
                            ),
                        )
                    }
                }
        }
    }

    private fun initializeForm(form: PlantEditorForm) {
        storeForm("original", form)
        storeForm("draft", form)
        mutableUiState.update { it.copy(form = form, original = form, isLoading = false) }
    }

    fun save() {
        val state = uiState.value
        if (state.isLoading || state.loadFailed || state.isSaving || state.isSaved || state.notFound) return
        val form = state.form
        val result = validatePlantInput(
            name = form.name,
            wateringIntervalInput = form.interval,
            lastWateredDate = form.lastWateredDate,
            note = form.note,
            today = LocalDate.now(),
        )
        if (result is PlantValidationResult.Invalid) {
            mutableUiState.update { it.copy(errors = result.errors, saveFailed = false) }
            return
        }
        val input = (result as PlantValidationResult.Valid).input
        mutableUiState.update { it.copy(isSaving = true, errors = null, saveFailed = false) }
        viewModelScope.launch {
            try {
                val saved = if (plantId == null) {
                    repository.addPlant(input)
                    true
                } else {
                    repository.updatePlant(plantId, input)
                }
                savedStateHandle["saved"] = saved
                mutableUiState.update { it.copy(isSaving = false, isSaved = saved, notFound = !saved) }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                mutableUiState.update { it.copy(isSaving = false, saveFailed = true) }
            }
        }
    }

    private fun restoreForm(prefix: String): PlantEditorForm? {
        val epochDay = savedStateHandle.get<Long>("${prefix}_date") ?: return null
        return PlantEditorForm(
            name = savedStateHandle["${prefix}_name"] ?: "",
            interval = savedStateHandle["${prefix}_interval"] ?: "7",
            lastWateredDate = LocalDate.ofEpochDay(epochDay),
            note = savedStateHandle["${prefix}_note"] ?: "",
        )
    }

    private fun storeForm(prefix: String, form: PlantEditorForm) {
        savedStateHandle["${prefix}_name"] = form.name
        savedStateHandle["${prefix}_interval"] = form.interval
        savedStateHandle["${prefix}_date"] = form.lastWateredDate.toEpochDay()
        savedStateHandle["${prefix}_note"] = form.note
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = requireNotNull(this[APPLICATION_KEY]) as PlantApplication
                PlantEditorViewModel(application.plantRepository, createSavedStateHandle())
            }
        }
    }
}
