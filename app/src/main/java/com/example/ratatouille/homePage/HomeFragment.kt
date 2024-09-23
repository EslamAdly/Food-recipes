package com.example.ratatouille.homePage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.ratatouille.R
import com.example.ratatouille.data.remote.DetailedMeal
import com.example.ratatouille.databinding.FragmentHomeBinding
import com.example.ratatouille.internetServices.API.MealRetrofitInstance
import com.example.ratatouille.mealView.MealActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var randomMeal: DetailedMeal? = null
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        fetchRandomMeal()
        fetchCategories()
    }

    private fun setupUI() {
        binding.randomMealImg.setOnClickListener {
            randomMeal?.let {
                val intent = Intent(activity, MealActivity::class.java).apply {
                    putExtra("mealId", it.idMeal)
                    putExtra("source", "retrofit")
                }
                startActivity(intent)
            }
        }

        binding.searchBtn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }

        setupCategoryRecyclerView()
    }

    private fun setupCategoryRecyclerView() {
        categoriesAdapter = CategoriesAdapter(emptyList())
        binding.recyclerView.apply {
            adapter = categoriesAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun fetchCategories() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val categoryResponse = MealRetrofitInstance.retrofitService.getCategories()
                if (categoryResponse.isSuccessful) {
                    val categories = categoryResponse.body()?.categories ?: emptyList()
                    Log.d("CategoryResponse", "Categories: $categories")
                    withContext(Dispatchers.Main) {
                        categoriesAdapter.data = categories
                        categoriesAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e("CategoryError", "Failed to fetch categories: ${categoryResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("CategoryError", "Error fetching categories: ${e.message}")
            }
        }
    }

    private fun fetchRandomMeal() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val randomMealResponse = MealRetrofitInstance.retrofitService.getRandomMeal()
                if (randomMealResponse.isSuccessful) {
                    randomMeal = randomMealResponse.body()?.meals?.firstOrNull()
                    withContext(Dispatchers.Main) {
                        randomMeal?.let {
                            Glide.with(this@HomeFragment)
                                .load(it.strMealThumb)
                                .into(binding.randomMealImg)
                        } ?: run {
                            Log.e("RandomMeal", "Random meal is null")
                        }
                    }
                } else {
                    Log.e("RandomMeal", "Failed to fetch random meal: ${randomMealResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("RandomMeal", "Error fetching random meal: ${e.message}")
            }
        }
    }
}
