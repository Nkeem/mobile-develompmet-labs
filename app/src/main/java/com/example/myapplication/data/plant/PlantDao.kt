package com.example.myapplication.data.plant

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDao {
    @Query("SELECT * FROM plants ORDER BY lastWateredEpochDay + wateringIntervalDays ASC, id ASC")
    fun observePlants(): Flow<List<Plant>>

    @Query("SELECT * FROM plants WHERE id = :id")
    fun observePlant(id: Long): Flow<Plant?>

    @Insert
    suspend fun insert(plant: Plant): Long

    @Update
    suspend fun update(plant: Plant): Int

    @Query(
        """
        UPDATE plants SET lastWateredEpochDay = :todayEpochDay
        WHERE id = :id AND lastWateredEpochDay != :todayEpochDay
        """,
    )
    suspend fun markWatered(id: Long, todayEpochDay: Long): Int

    @Query("DELETE FROM plants WHERE id = :id")
    suspend fun delete(id: Long): Int
}
