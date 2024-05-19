package com.szczepaniak.dawid.gymlog.models

data class Routine (
    val id: Int,
    val name: String,
    val exercises: List<String>
)
