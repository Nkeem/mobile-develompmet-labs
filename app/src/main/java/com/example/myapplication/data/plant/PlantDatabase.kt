package com.example.myapplication.data.plant

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Plant::class], version = 1, exportSchema = false)
abstract class PlantDatabase : RoomDatabase() {
    abstract fun plantDao(): PlantDao

    companion object {
        @Volatile
        private var instance: PlantDatabase? = null

        fun getInstance(context: Context): PlantDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    PlantDatabase::class.java,
                    "plants.db",
                ).build().also { instance = it }
            }
        }
    }
}
