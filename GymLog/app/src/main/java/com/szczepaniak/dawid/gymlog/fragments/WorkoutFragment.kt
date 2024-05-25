package com.szczepaniak.dawid.gymlog.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.szczepaniak.dawid.gymlog.AppDatabase
import com.szczepaniak.dawid.gymlog.activities.ExcercisesActivity
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.Singleton
import com.szczepaniak.dawid.gymlog.activities.CreateRoutineActivity
import com.szczepaniak.dawid.gymlog.activities.WorkoutActivity
import com.szczepaniak.dawid.gymlog.adapters.RoutinesAdapter
import com.szczepaniak.dawid.gymlog.models.Routine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class WorkoutFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private val REQUEST_CREATE_ROUTINES_CODE = 111
    private val REQUEST_EDIT_ROUTINES_CODE = 222

    private var routines: MutableList<Routine> = mutableListOf()

    private lateinit var exercisesButton: Button
    private lateinit var  newRoutineButton: Button
    private lateinit var  tvRoutinesCount: TextView
    private lateinit var  routinesRecyclerView: RecyclerView
    private lateinit var  emptyRoutinesView: View
    private lateinit var routinesAdapter: RoutinesAdapter
    private lateinit var startEmptyWorkoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_workout, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            WorkoutFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged", "CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        exercisesButton = view.findViewById(R.id.exercises_list_button)
        newRoutineButton = view.findViewById(R.id.new_routine_button)
        tvRoutinesCount = view.findViewById(R.id.routines_list_text)
        routinesRecyclerView = view.findViewById(R.id.routines_recycler_view)
        emptyRoutinesView = view.findViewById(R.id.empyt_routines_view)
        routinesAdapter = RoutinesAdapter(routines, requireContext(), object : RoutinesAdapter.OnItemSettingsClickListener{
            override fun onItemSettingsClick(position: Int) {
                val intent = Intent(activity, CreateRoutineActivity::class.java)
                intent.putExtra("editMode", true)
                Singleton.setEditRoutine(routines[position])
                startActivityForResult(intent, REQUEST_EDIT_ROUTINES_CODE)
            }

        })
        routinesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        routinesRecyclerView.adapter = routinesAdapter

        exercisesButton.setOnClickListener {
            val intent = Intent(activity, ExcercisesActivity::class.java)
            startActivity(intent)
        }

        newRoutineButton.setOnClickListener {
            val intent = Intent(activity, CreateRoutineActivity::class.java)
            startActivityForResult(intent, REQUEST_CREATE_ROUTINES_CODE)
        }

        loadRoutines()
        startEmptyWorkoutButton = view.findViewById(R.id.quick_start_button)
        startEmptyWorkoutButton.setOnClickListener{
            startActivity(Intent(context, WorkoutActivity::class.java))
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CREATE_ROUTINES_CODE -> {
                    routines.add(0, Singleton.getNewRoutine())
                    changeEmptyRoutinesViewVisibility()
                    routinesAdapter.notifyItemInserted(0)
                    routinesAdapter.notifyItemRangeChanged(0, routines.size)
                }

                REQUEST_EDIT_ROUTINES_CODE -> {

                    //loadRoutines()
                    if (data != null) {
                        val index = routines.indexOf(Singleton.getEditRoutine())
                        if(data.getStringExtra("mode").equals("delete")) {
                            routines.removeAt(index)
                            changeEmptyRoutinesViewVisibility()
                            routinesAdapter.notifyItemRemoved(index)
                            routinesAdapter.notifyItemRangeChanged(index, routines.size)
                        }else{
                            routines[index] = Singleton.getEditRoutine()
                            routinesAdapter.notifyItemChanged(index)
                            //routinesAdapter.notifyItemRangeChanged(index, routines.size)
                        }
                    }
                }

            }
        }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    fun loadRoutines(){

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            context?.let {
                val db = AppDatabase.getInstance(it)
                val routineDao = db.routineDao()
                val routinesList = routineDao.getAll().toMutableList()
                val routinesCount = routinesList.count()

                withContext(Dispatchers.Main) {
                    routines.clear()
                    routines.addAll(routinesList)
                    routines.reverse()
                    emptyRoutinesView.visibility = if(routinesList.isEmpty()) View.VISIBLE else View.GONE
                    routinesAdapter.notifyDataSetChanged()
                    routinesRecyclerView.invalidate()
                    tvRoutinesCount.text = "My Routines ($routinesCount)"
                }
            }
        }
    }

    private fun changeEmptyRoutinesViewVisibility(){
        tvRoutinesCount.text = "My Routines (${routines.size})"
        emptyRoutinesView.visibility = if(routines.isEmpty()) View.VISIBLE else View.GONE
    }
}