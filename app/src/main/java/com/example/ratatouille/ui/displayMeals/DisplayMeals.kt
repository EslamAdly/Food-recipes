package com.example.ratatouille.ui.displayMeals

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Query
import com.example.ratatouille.R
import com.example.ratatouille.databinding.ActivityDisplayMealsBinding
import com.example.ratatouille.factory.RetrofitViewModelFactory
import com.example.ratatouille.internetServices.API.MealRetrofitInstance
import com.example.ratatouille.ui.search.SearchMealsAdapter

class DisplayMeals : AppCompatActivity() {
    lateinit var viewModel: DisplayMealsViewModel
    lateinit var adapter: SearchMealsAdapter
    lateinit var binding: ActivityDisplayMealsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchType = intent.getStringExtra("searchType")
        val query = intent.getStringExtra("query")
        setupUI(query)
        setupViewModel()
        setupObservers()
        Log.d("displayMeals","searchType: $searchType  , query: $query")
        viewModel.fetchMeals(searchType!!, query!!)
    }

    private fun setupUI(query: String?) {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.queryName.text = query
        adapter = SearchMealsAdapter(listOf())
        binding.rvMeals.adapter = adapter
        binding.rvMeals.layoutManager = LinearLayoutManager(this@DisplayMeals)
    }
    private fun setupViewModel() {
        val retrofit = MealRetrofitInstance.retrofitService
        val factory = RetrofitViewModelFactory(retrofit)
        viewModel = ViewModelProvider(this, factory)[DisplayMealsViewModel::class.java]

    }

    private fun setupObservers() {
        viewModel.meals.observe(this) { meals ->
            adapter.meals = meals
            adapter.notifyDataSetChanged()
        }
        viewModel.isLoading.observe(this){isLoading->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

        }

    }
}