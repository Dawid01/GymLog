package com.szczepaniak.dawid.gymlog.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.szczepaniak.dawid.gymlog.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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