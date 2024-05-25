package com.szczepaniak.dawid.gymlog.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.TimerService

class WorkoutActivity : AppCompatActivity() {


    private var timerService: TimerService? = null
    private var isBound = false
    private lateinit var timeTextView: TextView
    private lateinit var prefs: SharedPreferences
    private val handler = Handler()
//    private val updateUITask = object : Runnable {
//        override fun run() {
//            val elapsedTime = prefs.getLong("elapsedTime", 0)
//            timeTextView.text = formatTime(elapsedTime)
//            handler.postDelayed(this, 1000)
//        }
//    }

//    private val connection = object : ServiceConnection {
//        override fun onServiceConnected(className: ComponentName, service: IBinder) {
//            val binder = service as TimerService.LocalBinder
//            timerService = binder.getService()
//            isBound = true
//        }
//
//        override fun onServiceDisconnected(arg0: ComponentName) {
//            isBound = false
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        timeTextView = findViewById(R.id.timer_textview)
//        prefs = getSharedPreferences("TrainingPrefs", MODE_PRIVATE)
//
//        val intent = Intent(this, TimerService::class.java)
//        bindService(intent, connection, Context.BIND_AUTO_CREATE)
//
//        val stopBtn: Button = findViewById(R.id.stop_button)
//        stopBtn.setOnClickListener {
//            val serviceIntent = Intent(this, TimerService::class.java)
//            stopService(serviceIntent)
//        }
    }

//    override fun onStart() {
//        super.onStart()
//        handler.post(updateUITask)
//    }
//
//    override fun onStop() {
//        super.onStop()
//        handler.removeCallbacks(updateUITask)
//        if (isBound) {
//            unbindService(connection)
//            isBound = false
//        }
//    }

    private fun formatTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / 1000 / 60) % 60
        val hours = (millis / 1000 / 60 / 60)
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}