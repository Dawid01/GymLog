package com.szczepaniak.dawid.gymlog.SheetDialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.adapters.MusclesAdapter

class MusclesSheetFragment : BottomSheetDialogFragment() {


    var musclesList = listOf(
        "",
        "abdominals",
        "abductors",
        "adductors",
        "biceps",
        "calves",
        "chest",
        "forearms",
        "glutes",
        "hamstrings",
        "lats",
        "lower_back",
        "middle_back",
        "neck",
        "quadriceps",
        "traps",
        "triceps"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_muscles_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.muscles_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = MusclesAdapter(musclesList, requireContext());
        recyclerView.adapter = adapter

    }
}