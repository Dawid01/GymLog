package com.szczepaniak.dawid.gymlog.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date


@Entity(tableName = "workout_table")
data class Workout(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val startTime: Date,
    var endTime: Date,
    var volume: Float,
    var rating: Int,
    @Ignore var exercises: List<Exercise>,
    @Ignore var exerciseSets: List<ExerciseSet>
) : Serializable{
    constructor(id: Int, title: String, startTime: Date, endTime: Date , volume: Float, rating: Int) : this(
        id,
        title,
        startTime,
        endTime,
        volume,
        rating,
        emptyList(),
        emptyList()
    )


    fun getDuration(): Long {
        return (endTime.time - startTime.time) / 1000
    }
}
