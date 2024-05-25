package com.szczepaniak.dawid.gymlog

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder

class TimerService : Service() {

    private val binder = LocalBinder()
    private var startTime: Long = 0
    private var isRunning = false
    private val handler = Handler()
    private val updateTimeTask = object : Runnable {
        override fun run() {
            if (isRunning) {
                val elapsedTime = System.currentTimeMillis() - startTime
                // Aktualizuj czas w SharedPreferences
                val prefs = getSharedPreferences("TrainingPrefs", MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putLong("elapsedTime", elapsedTime)
                editor.apply()

                handler.postDelayed(this, 1000) // Aktualizuj co sekundę
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        startTime = System.currentTimeMillis()
        isRunning = true
        handler.post(updateTimeTask)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        handler.removeCallbacks(updateTimeTask)
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class LocalBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }
}