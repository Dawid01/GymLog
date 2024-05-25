package com.szczepaniak.dawid.gymlog.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.CalendarView
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
        setUsername()


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
    fun setUsername(){
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        val username = sharedPref.getString("user_name", "")
        if (username != null) {
            if(username.isEmpty()){

                val dialogView = LayoutInflater.from(this).inflate(R.layout.username_dialog, null)
                val textInputLayout = dialogView.findViewById<TextInputLayout>(R.id.layoutTextName)
                val editTextName = dialogView.findViewById<TextInputEditText>(R.id.editTextName)

                val dialog = AlertDialog.Builder(this)
                    .setTitle("Enter Your Name")
                    .setView(dialogView)
                    .setCancelable(false)
                    .setPositiveButton("OK") { dialogInterface, _ ->
                    }
                    .create()

                dialog.show()

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val name = editTextName.text.toString().trim()
                    if (name.isEmpty()) {
                        textInputLayout.error = "Name cannot be empty"
                    } else {
                        textInputLayout.error = null
                        val editor = sharedPref.edit()
                        editor.putString("user_name", name)
                        editor.apply()
                        dialog.dismiss()
                        recreate()
                    }
                }

            }
        }
    }
}