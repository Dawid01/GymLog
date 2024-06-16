package com.szczepaniak.dawid.gymlog.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener
import com.szczepaniak.dawid.gymlog.AppDatabase
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.activities.WorkoutPreviewActivity
import com.szczepaniak.dawid.gymlog.adapters.WorkoutAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var workoutRecyclerView: RecyclerView
    private lateinit var adapter: WorkoutAdapter
    private lateinit var emptyHistoryView: View
    private lateinit var calendarView: MaterialCalendarView


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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workoutRecyclerView = view.findViewById(R.id.history_recyclerview)
        emptyHistoryView = view.findViewById(R.id.empyt_history_view)
        workoutRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = WorkoutAdapter(requireContext())
        workoutRecyclerView.adapter = adapter
        loadWorkouts()
        emptyHistoryView.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
        calendarView = view.findViewById(R.id.calendar)
        calendarView.setOnMonthChangedListener { _, date -> loadCalendarWorkouts(date) }
    }

    private fun loadCalendarWorkouts(date: CalendarDay){
        val db = context?.let { AppDatabase.getInstance(it) }
        val workoutDao = db?.workoutDao()
        val calendar = Calendar.getInstance()


        lifecycleScope.launch {
            val year = date.year.toString()
            val month = String.format("%02d", date.month)
            val workouts = withContext(Dispatchers.IO) {
                workoutDao?.getWorkoutsByMonth(year, month)
            }
            withContext(Dispatchers.Main) {
                calendarView.clearSelection()
                if (workouts != null) {
                    for (workout in workouts) {
                        calendar.time = workout.startTime
                        calendarView.setDateSelected(
                            CalendarDay.from(
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH) + 1,
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ), true
                        )
                    }
                }
            }
        }
    }



    override fun onResume() {
        super.onResume()
        loadWorkouts()
        loadCalendarWorkouts(calendarView.currentDate)
    }

    private fun loadWorkouts() {
        val db = context?.let { AppDatabase.getInstance(it) }
        val workoutDao = db?.workoutDao()

        workoutDao?.let { dao ->
            val pager = Pager(
                config = PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = { dao.getWorkoutsByStartDate() }
            )

            val pagingDataFlow = pager.flow.cachedIn(lifecycleScope)

            lifecycleScope.launch {
                pagingDataFlow.collectLatest { pagingData ->
                    adapter.submitData(pagingData)

                    adapter.addLoadStateListener { loadStates ->
                        val isEmpty = adapter.itemCount == 0 && loadStates.refresh is LoadState.NotLoading
                        emptyHistoryView.visibility = if (isEmpty) View.VISIBLE else View.GONE
                    }
                }
            }
        }
   }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}