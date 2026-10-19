package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.plant.PlantDatabase
import com.example.myapplication.data.plant.PlantRepository

class PlantApplication : Application() {
    val plantRepository: PlantRepository by lazy {
        PlantRepository(PlantDatabase.getInstance(this).plantDao())
    }
}
