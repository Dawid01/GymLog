package com.szczepaniak.dawid.gymlog

import android.provider.BaseColumns

object MyContract {
    object WorkoutEntry : BaseColumns {
        const val ID = "id"
        const val TABLE_NAME = "workout"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DATE = "date"
    }
    object WorkoutShemeEntry : BaseColumns {
        const val ID = "id"
        const val TABLE_NAME = "workout"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DATE = "date"
    }
    object ExerciseEntry : BaseColumns {
        const val ID = "id"
        const val TABLE_NAME = "exercise"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_REPS = "reps"
        const val COLUMN_NAME_SETS = "sets"
    }

    object ExerciseShemeEntry : BaseColumns {
        const val ID = "id"
        const val TABLE_NAME = "exercise"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_REPS = "reps"
        const val COLUMN_NAME_SETS = "sets"
    }
}
