package com.szczepaniak.dawid.gymlog.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.ApiClient
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.adapters.ExercisesAdapter
import com.szczepaniak.dawid.gymlog.models.Exercise
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.reflect.jvm.internal.impl.renderer.ClassifierNamePolicy.SHORT

class ExcercisesActivity : AppCompatActivity() {

    private lateinit var exerciseRecyclerView: RecyclerView
    private lateinit var exercisesAdapter: ExercisesAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_excercises)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        exerciseRecyclerView = findViewById(R.id.exercises_recycler_view)
        exerciseRecyclerView.layoutManager = LinearLayoutManager(this)

//        val call = ApiClient.apiService.getExercises("chest")
//
//        call.enqueue(object : Callback<List<Exercise>> {
//            override fun onResponse(call: Call<List<Exercise>>, response: Response<List<Exercise>>) {
//                if (response.isSuccessful) {
//                    val exercises = response.body()
//                    exercisesAdapter = ExercisesAdapter(exercises!!, this)
//                    exerciseRecyclerView.setAdapter(exercisesAdapter)
//                } else {
//                    Toast.makeText(this@ExcercisesActivity, "Błąd: ${response.code()}", Toast.LENGTH_SHORT).show()
//                    println("Błąd: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<List<Exercise>>, t: Throwable) {
//                Toast.makeText(this@ExcercisesActivity, "Network Error", Toast.LENGTH_SHORT).show()
//            }
//        })

        val exercises = arrayOf(
            Exercise(
                name = "Incline Hammer Curls",
                type = "strength",
                muscle = "biceps",
                equipment = "dumbbell",
                difficulty = "beginner",
                instructions = "Curl"
            ),
            Exercise(
                name = "Wide-grip barbell curl",
                type = "strength",
                muscle = "biceps",
                equipment = "barbell",
                difficulty = "beginner",
                instructions = "Curl"
            ),
            Exercise(
                name = "EZ-bar spider curl",
                type = "strength",
                muscle = "biceps",
                equipment = "barbell",
                difficulty = "intermediate",
                instructions = "Curl"
            ),
            Exercise(
                name = "Hammer Curls",
                type = "strength",
                muscle = "biceps",
                equipment = "dumbbell",
                difficulty = "intermediate",
                instructions = "Curl"
            ),
            Exercise(
                name = "EZ-Bar Curl",
                type = "strength",
                muscle = "biceps",
                equipment = "e-z_curl_bar",
                difficulty = "intermediate",
                instructions = "Curl"
            ),
            Exercise(
                name = "Zottman Curl",
                type = "strength",
                muscle = "biceps",
                equipment = "None",
                difficulty = "intermediate",
                instructions = "Curl"
            ),
            Exercise(
                name = "Biceps curl to shoulder press",
                type = "strength",
                muscle = "biceps",
                equipment = "dumbbell",
                difficulty = "beginner",
                instructions = "Press"
            ),
            Exercise(
                name = "Barbell Curl",
                type = "strength",
                muscle = "biceps",
                equipment = "barbell",
                difficulty = "intermediate",
                instructions = "Curl"
            ),
            Exercise(
                name = "Concentration curl",
                type = "strength",
                muscle = "biceps",
                equipment = "dumbbell",
                difficulty = "intermediate",
                instructions = "Curl"
            ),
            Exercise(
                name = "Flexor Incline Dumbbell Curls",
                type = "strength",
                muscle = "biceps",
                equipment = "dumbbell",
                difficulty = "beginner",
                instructions = "Curl"
            )
        )
        exercisesAdapter = ExercisesAdapter(exercises, this@ExcercisesActivity)
        exerciseRecyclerView.adapter = exercisesAdapter
        exercisesAdapter.notifyDataSetChanged()

        Toast.makeText(this@ExcercisesActivity, "Rozmiar ${exercisesAdapter.itemCount}", Toast.LENGTH_SHORT).show()
    }
}