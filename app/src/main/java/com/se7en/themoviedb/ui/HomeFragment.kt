package com.se7en.themoviedb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.se7en.themoviedb.adapters.FragmentType
import com.se7en.themoviedb.adapters.HomeFragmentAdapter
import com.se7en.themoviedb.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val fragmentAdapter =
            HomeFragmentAdapter(this)
        pager.adapter = fragmentAdapter

        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = FragmentType.values()[position].toString().replace('_', ' ')
        }.attach()
    }
}
