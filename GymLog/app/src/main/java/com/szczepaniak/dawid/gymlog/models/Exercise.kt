package com.szczepaniak.dawid.gymlog.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    foreignKeys = [ForeignKey(
        entity = Workout::class,
        parentColumns = ["id"],
        childColumns = ["workoutId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Exercise(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var workoutId: Int,
    val name: String? = null,
    val type: String? = null,
    val muscle: String? = null,
    val equipment: String? = null,
    val difficulty: String? = null,
    val instructions: String? = null
) : Serializable {

    @Ignore
    var sets: List<ExerciseSet>? = null
}
