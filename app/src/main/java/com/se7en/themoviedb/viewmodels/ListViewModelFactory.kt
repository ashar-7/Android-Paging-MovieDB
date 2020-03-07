package com.se7en.themoviedb.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.se7en.themoviedb.adapters.FragmentType

class ListViewModelFactory(private val type: FragmentType): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(type) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
