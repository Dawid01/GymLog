package com.szczepaniak.dawid.gymlog.activities

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.AppDatabase
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.Singleton
import com.szczepaniak.dawid.gymlog.adapters.ExerciseSetAdapter
import com.szczepaniak.dawid.gymlog.models.Exercise
import com.szczepaniak.dawid.gymlog.models.Routine
import com.szczepaniak.dawid.gymlog.models.Workout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private lateinit var backIcon: ImageView

    private var exercises: MutableList<Exercise> = mutableListOf()
    private lateinit var finishButton: Button
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
            timeTextView.text = Singleton.formatTime(elapsedTime)
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

        finishButton = findViewById(R.id.finish_button)
        finishButton.setOnClickListener {
            saveWorkout()
        }

        discardButton = findViewById(R.id.discard_button)
        discardButton.setOnClickListener {
            Singleton.saveCurrentWorkout(null)
            finish()
        }

        backIcon = findViewById(R.id.back_icon)
        backIcon.setOnClickListener {
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
                rating = 0,
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

    private fun saveWorkout(){

        if(currentWorkout != null) {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.workout_save_dialog, null)
            val ratingBar: RatingBar = dialogView.findViewById(R.id.rating_bar)
            val dialog = AlertDialog.Builder(this)
                .setTitle("Save ${currentWorkout?.title}")
                .setView(dialogView)
                .setCancelable(false)
                .setPositiveButton("Save") { _, _ ->

                    lifecycleScope.launch(Dispatchers.IO) {
                        this.let {
                            val db = AppDatabase.getInstance(applicationContext)
                            val workoutDao = db.workoutDao()
                            val mutableExerciseSets = currentWorkout!!.exerciseSets.toMutableList()
                            val mutableExercises = currentWorkout!!.exercises.toMutableList()
                            mutableExerciseSets.removeAll { !it.checked }
                            mutableExercises.removeAll { it.sets.isNullOrEmpty() }

                            currentWorkout!!.rating = ratingBar.rating.toInt()
                            currentWorkout!!.exerciseSets = mutableExerciseSets
                            currentWorkout!!.exercises = mutableExercises
                            currentWorkout!!.endTime = Date()

                            workoutDao.insert(currentWorkout!!)
                            withContext(Dispatchers.Main) { Singleton.saveCurrentWorkout(null)
                                finish()
                            }
                        }
                    }


                }.setNegativeButton("Cancel"){dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .create()

            dialog.show()
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
        val volumeText = if (volume > 0) {
            if (volume % 1 == 0f) volume.toInt().toString() else volume.toString()
        } else {
            "0"
        }
        currentWorkout?.volume  = volume
        tvVolume.text = "$volumeText kg"
        tvSets.text = "$sets"
        Singleton.saveCurrentWorkout(currentWorkout)
    }
}
