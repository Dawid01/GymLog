package com.szczepaniak.dawid.gymlog.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.szczepaniak.dawid.gymlog.AppDatabase
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.adapters.WorkoutAdapter
import com.szczepaniak.dawid.gymlog.models.Workout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
        adapter = WorkoutAdapter()
        workoutRecyclerView.adapter = adapter
        loadWorkouts()
        emptyHistoryView.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE


        calendarView = view.findViewById(R.id.calendar)

        val specialDates = setOf(
            Triple(2024, 4, 1),
            Triple(2024, 4, 5),
            Triple(2024, 4, 10),
            Triple(2024, 4, 15),
            Triple(2024, 4, 20),
            Triple(2024, 4, 25),
            Triple(2024, 4, 30),
            Triple(2024, 5, 1),
            Triple(2024, 5, 5),
            Triple(2024, 5, 10),
            Triple(2024, 5, 15),
            Triple(2024, 5, 20),
        )

        for (t in specialDates) {
            val date = CalendarDay.from(t.first, t.second, t.third)
            calendarView.setDateSelected(date, true)
        }

//        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
//            // Sprawdź, czy data jest stała i wymagana do podkreślenia
//            val isSpecialDate = checkIfSpecialDate(year, month, dayOfMonth)
//            if (isSpecialDate) {
//                view.setBackgroundResource(R.drawable.streaj_weeks)
//            }
//        }

    }


    override fun onResume() {
        super.onResume()
        loadWorkouts()
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

    private fun checkIfSpecialDate(year: Int, month: Int, dayOfMonth: Int): Boolean {
        val specialDates = setOf(
            Triple(2024, 4, 15), // 15 kwietnia 2024
            Triple(2024, 4, 20), // 20 kwietnia 2024
            Triple(2024, 5, 1)   // 1 maja 2024
        )

        return specialDates.contains(Triple(year, month + 1, dayOfMonth))
    }
}