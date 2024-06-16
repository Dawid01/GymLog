package com.szczepaniak.dawid.gymlog.doa

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.szczepaniak.dawid.gymlog.models.Exercise
import com.szczepaniak.dawid.gymlog.models.ExerciseSet
import com.szczepaniak.dawid.gymlog.models.ExerciseWithSets
import com.szczepaniak.dawid.gymlog.models.Workout
import com.szczepaniak.dawid.gymlog.models.WorkoutWithExercises

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: Workout): Long

    @Update
    suspend fun update(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<Exercise>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseSets(exerciseSets: List<ExerciseSet>)

    @Transaction
    suspend fun insertWorkoutWithExercisesAndSets(workout: Workout) {
        val workoutId = insert(workout).toInt()
        workout.exercises.forEach { exercise ->
            exercise.workoutId = workoutId
            val exerciseId = insertExercise(exercise)
            exercise.sets?.forEach { set ->
                set.exerciseId =
                    exerciseId.toInt()
            }
            insertExerciseSets(exercise.sets ?: emptyList())
        }
    }

    @Transaction
    suspend fun insertExerciseWithSets(exercise: Exercise, sets: List<ExerciseSet>) {
        val exerciseId = insertExercise(exercise)
        sets.forEach { set ->
            set.exerciseId = exerciseId.toInt()
        }
        insertExerciseSets(sets)
    }

    @Transaction
    @Query("SELECT * FROM exercise WHERE id = :exerciseId")
    suspend fun getExerciseWithSets(exerciseId: Int): ExerciseWithSets

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseSet(exerciseSet: ExerciseSet)

    @Query("SELECT COUNT(*) FROM workout_table")
    suspend fun countWorkouts(): Int

    @Query("SELECT SUM(volume) FROM workout_table")
    suspend fun totalVolume(): Long

    @Query("SELECT * FROM workout_table ORDER BY startTime DESC")
    fun getWorkoutsByStartDate(): PagingSource<Int, Workout>

    @Query("SELECT * FROM workout_table WHERE id = :id")
    suspend fun getWorkout(id: Int): Workout?

    @Query("SELECT * FROM workout_table WHERE strftime('%Y', startTime / 1000, 'unixepoch') = :year AND strftime('%m', startTime / 1000, 'unixepoch') = :month ORDER BY startTime DESC")
    suspend fun getWorkoutsByMonth(year: String, month: String): List<Workout>

    @Transaction
    @Query("SELECT * FROM workout_table WHERE id = :workoutId")
    suspend fun getWorkoutWithExercises(workoutId: Int): WorkoutWithExercises

    @Transaction
    @Query("SELECT * FROM exercise WHERE workoutId = :workoutId")
    suspend fun getExercisesWithSets(workoutId: Int): List<ExerciseWithSets>
}
