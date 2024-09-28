package com.example.ratatouille.ui.homePage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.ratatouille.R
import com.example.ratatouille.data.remote.DetailedMeal
import com.example.ratatouille.databinding.FragmentHomeBinding
import com.example.ratatouille.ui.mealView.MealActivity

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var viewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupUI()
        setupObservers()
        viewModel.fetchRandomMeal()
        viewModel.fetchCategories()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    private fun setupUI() {
        binding.randomMealImg.setOnClickListener {
            viewModel.randomMeal.value?.let { meal ->
                val intent = Intent(activity, MealActivity::class.java).apply {
                    putExtra("mealId", meal.idMeal)
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

    private fun setupObservers() {
        viewModel.randomMeal.observe(viewLifecycleOwner) { meal ->
            Glide.with(binding.randomMealImg.context)
                .load(meal?.strMealThumb)
                .into(binding.randomMealImg)
        }

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.data = categories
            categoriesAdapter.notifyDataSetChanged()
        }

    }
}

