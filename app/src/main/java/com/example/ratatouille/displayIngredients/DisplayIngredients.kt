package com.example.ratatouille.displayIngredients

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ratatouille.dataBase.FavoriteDatabase
import com.example.ratatouille.databinding.FragmentDisplayIngredientsBinding
import com.example.ratatouille.factory.DisplayIngredientViewModelFactory
import com.example.ratatouille.internetServices.API.MealRetrofitInstance
import com.example.ratatouille.mealView.IngredientsAdapter
import com.example.ratatouille.mealView.MealClickListener

class DisplayIngredients : Fragment(), MealClickListener {
    lateinit var binding: FragmentDisplayIngredientsBinding
    lateinit var viewModel: DisplayIngredientViewModel
    lateinit var adapter: IngredientsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentDisplayIngredientsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
        viewModel.getData()
    }
    private fun setupViewModel() {
        val retrofit = MealRetrofitInstance.retrofitService
        val ingredientDao = FavoriteDatabase.getFavoriteDatabase(requireContext()).getIngredientDao()
        val factory = DisplayIngredientViewModelFactory(ingredientDao, retrofit)
        viewModel = ViewModelProvider(this, factory)[DisplayIngredientViewModel::class.java]
    }

    private fun setupUI() {
        adapter = IngredientsAdapter(emptyList(),null,this)
        binding.ingredientsRecyclerView.adapter = adapter
        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchIngredients(it)
                }
                return true
            }
        })
    }
    private fun observeViewModel() {
        viewModel.ingredientLiveData.observe(viewLifecycleOwner) { ingredients ->
            binding.progressBar.visibility = View.GONE
            Log.d("Ingredients", "asddas"+ingredients.toString())
            adapter.ingredientList = ingredients
            adapter.notifyDataSetChanged()
        }
    }

    override fun onIngredientClick(position: Int) {
        viewModel.selectIngredient(position)
    }
}