package com.szczepaniak.dawid.gymlog.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.SearchView
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

class ExcercisesActivity : AppCompatActivity() {

    private lateinit var exerciseRecyclerView: RecyclerView
    private lateinit var exercisesAdapter: ExercisesAdapter
    private lateinit var searchView: SearchView

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
        searchView = findViewById(R.id.searchView)
        searchView.queryHint = "Search excercises"

        val call = ApiClient.apiService.getExercises("chest")


        call.enqueue(object : Callback<Array<Exercise>> {
            override fun onResponse(call: Call<Array<Exercise>>, response: Response<Array<Exercise>>) {
                if (response.isSuccessful) {
                    val exercises = response.body()
                    exercisesAdapter = ExercisesAdapter(exercises!!,this@ExcercisesActivity)
                    exerciseRecyclerView.setAdapter(exercisesAdapter)
                } else {
                    Toast.makeText(this@ExcercisesActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<Array<Exercise>>, t: Throwable) {
                Toast.makeText(this@ExcercisesActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })

        /*val exercises = arrayOf(
            Exercise(
                name = "Incline Hammer Curls",
                type = "strength",
                muscle = "triceps",
                equipment = "dumbbell",
                difficulty = "beginner",
                instructions = "Curl"
            ),
            Exercise(
                name = "Wide-grip barbell curl",
                type = "strength",
                muscle = "lats",
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
                muscle = "adductors",
                equipment = "dumbbell",
                difficulty = "expert",
                instructions = "Curl"
            ),
            Exercise(
                name = "EZ-Bar Curl",
                type = "strength",
                muscle = "abdominals",
                equipment = "e-z_curl_bar",
                difficulty = "intermediate",
                instructions = "Curl"
            ),
            Exercise(
                name = "Zottman Curl",
                type = "strength",
                muscle = "glutes",
                equipment = "None",
                difficulty = "intermediate",
                instructions = "Curl"
            ),
            Exercise(
                name = "Biceps curl to shoulder press",
                type = "strength",
                muscle = "traps",
                equipment = "dumbbell",
                difficulty = "beginner",
                instructions = "Press"
            ),
            Exercise(
                name = "Barbell Curl",
                type = "strength",
                muscle = "hamstrings",
                equipment = "barbell",
                difficulty = "expert",
                instructions = "Curl"
            ),
            Exercise(
                name = "Concentration curl",
                type = "strength",
                muscle = "chest",
                equipment = "dumbbell",
                difficulty = "intermediate",
                instructions = "Curl"
            ),
            Exercise(
                name = "Flexor Incline Dumbbell Curls",
                type = "strength",
                muscle = "calves",
                equipment = "dumbbell",
                difficulty = "expert",
                instructions = "Curl"
            )
        )
        exercisesAdapter = ExercisesAdapter(exercises, this)
        exerciseRecyclerView.adapter = exercisesAdapter*/
    }
}