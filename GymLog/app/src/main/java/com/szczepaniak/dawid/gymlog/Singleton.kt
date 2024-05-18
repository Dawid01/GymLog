package com.szczepaniak.dawid.gymlog

import android.content.Context

class Singleton(applicationContext: Context) {
    private var instance: Singleton? = null

    fun getInstance(context: Context): Singleton{
        if (instance == null){
            instance = Singleton(context.applicationContext)
        }
        return instance!!
    }
}