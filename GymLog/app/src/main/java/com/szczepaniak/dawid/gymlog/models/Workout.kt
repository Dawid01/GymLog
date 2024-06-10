package com.szczepaniak.dawid.gymlog.models

data class Workout(
    val id: Int,
    val time: Int,
    val volume: Float,
    val date: java.util.Date,
    val exercises: List<Exercise>,
    val exerciseSets: List<ExerciseSet>
)
