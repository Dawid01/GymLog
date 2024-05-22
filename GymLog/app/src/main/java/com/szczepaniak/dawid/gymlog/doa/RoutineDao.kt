package com.szczepaniak.dawid.gymlog.doa

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.szczepaniak.dawid.gymlog.models.Exercise
import com.szczepaniak.dawid.gymlog.models.Routine

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routine")
    fun getAll(): List<Routine>

    @Query("SELECT * FROM routine WHERE id IN (:routineIds)")
    fun loadAllByIds(routineIds: IntArray): List<Routine>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(routine: Routine)
    @Insert
    fun insertAll(vararg routines: Routine)

    @Query("DELETE FROM routine WHERE id = :routineId")
    suspend fun deleteById(routineId: Int): Int

    @Query("UPDATE routine SET name = :name, exercises = :exercises, muscles = :muscles WHERE id = :id")
    suspend fun updateById(id: Int, name: String, exercises: List<Exercise>, muscles: List<String>): Int
}