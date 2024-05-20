package com.szczepaniak.dawid.gymlog.doa

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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

    @Delete
    fun delete(routine: Routine)
}