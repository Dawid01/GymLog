package com.szczepaniak.dawid.gymlog.activities

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.Singleton
import com.szczepaniak.dawid.gymlog.TimerService
import com.szczepaniak.dawid.gymlog.adapters.ExerciseSetAdapter
import com.szczepaniak.dawid.gymlog.models.Exercise
import com.szczepaniak.dawid.gymlog.models.Routine

class WorkoutActivity : AppCompatActivity() {


    private lateinit var routine: Routine
    private lateinit var tvRoutineTitle: TextView
    private lateinit var exerciseSetRecyclerView: RecyclerView
    private lateinit var exerciseSetAdapter: ExerciseSetAdapter
    private var exercises: MutableList<Exercise> = mutableListOf()

    private var timerService: TimerService? = null
    private var isBound = false
    private lateinit var timeTextView: TextView
    private lateinit var prefs: SharedPreferences
    private val handler = Handler()

    private val updateUITask = object : Runnable {
        override fun run() {
            val elapsedTime = prefs.getLong("elapsedTime", 0)
            timeTextView.text = formatTime(elapsedTime)
            handler.postDelayed(this, 1000)
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.LocalBinder
            timerService = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            isBound = false
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val isSelected = intent.getBooleanExtra("selected", false)
        exerciseSetRecyclerView = findViewById(R.id.exertcise_sets_recycler_view)
        exerciseSetRecyclerView.layoutManager = LinearLayoutManager(this)


        tvRoutineTitle = findViewById(R.id.routine_title)
        if(isSelected){
            routine = Singleton.getSelectedRoutine()
            tvRoutineTitle.text = routine.name
            exercises = routine.exercises.toMutableList()
        }else{
            tvRoutineTitle.text = "Quick Workout"
        }

        exerciseSetAdapter = ExerciseSetAdapter(exercises, this, object : ExerciseSetAdapter.ValueChangeListener{
            override fun onValueChange() {
                calculateInfoValues()
            }

        })
        exerciseSetRecyclerView.adapter = exerciseSetAdapter

        timeTextView = findViewById(R.id.durration_text)
        prefs = getSharedPreferences("TrainingPrefs", MODE_PRIVATE)

        val intent = Intent(this, TimerService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)

        val stopBtn: Button = findViewById(R.id.finish_button)
        stopBtn.setOnClickListener {
            val serviceIntent = Intent(this, TimerService::class.java)
            stopService(serviceIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        handler.post(updateUITask)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(updateUITask)
        if (isBound) {
            unbindService(connection)
            isBound = false
        }
    }


    private fun formatTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / 1000 / 60) % 60
        val hours = (millis / 1000 / 60 / 60)

        val sb = StringBuilder()

        if (hours > 0) {
            sb.append("$hours h ")
        }
        if (minutes > 0 || hours > 0) {
            sb.append("$minutes min ")
        }
        sb.append("$seconds s")

        return sb.toString()
    }

    fun calculateInfoValues(){

        var setsCount = 0
        var volume = 0

//        for(exercise in exercises){
//
//            for(set in exercises.)
//        }
    }

}