package com.szczepaniak.dawid.gymlog.models

import java.sql.Date

data class ExerciseSet(
    val id: Int,
    var checked: Boolean,
    var rep: Int,
    var volume: Float
)