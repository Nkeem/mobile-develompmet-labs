package com.example.myapplication.data.plant

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val wateringIntervalDays: Int,
    val lastWateredEpochDay: Long,
    val note: String,
)
