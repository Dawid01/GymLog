package com.szczepaniak.dawid.gymlog

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseMgr(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_WORKOUT_ENTRIES)
        db.execSQL(SQL_CREATE_WORKOUT_SHEME_ENTRIES)
        db.execSQL(SQL_CREATE_EXERCISE_ENTRIES)
        db.execSQL(SQL_CREATE_EXERCISE_SHEME_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_WORKOUT_ENTRIES)
        db.execSQL(SQL_DELETE_WORKOUT_SHEME_ENTRIES)
        db.execSQL(SQL_DELETE_EXERCISE_ENTRIES)
        db.execSQL(SQL_DELETE_EXERCISE_SHEME_ENTRIES)
        onCreate(db)
    }

    companion object {

        const val DATABASE_NAME = "GymLogDatabase.db"
        const val DATABASE_VERSION = 1

        private const val SQL_CREATE_WORKOUT_ENTRIES = "CREATE TABLE IF NOT EXISTS ${MyContract.WorkoutEntry.TABLE_NAME} (" +
                "${MyContract.WorkoutEntry.ID} INTEGER PRIMARY KEY," +
                "${MyContract.WorkoutEntry.COLUMN_NAME_TITLE} TEXT," +
                "${MyContract.WorkoutEntry.COLUMN_NAME_DATE} TEXT)"

        private const val SQL_DELETE_WORKOUT_ENTRIES = "DROP TABLE IF EXISTS ${MyContract.WorkoutShemeEntry.TABLE_NAME}"

        private const val SQL_CREATE_WORKOUT_SHEME_ENTRIES = "CREATE TABLE IF NOT EXISTS ${MyContract.WorkoutShemeEntry.TABLE_NAME} (" +
                "${MyContract.WorkoutShemeEntry.ID} INTEGER PRIMARY KEY," +
                "${MyContract.WorkoutShemeEntry.COLUMN_NAME_TITLE} TEXT," +
                "${MyContract.WorkoutShemeEntry.COLUMN_NAME_DATE} TEXT)"

        private const val SQL_DELETE_WORKOUT_SHEME_ENTRIES = "DROP TABLE IF EXISTS ${MyContract.WorkoutShemeEntry.TABLE_NAME}"

        private const val SQL_CREATE_EXERCISE_ENTRIES = "CREATE TABLE IF NOT EXISTS ${MyContract.ExerciseEntry.TABLE_NAME} (" +
                "${MyContract.ExerciseEntry.ID} INTEGER PRIMARY KEY," +
                "${MyContract.ExerciseEntry.COLUMN_NAME_TITLE} TEXT," +
                "${MyContract.ExerciseEntry.COLUMN_NAME_REPS} INTEGER," +
                "${MyContract.ExerciseEntry.COLUMN_NAME_SETS} INTEGER)"

        private const val SQL_DELETE_EXERCISE_ENTRIES = "DROP TABLE IF EXISTS ${MyContract.ExerciseEntry.TABLE_NAME}"

        private const val SQL_CREATE_EXERCISE_SHEME_ENTRIES = "CREATE TABLE IF NOT EXISTS ${MyContract.ExerciseShemeEntry.TABLE_NAME} (" +
                "${MyContract.ExerciseShemeEntry.ID} INTEGER PRIMARY KEY," +
                "${MyContract.ExerciseShemeEntry.COLUMN_NAME_TITLE} TEXT," +
                "${MyContract.ExerciseShemeEntry.COLUMN_NAME_REPS} INTEGER," +
                "${MyContract.ExerciseShemeEntry.COLUMN_NAME_SETS} INTEGER)"

        private const val SQL_DELETE_EXERCISE_SHEME_ENTRIES = "DROP TABLE IF EXISTS ${MyContract.ExerciseShemeEntry.TABLE_NAME}"

    }
}