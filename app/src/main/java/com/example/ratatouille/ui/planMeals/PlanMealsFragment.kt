package com.example.ratatouille.ui.planMeals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ratatouille.R
import com.example.ratatouille.dataBase.FavoriteDatabase
import com.example.ratatouille.databinding.FragmentFavoriteBinding
import com.example.ratatouille.databinding.FragmentPlanMealsBinding
import com.example.ratatouille.factory.FavoriteViewModeFactory
import com.example.ratatouille.factory.PlanMealsViewModelFactory
import com.example.ratatouille.ui.favoriteMeals.FavoriteMealsAdapter
import com.example.ratatouille.ui.favoriteMeals.FavoriteMealsViewModel


class PlanMealsFragment : Fragment() {

    private lateinit var saturdayAdapter: PlanMealAdapter
    private lateinit var sundayAdapter: PlanMealAdapter
    private lateinit var mondayAdapter: PlanMealAdapter
    private lateinit var tuesdayAdapter: PlanMealAdapter
    private lateinit var wednesdayAdapter: PlanMealAdapter
    private lateinit var thursdayAdapter: PlanMealAdapter
    private lateinit var fridayAdapter: PlanMealAdapter

    lateinit var viewModel: PlanMealViewModel
    lateinit var binding: FragmentPlanMealsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
setupViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =FragmentPlanMealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()

    }
    private fun setupUI() {
        saturdayAdapter = PlanMealAdapter(listOf())
        sundayAdapter = PlanMealAdapter(listOf())
        mondayAdapter = PlanMealAdapter(listOf())
        tuesdayAdapter = PlanMealAdapter(listOf())
        wednesdayAdapter = PlanMealAdapter(listOf())
        thursdayAdapter = PlanMealAdapter(listOf())
        fridayAdapter = PlanMealAdapter(listOf())

        setupRecyclerView(binding.satRecyclerView, saturdayAdapter)
        setupRecyclerView(binding.sunRecyclerView, sundayAdapter)
        setupRecyclerView(binding.monRecyclerView, mondayAdapter)
        setupRecyclerView(binding.tueRecyclerView, tuesdayAdapter)
        setupRecyclerView(binding.wedRecyclerView, wednesdayAdapter)
        setupRecyclerView(binding.thuRecyclerView, thursdayAdapter)
        setupRecyclerView(binding.friRecyclerView, fridayAdapter)
    }
    private fun setupRecyclerView(recyclerView: RecyclerView, adapter: PlanMealAdapter) {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }
    private fun setupViewModel(){
        val dao = FavoriteDatabase.getFavoriteDatabase(requireContext()).getMealsPlanDao()
        val factory= PlanMealsViewModelFactory(dao)
        viewModel = ViewModelProvider(this,factory)[PlanMealViewModel::class.java]

    }
    private fun observeViewModel() {
        viewModel.saturdayMeals.observe(viewLifecycleOwner) {
            saturdayAdapter.meals = it
            saturdayAdapter.notifyDataSetChanged()
        }
        viewModel.sundayMeals.observe(viewLifecycleOwner) {
            sundayAdapter.meals = it
            sundayAdapter.notifyDataSetChanged()
        }
        viewModel.mondayMeals.observe(viewLifecycleOwner) {
            mondayAdapter.meals = it
            mondayAdapter.notifyDataSetChanged()
        }
        viewModel.tuesdayMeals.observe(viewLifecycleOwner) {
            tuesdayAdapter.meals = it
            tuesdayAdapter.notifyDataSetChanged()
        }
        viewModel.wednesdayMeals.observe(viewLifecycleOwner) {
            wednesdayAdapter.meals = it
            wednesdayAdapter.notifyDataSetChanged()
        }
        viewModel.thursdayMeals.observe(viewLifecycleOwner) {
            thursdayAdapter.meals = it
            thursdayAdapter.notifyDataSetChanged()
        }
        viewModel.fridayMeals.observe(viewLifecycleOwner) {
            fridayAdapter.meals = it
        }

    }
}