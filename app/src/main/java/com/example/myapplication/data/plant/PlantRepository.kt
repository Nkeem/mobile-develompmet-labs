package com.example.myapplication.data.plant

import com.example.myapplication.domain.plant.PlantInput
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow

class PlantRepository(private val plantDao: PlantDao) {
    fun observePlants(): Flow<List<Plant>> = plantDao.observePlants()

    fun observePlant(id: Long): Flow<Plant?> = plantDao.observePlant(id)

    suspend fun addPlant(input: PlantInput): Long = plantDao.insert(input.toPlant())

    suspend fun updatePlant(id: Long, input: PlantInput): Boolean {
        return plantDao.update(input.toPlant(id)) > 0
    }

    suspend fun markWatered(id: Long, today: LocalDate): Boolean {
        return plantDao.markWatered(id, today.toEpochDay()) > 0
    }

    suspend fun deletePlant(id: Long): Boolean = plantDao.delete(id) > 0

    private fun PlantInput.toPlant(id: Long = 0): Plant = Plant(
        id = id,
        name = name,
        wateringIntervalDays = wateringIntervalDays,
        lastWateredEpochDay = lastWateredDate.toEpochDay(),
        note = note,
    )
}
