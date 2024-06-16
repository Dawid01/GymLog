package com.szczepaniak.dawid.gymlog.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.szczepaniak.dawid.gymlog.AppDatabase
import com.szczepaniak.dawid.gymlog.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.math.pow


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null


    private lateinit var sharedPref: SharedPreferences

    private lateinit var tvUserName: TextView

    private lateinit var tvWorkoutsCount: TextView
    private lateinit var tvVolumeTotal: TextView

    private lateinit var tvAge: TextView
    private lateinit var tvGender: TextView
    private lateinit var tvWeight: TextView
    private lateinit var tvHeight: TextView
    private lateinit var tvBMI: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvUserName = view.findViewById(R.id.user_name)
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)!!
        tvUserName.text = "(${sharedPref.getString("user_name", "")})"

        tvWorkoutsCount = view.findViewById(R.id.workouts_count_text)
        tvVolumeTotal = view.findViewById(R.id.total_volume_text)

        tvAge = view.findViewById(R.id.value_age)
        tvGender = view.findViewById(R.id.value_gender)
        tvWeight = view.findViewById(R.id.value_weight)
        tvHeight = view.findViewById(R.id.value_height)
        tvBMI = view.findViewById(R.id.value_bmi)
        initializeUserInfo()
        updateUserStats()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onResume() {
        super.onResume()
        updateUserStats()
    }

    @SuppressLint("SetTextI18n")
    fun initializeUserInfo(){

        val height = sharedPref.getFloat("user_height", 1f)
        val weight = sharedPref.getFloat("user_weight", 1f)
        val bmi = weight / (height.toDouble() / 100).pow(2)
        val date = sharedPref.getString("date_of_birth", "01-06-2024")
        val gender = sharedPref.getInt("gender", 0)

        tvAge.text = "${date?.let { calculateAge(it) }}"
        tvWeight.text = "$weight kg"
        tvHeight.text = "$height cm"
        tvBMI.text = String.format("%.2f", bmi)
        tvGender.text = if(gender == 1) "Male ♂\uFE0F" else "Female ♀\uFE0F "
        val color = when {
            bmi < 18.5 -> Color.parseColor("#4FC3F7")
            bmi in 18.5..24.9 -> Color.parseColor("#81C784")
            bmi in 25.0..29.9 -> Color.parseColor("#FFB74D")
            else -> Color.parseColor("#E57373")
        }
        tvBMI.setTextColor(color)
    }

    @SuppressLint("SetTextI18n")
    private fun updateUserStats(){
        val db = context?.let { AppDatabase.getInstance(it) }
        val workoutDao = db?.workoutDao()

        lifecycleScope.launch {
            var totalVolume = workoutDao?.totalVolume()
            if(totalVolume == null){
                totalVolume = 0
            }
            val countWorkouts = workoutDao?.countWorkouts()
            withContext(Dispatchers.Main) {
                tvVolumeTotal.text = "$totalVolume"
                tvWorkoutsCount.text = "$countWorkouts"
            }
        }

    }

    private fun calculateAge(birthDate: String): Int {
        val formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val parsedBirthDate = LocalDate.parse(birthDate, formatter)
        val currentDate = LocalDate.now()
        return ChronoUnit.YEARS.between(parsedBirthDate, currentDate).toInt()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}