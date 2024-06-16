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
    var sets: Int,
    @Ignore var exercises: List<Exercise> = emptyList()
) {
    constructor(id: Int, title: String, startTime: Date, endTime: Date, volume: Float, sets: Int, rating: Int) : this(
        id,
        title,
        startTime,
        endTime,
        volume,
        rating,
        sets,
        emptyList()
    )

    fun getDuration(): Long {
        return (endTime.time - startTime.time) / 1000
    }
}
