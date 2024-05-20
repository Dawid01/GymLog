package com.szczepaniak.dawid.gymlog.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.szczepaniak.dawid.gymlog.Converters

@Entity
@TypeConverters(Converters::class)
data class Routine (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val exercises: List<Exercise>
){
    constructor(name: String, exercises: List<Exercise>) : this(0, name, exercises)
}
