package com.example.ratatouille.ui.search

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
import com.example.ratatouille.factory.RetrofitViewModelFactory
import com.example.ratatouille.internetServices.API.MealRetrofitInstance

class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: com.example.ratatouille.ui.search.SearchViewModel
    lateinit var adapter: com.example.ratatouille.ui.search.SearchMealsAdapter
    private var searchMethod: com.example.ratatouille.ui.search.SearchType =
        com.example.ratatouille.ui.search.SearchType.NAME
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
        adapter = com.example.ratatouille.ui.search.SearchMealsAdapter(listOf())
        binding.resultRv.adapter = adapter
        binding.resultRv.layoutManager = LinearLayoutManager(requireContext())

        binding.filterBtn.setOnClickListener {
            popupMenu = androidx.appcompat.widget.PopupMenu(requireContext(), binding.filterBtn)
            popupMenu.menuInflater.inflate(R.menu.popup_filter_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                searchMethod = getSearchMethod(item.itemId)
                true
            }
            popupMenu.show()

        }
    }

    private fun setupViewModel() {
        val retrofit = MealRetrofitInstance.retrofitService
        val factory = RetrofitViewModelFactory(retrofit)
        viewModel = ViewModelProvider(this, factory)[com.example.ratatouille.ui.search.SearchViewModel::class.java]

    }

    private fun observeViewModel() {
        viewModel.meals.observe(viewLifecycleOwner) { meals ->
            if (meals.isEmpty()) {
                binding.resultRv.visibility = View.GONE
            } else {
                binding.resultRv.visibility = View.VISIBLE
                binding.errorMessage.visibility = View.GONE
            }
            adapter.meals = meals
            adapter.notifyDataSetChanged()
        }
        viewModel.message.observe(viewLifecycleOwner) { message ->
//            makeToast(message, this@SearchFragment.requireContext())
            binding.errorMessage.text = message
            binding.errorMessage.visibility = View.VISIBLE
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

    private fun getSearchMethod(method: Int): com.example.ratatouille.ui.search.SearchType {
        return when (method) {
            R.id.categoryOption -> com.example.ratatouille.ui.search.SearchType.CATEGORY
            R.id.ingredientOption -> com.example.ratatouille.ui.search.SearchType.INGREDIENT
            R.id.nameOption -> com.example.ratatouille.ui.search.SearchType.NAME
            else -> com.example.ratatouille.ui.search.SearchType.AREA
        }
    }

}