package com.szczepaniak.dawid.gymlog.models

 class Exercise(
    val id: Int,
    val name: String? = null,
    val type: String? = null,
    val muscle: String? = null,
    val equipment: String? = null,
    val difficulty: String? = null,
    val instructions: String? = null
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
 }
