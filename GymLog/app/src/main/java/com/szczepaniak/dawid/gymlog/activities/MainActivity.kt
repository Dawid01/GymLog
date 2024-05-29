package com.szczepaniak.dawid.gymlog.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.szczepaniak.dawid.gymlog.R
import com.szczepaniak.dawid.gymlog.RetrofitClient
import com.szczepaniak.dawid.gymlog.adapters.MainViewPageAdapter
import com.szczepaniak.dawid.gymlog.fragments.HomeFragment
import com.szczepaniak.dawid.gymlog.fragments.ProfileFragment
import com.szczepaniak.dawid.gymlog.fragments.WorkoutFragment
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var pageAdapter: MainViewPageAdapter

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        RetrofitClient.context = this
        setUserInfo()


        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tabLayout)
        pageAdapter = MainViewPageAdapter(this)

        pageAdapter.addFragment(HomeFragment(), "Home",
            R.drawable.home_icon,
            R.drawable.home_icon_fill
        )
        pageAdapter.addFragment(WorkoutFragment(), "Workout",
            R.drawable.workout_icon,
            R.drawable.workout_icon_fill
        )
        pageAdapter.addFragment(ProfileFragment(), "Profile",
            R.drawable.profile_icon,
            R.drawable.profile_icon_fill
        )

        viewPager.setAdapter(pageAdapter)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = pageAdapter.getPageTitle(position)
            tab.icon = getResources().getDrawable(pageAdapter.getPageIcon(position))
        }.attach()
        tabLayout.getTabAt(0)?.icon = getResources().getDrawable(pageAdapter.getPageFillIcon(0))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.icon = getResources().getDrawable(pageAdapter.getPageFillIcon(tab?.position!!))

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.icon = getResources().getDrawable(pageAdapter.getPageIcon(tab?.position!!))

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

    }

    @SuppressLint("CutPasteId")
    fun setUserInfo(){
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val username = sharedPref.getString("user_name", "")
        if (username != null) {
            if(username.isEmpty()){

                val dialogView = LayoutInflater.from(this).inflate(R.layout.user_info_dialog, null)
                val nameInputLayout = dialogView.findViewById<TextInputLayout>(R.id.layout_text_name)
                val editTextName = dialogView.findViewById<TextInputEditText>(R.id.edit_text_name)
                val heightInputLayout = dialogView.findViewById<TextInputLayout>(R.id.layout_text_height)
                val editTextHeight = dialogView.findViewById<TextInputEditText>(R.id.edit_text_height)
                val weightInputLayout = dialogView.findViewById<TextInputLayout>(R.id.layout_text_weight)
                val editTextWeight = dialogView.findViewById<TextInputEditText>(R.id.edit_text_weight)
                val datePicker = dialogView.findViewById<DatePicker>(R.id.datePicker)
                val genderGroup: RadioGroup = dialogView.findViewById(R.id.gender_group)

                val dialog = AlertDialog.Builder(this)
                    .setTitle("Enter Your Details")
                    .setView(dialogView)
                    .setCancelable(false)
                    .setPositiveButton("OK") { dialogInterface, _ ->
                    }
                    .create()

                dialog.show()

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val name = editTextName.text.toString().trim()
                    val height = editTextHeight.text.toString().trim()
                    val weight = editTextWeight.text.toString().trim()

                    val day = datePicker.dayOfMonth
                    val month = datePicker.month
                    val year = datePicker.year
                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, day)
                    val selectedDate = calendar.time

                    val gender = genderGroup.getCheckedRadioButtonId()

                    var canSave = true
                    if (name.isEmpty()) {
                        heightInputLayout.isErrorEnabled = false
                        weightInputLayout.isErrorEnabled = false
                        nameInputLayout.error = "Name cannot be empty"
                        canSave = false
                    }else
                    if (height.isEmpty()) {
                        nameInputLayout.isErrorEnabled = false
                        heightInputLayout.isErrorEnabled = false
                        heightInputLayout.error = "Height cannot be empty"
                        canSave = false
                    }else
                    if (weight.isEmpty()) {
                        nameInputLayout.isErrorEnabled = false
                        heightInputLayout.isErrorEnabled = false
                        weightInputLayout.error = "Weight cannot be empty"
                        canSave = false
                    }

                    if(canSave){
                        nameInputLayout.error = null
                        val editor = sharedPref.edit()
                        editor.putString("user_name", name)
                        editor.putString("user_height", height)
                        editor.putString("user_weight", weight)
                        editor.putString("date_of_birth", selectedDate.toString())
                        editor.putInt("gender", gender)
                        editor.apply()
                        dialog.dismiss()
                        recreate()
                    }
                }

            }
        }
    }
}