package com.szczepaniak.dawid.gymlog.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.szczepaniak.dawid.gymlog.Converters
import java.io.Serializable

@Entity
@TypeConverters(Converters::class)
data class Routine (
    @PrimaryKey(autoGenerate = true) val id: Long,
    var name: String,
    var exercises: List<Exercise>,
    var muscles: List<String>
): Serializable {
    constructor(name: String, exercises: List<Exercise>, muscles: List<String>) : this(0, name, exercises, muscles)

}
