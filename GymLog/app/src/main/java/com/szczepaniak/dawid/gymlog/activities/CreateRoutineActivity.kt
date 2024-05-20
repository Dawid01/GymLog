package com.szczepaniak.dawid.gymlog.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.szczepaniak.dawid.gymlog.AppDatabase
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.Singleton
import com.szczepaniak.dawid.gymlog.adapters.ExercisesAdapter
import com.szczepaniak.dawid.gymlog.doa.RoutineDao
import com.szczepaniak.dawid.gymlog.models.Exercise
import com.szczepaniak.dawid.gymlog.models.Routine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateRoutineActivity : AppCompatActivity() {

    private lateinit var excercisesRecyclerView: RecyclerView
    private lateinit var excercisesAdapter: ExercisesAdapter
    private var exercises: MutableList<Exercise> = mutableListOf()
    private val REQUEST_SELECT_CODE = 123
    private lateinit var context: Context

    private lateinit var titleLayout: TextInputLayout
    private lateinit var titleText: TextInputEditText

    private lateinit var routineDao: RoutineDao

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_routine)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        context = this
        excercisesRecyclerView = findViewById(R.id.create_routine_recycler_view)
        excercisesRecyclerView.layoutManager = LinearLayoutManager(this)
        excercisesRecyclerView.visibility = View.VISIBLE

        val addButton: Button = findViewById(R.id.add_exercise_button)
        addButton.setOnClickListener{
            val intent = Intent(this, ExcercisesActivity::class.java)
            intent.putExtra("select", true)
            startActivityForResult(intent, REQUEST_SELECT_CODE)
        }

        titleLayout = findViewById(R.id.routine_title_layout)
        titleText = findViewById(R.id.routine_title_text)

        val cancel: TextView = findViewById(R.id.cancel_text)
        cancel.setOnClickListener{
            finish()
        }


        val db = AppDatabase.getInstance(this)
        routineDao = db.routineDao()

        val save: TextView = findViewById(R.id.save_text)
        save.setOnClickListener{
            if(titleText.text.isNullOrEmpty()){
                titleLayout.error = "This field is required"
                titleLayout.isErrorEnabled = true
            }else{
                var routine = Routine("titleText.text", exercises)
                lifecycleScope.launch {
                    saveRoutine(routine)
                    finish()
                }
            }
        }

        titleText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    titleLayout.error = null
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })


        excercisesAdapter = ExercisesAdapter(exercises, this, false, null, object : ExercisesAdapter.OnItemClickListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onItemClick(position: Int) {
                val clickedExercise = exercises[position]
                val builder = AlertDialog.Builder(context)
                builder.setTitle(clickedExercise.name.toString().replace("_", " ").uppercase())
                builder.setMessage("Do you want to delete this exercise?")

                builder.setPositiveButton("Yes") { dialog, which ->
                    exercises.removeAt(position)
                    excercisesAdapter.notifyDataSetChanged()
                    dialog.dismiss()
                }

                builder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }

                builder.show()
            }
        })
        excercisesRecyclerView.adapter = excercisesAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SELECT_CODE && resultCode == Activity.RESULT_OK) {
            val selectedItems = Singleton.getSelectedExercises()
            if (selectedItems.isNotEmpty()) {
                selectedItems.forEach { selectedExercise ->
                    if (!exercises.any { it.equals(selectedExercise) }) {
                        exercises.add(selectedExercise)
                    }
                }
                excercisesAdapter.notifyDataSetChanged()
                Singleton.clearSelectedExercises()
            }
        }
    }

    private suspend fun saveRoutine(routine: Routine) {
        withContext(Dispatchers.IO) {
            routineDao.insert(routine)
        }
    }

}