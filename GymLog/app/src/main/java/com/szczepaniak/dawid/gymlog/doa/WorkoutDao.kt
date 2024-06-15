package com.szczepaniak.dawid.gymlog.doa

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.szczepaniak.dawid.gymlog.models.Workout

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workout: Workout)

    @Update
    suspend fun update(workout: Workout)

    @Delete
    suspend fun delete(workout: Workout)

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

}