package com.szczepaniak.dawid.gymlog.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "workout_table")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val startTime: Long,
    val endTime: Long,
    val volume: Float,
    val date: Date,
    @Ignore val exercises: List<Exercise>,
    @Ignore val exerciseSets: List<ExerciseSet>
) {
    constructor(id: Int, title: String, startTime: Long, endTime: Long , volume: Float, date: Date) : this(
        id,
        title,
        startTime,
        endTime,
        volume,
        date,
        emptyList(),
        emptyList()
    )
}
