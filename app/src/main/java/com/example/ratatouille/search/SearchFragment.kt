package com.example.ratatouille.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ratatouille.R
import com.example.ratatouille.databinding.FragmentSearchBinding
import com.example.ratatouille.factory.SearchViewModelFactory
import com.example.ratatouille.internetServices.MealRetrofitInstance
import com.example.ratatouille.makeSnackBar

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: SearchViewModel
    lateinit var adapter: SearchMealsAdapter
    private var searchMethod: SearchType = SearchType.NAME
    lateinit var popupMenu: androidx.appcompat.widget.PopupMenu
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupSearchView()
        observeViewModel()
    }

    private fun setupUI() {
        adapter = SearchMealsAdapter(listOf())
        binding.resultRv.adapter = adapter
        binding.resultRv.layoutManager = LinearLayoutManager(requireContext())

        binding.filterBtn.setOnClickListener {
            popupMenu = androidx.appcompat.widget.PopupMenu(requireContext(), binding.filterBtn)
            popupMenu.menuInflater.inflate(R.menu.popup_filter_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->searchMethod=getSearchMethod(item.itemId)
                true
            }
            popupMenu.show()

        }
    }

    private fun setupViewModel() {
        val retrofit = MealRetrofitInstance.retrofitService
        val factory = SearchViewModelFactory(retrofit)
        viewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]

    }
    private fun observeViewModel() {
        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            adapter.meals = meals
            adapter.notifyDataSetChanged()
        }
        viewModel.message.observe(viewLifecycleOwner) { message ->
            makeSnackBar(message, binding.root)
        }
    }
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.search(query, searchMethod)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }
        })
    }

    private fun getSearchMethod(method: Int): SearchType {
        return when (method) {
            R.id.categoryOption -> SearchType.CATEGORY
            R.id.ingredientOption -> SearchType.INGREDIENT
            R.id.nameOption -> SearchType.NAME
            else -> SearchType.AREA
        }
    }

}