package com.szczepaniak.dawid.gymlog

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.szczepaniak.dawid.gymlog.models.Exercise
import com.szczepaniak.dawid.gymlog.models.Routine
import com.szczepaniak.dawid.gymlog.models.Workout

object Singleton {
    private var selectedExercises: MutableList<Exercise> = mutableListOf()
    private lateinit var newRoutine: Routine
    private lateinit var editRoutine: Routine
    private var selectedRoutine: Routine? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences("TrainingPrefs", Context.MODE_PRIVATE)
    }

    fun getSelectedExercises(): List<Exercise> {
        return selectedExercises
    }

    fun setSelectedExercise(exercises: List<Exercise>) {
        selectedExercises = exercises.toMutableList()
    }
    fun clearSelectedExercises() {
        selectedExercises.clear()
    }

    fun getNewRoutine() : Routine{
        return  newRoutine
    }

    fun setNewRoutine(routine: Routine){
        newRoutine = routine
    }

    fun getEditRoutine() : Routine{
        return editRoutine
    }

    fun setEditRoutine(routine: Routine){
        editRoutine = routine
    }

    fun getSelectedRoutine() : Routine? {
        return selectedRoutine
    }

    fun setSelectedRoutine(routine: Routine){
        selectedRoutine = routine
    }

    fun saveCurrentWorkout(workout: Workout?) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(workout)
        editor.putString("current_workout", json)
        editor.apply()
    }

    fun getCurrentWorkout(): Workout? {
        val json = sharedPreferences.getString("current_workout", null)
        return if (json != null) {
            val type = object : TypeToken<Workout>() {}.type
            gson.fromJson(json, type)
        } else {
           return null
        }
    }


}