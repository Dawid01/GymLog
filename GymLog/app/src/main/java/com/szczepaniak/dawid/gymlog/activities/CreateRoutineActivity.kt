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
import android.widget.Toast
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
import kotlin.properties.Delegates

class CreateRoutineActivity : AppCompatActivity() {

    private lateinit var excercisesRecyclerView: RecyclerView
    private lateinit var excercisesAdapter: ExercisesAdapter
    private var exercises: MutableList<Exercise> = mutableListOf()
    private val REQUEST_SELECT_CODE = 123
    private lateinit var context: Context

    private lateinit var titleLayout: TextInputLayout
    private lateinit var titleText: TextInputEditText
    private lateinit var deleteButton: Button

    private lateinit var routineDao: RoutineDao
    private var isEditMode by Delegates.notNull<Boolean>()
    private lateinit var editRoutine: Routine

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

        isEditMode = intent.getBooleanExtra("editMode", false)
        deleteButton = findViewById(R.id.delete_button)
        deleteButton.visibility = if(isEditMode) View.VISIBLE else View.GONE
        if(isEditMode){
            editRoutine = Singleton.getEditRoutine()
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

        if(isEditMode){
            titleText.setText(editRoutine.name)
            val title:TextView = findViewById(R.id.create_routine_title)
            title.text = "Edit Routine"
            exercises = editRoutine.exercises.toMutableList()

            deleteButton.setOnClickListener{
                val builder = AlertDialog.Builder(context)
                builder.setTitle(editRoutine.name)
                builder.setMessage("Do you want to delete this routine?")

                builder.setPositiveButton("Yes") { dialog, which ->

                    context.let { ctx ->
                        lifecycleScope.launch(Dispatchers.IO) {
                            val db = AppDatabase.getInstance(ctx)
                            val routineDao = db.routineDao()

                            val rowsDeleted = routineDao.deleteById(Singleton.getEditRoutine().id)

                            withContext(Dispatchers.Main) {
                                if(rowsDeleted > 0) {
                                    dialog.dismiss()
                                    val resultIntent = Intent()
                                    resultIntent.putExtra("mode", "delete")
                                    setResult(Activity.RESULT_OK, resultIntent)
                                    finish()
                                    dialog.dismiss()
                                }else{
                                    Toast.makeText(ctx, "Failed to delete the routine. Please try again.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }

                builder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }

                builder.show()
            }
        }

        val cancel: TextView = findViewById(R.id.cancel_text)
        cancel.setOnClickListener{
            finish()
        }


        val db = AppDatabase.getInstance(this)
        routineDao = db.routineDao()

        val save: TextView = findViewById(R.id.save_text)
        save.setOnClickListener{
            if(!isEditMode) {
                if (titleText.text.isNullOrEmpty()) {
                    titleLayout.error = "This field is required"
                    titleLayout.isErrorEnabled = true
                } else {
                    var musclesSet: MutableSet<String> = mutableSetOf()
                    for (exercise in exercises) {
                        musclesSet.add(exercise.muscle.toString())
                    }
                    var routine =
                        Routine(titleText.text.toString(), exercises, musclesSet.toList().sorted())
                    lifecycleScope.launch {
                        saveRoutine(routine)
                    }
                }
            }else{
                lifecycleScope.launch {

                    if (titleText.text.isNullOrEmpty()) {
                        titleLayout.error = "This field is required"
                        titleLayout.isErrorEnabled = true
                    }else {

                        editRoutine.name = titleText.text.toString()
                        editRoutine.exercises = exercises
                        var musclesSet: MutableSet<String> = mutableSetOf()
                        for (exercise in exercises) {
                            musclesSet.add(exercise.muscle.toString())
                        }
                        editRoutine.muscles = musclesSet.toList()
                        editRoutine(editRoutine)
                    }

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
                    excercisesAdapter.notifyItemRemoved(position)
                    excercisesAdapter.notifyItemRangeChanged(position, exercises.size)
                    //excercisesAdapter.notifyDataSetChanged()
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
            val newRoutineId = routineDao.insert(routine)
            val savedRoutine = routineDao.getById(newRoutineId)
            if (savedRoutine != null) {
                Singleton.setNewRoutine(savedRoutine)
            }
            withContext(Dispatchers.Main) {
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    private suspend fun editRoutine(routine: Routine) {
        withContext(Dispatchers.IO) {
            routineDao.updateById(routine.id, routine.name, routine.exercises, routine.muscles)
            val resultIntent = Intent()
            resultIntent.putExtra("mode", "edit")
            Singleton.setEditRoutine(editRoutine)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

}