package com.szczepaniak.dawid.gymlog

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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

        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tabLayout)
        pageAdapter = MainViewPageAdapter(this)

        pageAdapter.addFragment(HomeFragment(), "Home", R.drawable.home_icon)
        pageAdapter.addFragment(WorkoutFragment(), "Workout", R.drawable.workout_icon)
        pageAdapter.addFragment(ProfileFragment(), "Profile", R.drawable.profile_icon)

        viewPager.setAdapter(pageAdapter)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = pageAdapter.getPageTitle(position)
            tab.icon = getResources().getDrawable(pageAdapter.getPageIcon(position))
        }.attach()

    }

}