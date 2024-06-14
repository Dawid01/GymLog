package com.szczepaniak.dawid.gymlog

import android.content.Context
import androidx.paging.PagingData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.szczepaniak.dawid.gymlog.doa.RoutineDao
import com.szczepaniak.dawid.gymlog.doa.WorkoutDao
import com.szczepaniak.dawid.gymlog.models.Exercise
import com.szczepaniak.dawid.gymlog.models.Routine
import com.szczepaniak.dawid.gymlog.models.Workout
import java.util.concurrent.Flow

@Database(entities = [Routine::class, Exercise::class, Workout::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun routineDao(): RoutineDao
    abstract fun workoutDao(): WorkoutDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gym_log_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}