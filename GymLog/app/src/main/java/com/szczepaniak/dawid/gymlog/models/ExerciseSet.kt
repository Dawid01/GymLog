package com.szczepaniak.dawid.gymlog.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    foreignKeys = [ForeignKey(
        entity = Exercise::class,
        parentColumns = ["id"],
        childColumns = ["exerciseId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ExerciseSet(
    @PrimaryKey(autoGenerate = true) val id: Int,
    var exerciseId: Int,
    var checked: Boolean,
    var rep: Int,
    var volume: Float
) : Serializable
