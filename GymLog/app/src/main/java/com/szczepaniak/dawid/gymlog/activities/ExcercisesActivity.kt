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
    private var exercises: MutableList<Exercise> = mutableListOf()
    private var page: Int = 0
    private var muscle: String = ""
    private var lastMuscle: String = ""

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

        exercisesAdapter = ExercisesAdapter(exercises,this@ExcercisesActivity)
        exerciseRecyclerView.setAdapter(exercisesAdapter)
        loadExercises(muscle, page)

        exerciseRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    loadExercises(muscle, ++page)
                }
            }
        })


    }

    private fun loadExercises(muscle: String, page:Int){

        if(lastMuscle != muscle) {
            exercises.clear()
        }
        lastMuscle = muscle
        val call = ApiClient.apiService.getExercises(muscle, page * 10)


        call.enqueue(object : Callback<Array<Exercise>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<Array<Exercise>>, response: Response<Array<Exercise>>) {
                if (response.isSuccessful) {
                    val newExercises = response.body()?.toMutableList() ?: mutableListOf()
                    exercises.addAll(newExercises)
                    exercisesAdapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(this@ExcercisesActivity, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<Array<Exercise>>, t: Throwable) {
                Toast.makeText(this@ExcercisesActivity, "Network Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}