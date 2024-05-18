package com.szczepaniak.dawid.gymlog.adapters

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPageAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList: MutableList<Fragment> = ArrayList()
    private val fragmentTitleList: MutableList<String> = ArrayList()
    private val iconsList: MutableList<Int> = ArrayList()
    private val fillIconsList: MutableList<Int> = ArrayList()

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String, icon: Int, fillIcon: Int) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
        iconsList.add(icon)
        fillIconsList.add(fillIcon)
    }

    fun getPageTitle(position: Int): CharSequence {
        return fragmentTitleList[position]
    }

    fun getPageIcon(position: Int): Int {
        return iconsList[position]
    }

    fun getPageFillIcon(position: Int): Int {
        return fillIconsList[position]
    }
}