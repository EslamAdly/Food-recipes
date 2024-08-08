package com.example.ratatouille.UI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.ratatouille.R
import com.example.ratatouille.data.Meal
import com.example.ratatouille.databinding.FragmentHomeBinding
import com.example.ratatouille.internetServices.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {
private lateinit var binding:FragmentHomeBinding
lateinit var randomMeal:Meal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchRandomMeal()

        binding.randomMealImg.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra("mealId",randomMeal.idMeal)
            startActivity(intent)
        }
    }
    private fun fetchRandomMeal(){
        lifecycleScope.launch(Dispatchers.IO){
            randomMeal= RetrofitInstance.retrofitService.getRandomMeal().meals[0]
            withContext(Dispatchers.Main){
                Glide.with(this@HomeFragment).load(randomMeal.strMealThumb).into(binding.randomMealImg)

            }
        }
    }

}