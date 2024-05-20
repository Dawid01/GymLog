package com.szczepaniak.dawid.gymlog.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.ApiClient
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.SheetDialogs.MusclesSheetFragment
import com.szczepaniak.dawid.gymlog.Singleton
import com.szczepaniak.dawid.gymlog.adapters.ExercisesAdapter
import com.szczepaniak.dawid.gymlog.models.Exercise
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExcercisesActivity : AppCompatActivity() {

    private lateinit var exerciseRecyclerView: RecyclerView
    private lateinit var notFoundView: View
    private lateinit var exercisesAdapter: ExercisesAdapter
    private lateinit var searchView: SearchView
    private lateinit var addSelected: Button
    private var exercises: MutableList<Exercise> = mutableListOf()
    private var page: Int = 0
    private var muscle: String = ""
    private var lastMuscle: String = ""
    private var selectedExercises: MutableList<Exercise> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_excercises)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        exerciseRecyclerView = findViewById(R.id.exertcises_recycler_view)
        notFoundView = findViewById(R.id.empyt_routines_view)
        exerciseRecyclerView.layoutManager = LinearLayoutManager(this)
        searchView = findViewById(R.id.searchView)
        searchView.queryHint = "Search excercises"
        addSelected = findViewById(R.id.add_selected_button)

        val canSelect: Boolean = intent.getBooleanExtra("select", false)
        val title: TextView = findViewById(R.id.excercises_title)
        if(canSelect){
            title.text = "Select excercises"
            title.textSize = 20f

            addSelected.setOnClickListener {
                Singleton.setSelectedExercise(selectedExercises)
                val resultIntent = Intent()
                resultIntent.putExtra("test", "test")
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
        exercisesAdapter = ExercisesAdapter(exercises,this@ExcercisesActivity, canSelect, object : ExercisesAdapter.OnSelectOrUnselectItem{
            override fun onSelectedChange(selected: HashSet<Exercise>) {
                if(canSelect) {
                    selectedExercises = selected.toMutableList()
                    addSelected.visibility = if (selected.size > 0) View.VISIBLE else View.GONE
                    addSelected.text = "Add selected (${selected.size})"
                }
            }
        })
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


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                if(newText == ""){
                    loadExercises(muscle, page, true)
                }
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                page = 0
                loadExercises(muscle, page, true)
                searchView.clearFocus()
                return false
            }
        })

        val musclesButton: Button = findViewById(R.id.add_exercise_button)
        musclesButton.setOnClickListener {
            val musclesSheet = MusclesSheetFragment()
            musclesSheet.setOnDataReturnedListener { data ->
                muscle = data.toString()
                if(muscle != "") {
                    musclesButton.text = muscle.replace("_", " ").uppercase()
                }else{
                    musclesButton.text = "All Muscles"
                }
                page = 0
                loadExercises(muscle, page, true)
            }
            musclesSheet.show(supportFragmentManager, musclesSheet.tag)
        }

       val cancel: TextView = findViewById(R.id.cancel_text)
       cancel.setOnClickListener{
            finish()
       }

    }


    private fun loadExercises(muscle: String, page:Int, clearOld: Boolean = false){

        if(lastMuscle != muscle || clearOld) {
            exercises.clear()
        }
        lastMuscle = muscle
        val call = ApiClient.apiService.getExercises(muscle, searchView.query.toString(),page * 10)


        call.enqueue(object : Callback<Array<Exercise>> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<Array<Exercise>>, response: Response<Array<Exercise>>) {
                if (response.isSuccessful) {
                    val newExercises = response.body()?.toMutableList() ?: mutableListOf()
                    exercises.addAll(newExercises)
                    exercisesAdapter.notifyDataSetChanged()

                    if(exercises.isEmpty()){
                        notFoundView.visibility = View.VISIBLE
                    }else{
                        notFoundView.visibility = View.GONE
                    }

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