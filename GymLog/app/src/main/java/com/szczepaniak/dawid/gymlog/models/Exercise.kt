package com.szczepaniak.dawid.gymlog.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    @PrimaryKey val id: Int,
    val name: String? = null,
    val type: String? = null,
    val muscle: String? = null,
    val equipment: String? = null,
    val difficulty: String? = null,
    val instructions: String? = null,
    var sets: List<ExerciseSet>? = null
){
    override fun equals(other: Any?): Boolean {
       if (this === other) return true
       if (other !is Exercise) return false

       if (id != other.id) return false
       if (name != other.name) return false
       if (type != other.type) return false
       if (muscle != other.muscle) return false
       if (equipment != other.equipment) return false
       if (difficulty != other.difficulty) return false
       if (instructions != other.instructions) return false

       return true
    }

   override fun hashCode(): Int {
      var result = id
      result = 31 * result + (name?.hashCode() ?: 0)
      result = 31 * result + (type?.hashCode() ?: 0)
      result = 31 * result + (muscle?.hashCode() ?: 0)
      result = 31 * result + (equipment?.hashCode() ?: 0)
      result = 31 * result + (difficulty?.hashCode() ?: 0)
      result = 31 * result + (instructions?.hashCode() ?: 0)
      return result
   }
}
