package com.szczepaniak.dawid.gymlog.models

data class ExerciseSet(
    val exercise: Exercise,
    val weight: Float,
    val reps: Int,
    val sets: Int
)