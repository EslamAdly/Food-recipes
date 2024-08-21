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
import com.bumptech.glide.Glide
import com.example.ratatouille.R
import com.example.ratatouille.data.DetailedMeal
import com.example.ratatouille.databinding.FragmentHomeBinding
import com.example.ratatouille.internetServices.MealRetrofitInstance
import com.example.ratatouille.mealView.MealActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var randomMeal: DetailedMeal? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchRandomMeal()
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
    }

    private fun fetchRandomMeal() {
        try {
            lifecycleScope.launch(Dispatchers.IO) {

                val randomMealResponse = MealRetrofitInstance.retrofitService.getRandomMeal()
                if(randomMealResponse.isSuccessful){
                    Log.d("RandomMeal", "Random meal fetched successfully")

                }
                randomMeal = randomMealResponse.body()?.meals?.firstOrNull()

                withContext(Dispatchers.Main) {
                    randomMeal?.let {
                        withContext(Dispatchers.Main) {
                            Glide.with(this@HomeFragment).load(randomMeal?.strMealThumb)
                                .into(binding.randomMealImg)
                        }
                    }?:run{
                        Log.e("RandomMeal", "Random meal is null")
                    }

                }
            }
        } catch (e: Exception) {
            Log.e("IOException", "fetchRandomMeal:$e}")
        }
    }

}