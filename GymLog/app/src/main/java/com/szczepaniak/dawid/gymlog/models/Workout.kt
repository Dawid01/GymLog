package com.szczepaniak.dawid.gymlog.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "workout_table")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val time: Int,
    val volume: Float,
    val date: Date,
    @Ignore val exercises: List<Exercise>,
    @Ignore val exerciseSets: List<ExerciseSet>
) {
    constructor(id: Int, title: String, time: Int, volume: Float, date: Date) : this(
        id,
        title,
        time,
        volume,
        date,
        emptyList(),
        emptyList()
    )
}
