package com.szczepaniak.dawid.gymlog.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.szczepaniak.dawid.gymlog.adapters.ExercisesAdapter
import com.szczepaniak.dawid.gymlog.models.Exercise

class CreateRoutineActivity : AppCompatActivity() {

    private lateinit var excercisesRecyclerView: RecyclerView
    private lateinit var excercisesAdapter: ExercisesAdapter
    private var exercises = listOf<Exercise>()
    private val REQUEST_SELECT_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_routine)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        excercisesRecyclerView = findViewById(R.id.exertcises_recycler_view)
        excercisesRecyclerView.layoutManager = LinearLayoutManager(this)
        excercisesRecyclerView.visibility = View.VISIBLE

        val addButton: Button = findViewById(R.id.add_exercise_button)
        addButton.setOnClickListener{
            val intent= Intent(this, ExcercisesActivity::class.java)
            intent.putExtra("select", true)
            startActivityForResult(intent, REQUEST_SELECT_CODE)
        }


        val cancel: TextView = findViewById(R.id.cancel_text)
        cancel.setOnClickListener{
            finish()
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_CODE && resultCode  == Activity.RESULT_OK) {
            val selectedItems = Singleton.getSelectedExercises()
            if(selectedItems.isNotEmpty()) {
                selectedItems.forEach { selectedExercise ->
                    if (!exercises.equals(selectedExercise)) {
                        exercises += selectedExercise
                    }
                }
                //excercisesAdapter.notifyDataSetChanged()
                excercisesAdapter = ExercisesAdapter(exercises, this, false, object : ExercisesAdapter.OnSelectOrUnselectItem {
                    override fun onSelectedChange(selected: HashSet<Exercise>) {
                    }
                })
                excercisesRecyclerView.adapter = excercisesAdapter
                Singleton.clearSelectedExercises()
            }
        }
    }

}