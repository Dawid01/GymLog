package com.szczepaniak.dawid.gymlog
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.szczepaniak.dawid.gymlog.models.Exercise
import com.szczepaniak.dawid.gymlog.models.ExerciseSet

class Converters {
    @TypeConverter
    fun fromExerciseList(value: List<Exercise>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Exercise>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toExerciseList(value: String): List<Exercise> {
        val gson = Gson()
        val type = object : TypeToken<List<Exercise>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromExerciseSetList(value: List<ExerciseSet>): String {
        val gson = Gson()
        val type = object : TypeToken<List<ExerciseSet>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toExerciseSetList(value: String): List<ExerciseSet> {
        val gson = Gson()
        val type = object : TypeToken<List<ExerciseSet>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromMusclesList(value: List<String>): String {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toMusclesList(value: String): List<String> {
        val gson = Gson()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }
}
