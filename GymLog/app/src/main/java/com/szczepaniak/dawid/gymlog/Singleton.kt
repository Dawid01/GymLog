package com.szczepaniak.dawid.gymlog

import android.content.Context
import com.szczepaniak.dawid.gymlog.models.Exercise

class Singleton(applicationContext: Context) {
    private var instance: Singleton? = null
    public lateinit var exercise: Exercise

    public fun getInstance(context: Context): Singleton{
        if (instance == null){
            instance = Singleton(context.applicationContext)
        }
        return instance!!
    }
}