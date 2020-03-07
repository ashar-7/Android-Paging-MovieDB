package com.se7en.themoviedb.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.se7en.themoviedb.R
import com.se7en.themoviedb.ui.ListFragment

enum class FragmentType { MOVIES, TV_SHOWS }

class HomeFragmentAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        val type = FragmentType.values()[position]
        val fragment = ListFragment()
        fragment.arguments = Bundle().apply {
            putSerializable("type", type)
        }

        return fragment
    }

    override fun getItemCount() = FragmentType.values().size
}
