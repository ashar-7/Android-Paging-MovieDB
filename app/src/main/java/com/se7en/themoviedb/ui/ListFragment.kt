package com.se7en.themoviedb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.se7en.themoviedb.NetworkState
import com.se7en.themoviedb.R
import com.se7en.themoviedb.adapters.FragmentType
import com.se7en.themoviedb.adapters.HomeRecyclerAdapter
import com.se7en.themoviedb.viewmodels.ListViewModel
import com.se7en.themoviedb.viewmodels.ListViewModelFactory
import kotlinx.android.synthetic.main.fragment_list.*

// TODO: Movie details, favorites

class ListFragment: Fragment() {

    private lateinit var viewModel: ListViewModel
    private val adapter = HomeRecyclerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val argKey = "type"
        if(arguments?.containsKey(argKey) != true) {
            throw Exception("Fragment type not found")
        }
        val type = arguments?.getSerializable(argKey) as FragmentType

        val viewModelFactory = ListViewModelFactory(type)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ListViewModel::class.java)

        recyclerView.apply {
            adapter = this@ListFragment.adapter
            layoutManager = GridLayoutManager(context, 3)
        }

        retryButton.setOnClickListener { viewModel.retry() }

        swipeRefresh.setOnRefreshListener { viewModel.refresh() }

        viewModel.refreshState.observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing = it is NetworkState.Loading
        })

        viewModel.pagedList.observe(viewLifecycleOwner, Observer { list ->
            list?.let {
                adapter.submitList(it)
            }
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer { state ->
            when(state) {
                is NetworkState.Loading -> {
                    retryLayout.visibility = View.GONE
                    emptyLayout.visibility = View.GONE
                }
                is NetworkState.Failure -> {
                    emptyLayout.visibility = View.GONE
                    if(state.isInitial) {
                        retryLayout.visibility = View.VISIBLE
                    }
                }
                is NetworkState.Success -> {
                    retryLayout.visibility = View.GONE
                    if(state.isEmpty) {
                        emptyLayout.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            queryHint = getString(R.string.search_hint)

            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.setQuery(query ?: "")
                    clearFocus()
                    return false
                }
            })

            setOnCloseListener {
                viewModel.setQuery("")
                false
            }

            viewModel.setQuery(query.toString())
        }
    }
}
