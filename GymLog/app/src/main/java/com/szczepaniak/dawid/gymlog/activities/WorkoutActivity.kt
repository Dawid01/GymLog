package com.szczepaniak.dawid.gymlog.activities

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
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
import com.szczepaniak.dawid.gymlog.adapters.ExerciseSetAdapter
import com.szczepaniak.dawid.gymlog.models.Exercise
import com.szczepaniak.dawid.gymlog.models.Routine
import com.szczepaniak.dawid.gymlog.models.Workout
import java.util.Date
import kotlin.properties.Delegates

class WorkoutActivity : AppCompatActivity() {

    private lateinit var routine: Routine
    private var isSelected by Delegates.notNull<Boolean>()
    private lateinit var tvRoutineTitle: TextView
    private lateinit var exerciseSetRecyclerView: RecyclerView
    private lateinit var exerciseSetAdapter: ExerciseSetAdapter
    private lateinit var tvVolume: TextView
    private lateinit var tvSets: TextView

    private var exercises: MutableList<Exercise> = mutableListOf()
    private lateinit var discardButton: Button

    private lateinit var timeTextView: TextView
    private lateinit var prefs: SharedPreferences
    private val handler = Handler()
    private var currentWorkout: Workout? = null
    private var elapsedTime: Long = 0

    private val updateUITask = object : Runnable {
        override fun run() {
            val currentTime = Date().time
            if (currentWorkout != null) {
                elapsedTime = (currentTime - currentWorkout!!.startTime.time) / 1000
            }
            timeTextView.text = formatTime(elapsedTime)
            handler.postDelayed(this, 1000)
            elapsedTime += 1
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
        prefs = getSharedPreferences("TrainingPrefs", MODE_PRIVATE)
        isSelected = intent.getBooleanExtra("selected", false)
        initWorkout()
        exerciseSetRecyclerView = findViewById(R.id.exertcise_sets_recycler_view)
        exerciseSetRecyclerView.layoutManager = LinearLayoutManager(this)

        tvSets = findViewById(R.id.sets_total_text)
        tvVolume = findViewById(R.id.volume_total_text)


        tvRoutineTitle = findViewById(R.id.routine_title)
        if (isSelected) {
            routine = Singleton.getSelectedRoutine()!!
            tvRoutineTitle.text = routine.name
            exercises = routine.exercises.toMutableList()
        } else {
            if (currentWorkout == null) {
                tvRoutineTitle.text = "Quick Workout"
            } else {
                tvRoutineTitle.text = currentWorkout?.title.toString()
                exercises = currentWorkout?.exercises?.toMutableList() ?: mutableListOf()
            }
        }

        exerciseSetAdapter = ExerciseSetAdapter(exercises, this, object : ExerciseSetAdapter.ValueChangeListener {
            override fun onValueChange() {
                calculateInfoValues()
            }
        })
        exerciseSetRecyclerView.adapter = exerciseSetAdapter

        timeTextView = findViewById(R.id.durration_text)

        val stopBtn: Button = findViewById(R.id.finish_button)
        stopBtn.setOnClickListener {

        }

        discardButton = findViewById(R.id.discard_button)
        discardButton.setOnClickListener {
            Singleton.saveCurrentWorkout(null)
            finish()
        }
        calculateInfoValues()
    }

    private fun initWorkout() {
        val singletonWorkout: Workout? = Singleton.getCurrentWorkout()
        if (singletonWorkout == null) {
            if(isSelected) {
                routine = Singleton.getSelectedRoutine()!!
            }
            currentWorkout = Workout(
                id = 0,
                title = if(isSelected) routine.name else "Quick Workout",
                startTime = Date(),
                endTime = Date(),
                volume = 0f,
                date = Date(),
                exercises = if(isSelected) routine.exercises else emptyList(),
                exerciseSets = emptyList()
            )
            Singleton.saveCurrentWorkout(currentWorkout)
        } else {
            currentWorkout = singletonWorkout
        }
    }

    override fun onStart() {
        super.onStart()
        handler.post(updateUITask)
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(updateUITask)
    }

    private fun formatTime(seconds: Long): String {
        val s = seconds % 60
        val m = (seconds / 60) % 60
        val h = (seconds / 3600)

        return if (h > 0) {
            String.format("%d h %02d min %02d s", h, m, s)
        } else if (m > 0) {
            String.format("%d min %02d s", m, s)
        } else {
            String.format("%d s", s)
        }
    }

    @SuppressLint("SetTextI18n")
    fun calculateInfoValues() {

        var sets = 0
        var volume = 0f

        for (exercise in exercises){

            if(exercise.sets?.isNotEmpty() == true) {
                for (set in exercise.sets!!) {
                    if (set.checked) {
                        sets++
                        volume += set.volume * set.rep
                    }
                }
            }
        }
        tvVolume.text = "$volume kg"
        tvSets.text = "$sets"
        Singleton.saveCurrentWorkout(currentWorkout)
    }
}
