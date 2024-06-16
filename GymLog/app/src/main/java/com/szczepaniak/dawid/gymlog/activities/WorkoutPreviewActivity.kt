package com.szczepaniak.dawid.gymlog.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import com.szczepaniak.dawid.gymlog.doa.WorkoutDao
import com.szczepaniak.dawid.gymlog.models.Exercise
import com.szczepaniak.dawid.gymlog.models.Workout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.text.SimpleDateFormat
import java.util.Locale

class WorkoutPreviewActivity : AppCompatActivity() {

    private lateinit var workout: Workout
    private lateinit var tvTitle: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvTime: TextView
    private lateinit var tvVolume: TextView
    private lateinit var ratingBar: RatingBar

    private lateinit var exercisesRecyclerView: RecyclerView
    private lateinit var adapter: ExerciseSetAdapter
    private var exercises: MutableList<Exercise> = mutableListOf()


    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_workout_preview)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvTitle = findViewById(R.id.title_text)
        tvDate = findViewById(R.id.date_text)
        tvTime = findViewById(R.id.time_text)
        tvVolume = findViewById(R.id.volume_text)
        ratingBar = findViewById(R.id.rating_bar)

        workout = Singleton.getSelectedWorkout()!!

        tvTitle.text = workout.title
        val dateFormat = SimpleDateFormat("EEEE dd.MM.yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(workout.startTime).capitalize(Locale.ROOT)
        tvDate.text = formattedDate

        tvTime.text = Singleton.formatTime(workout.getDuration())
        ratingBar.rating = workout.rating.toFloat()
        val volumeText = if (workout.volume > 0) {
            if (workout.volume % 1 == 0f) workout.volume.toInt().toString() else workout.volume.toString()
        } else {
            "0"
        }
        tvVolume.text = "$volumeText kg"

        val workoutDao: WorkoutDao = AppDatabase.getInstance(application).workoutDao()


        exercisesRecyclerView = findViewById(R.id.preview_recycler_view)
        exercisesRecyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                exercises = workoutDao.getWorkoutWithExercises(workout.id).exercises.toMutableList()

                for (exercise in exercises) {
                    val exerciseWithSets = workoutDao.getExerciseWithSets(exercise.id)
                    exercise.sets = exerciseWithSets.sets.toMutableList()
                }

                withContext(Dispatchers.Main) {
                    adapter = ExerciseSetAdapter(exercises, applicationContext, false)
                    exercisesRecyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }
}