package com.szczepaniak.dawid.gymlog.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Exercise::class,
        parentColumns = ["id"],
        childColumns = ["exerciseId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ExerciseSet(
    @PrimaryKey val id: Int,
    val exerciseId: Int,
    var checked: Boolean,
    var rep: Int,
    var volume: Float
)
