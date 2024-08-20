package com.example.ratatouille.favoriteMeals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ratatouille.dataBase.FavoriteDatabase
import com.example.ratatouille.databinding.FragmentFavoriteBinding
import com.example.ratatouille.factory.FavoriteViewModeFactory

class FavoriteFragment : Fragment() {

    private lateinit var adapter: FavoriteMealsAdapter
    private lateinit var viewModel: FavoriteMealsViewModel
    private lateinit var binding: FragmentFavoriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViewModel()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()

        viewModel.meals.observe(viewLifecycleOwner) {
            binding.favoriteProgressBar.visibility = View.GONE
            adapter.meals = it
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupUI() {
        adapter = FavoriteMealsAdapter(listOf())
        binding.favoriteRecyclerView.adapter = adapter
        binding.favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    private fun setupViewModel(){
        val dao = FavoriteDatabase.getFavoriteDatabase(requireContext()).getMealDao()
        val factory= FavoriteViewModeFactory(dao)
        viewModel = ViewModelProvider(this,factory)[FavoriteMealsViewModel::class.java]

    }
}