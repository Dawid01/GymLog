package com.szczepaniak.dawid.gymlog

import android.content.Context
import com.szczepaniak.dawid.gymlog.models.Exercise

object Singleton {
    private var selectedExercises: MutableList<Exercise> = mutableListOf()

    fun getSelectedExercises(): List<Exercise> {
        return selectedExercises
    }

    fun setSelectedExercise(exercises: List<Exercise>) {
        selectedExercises = exercises.toMutableList()
    }
    fun clearSelectedExercises() {
        selectedExercises.clear()
    }
}