package com.example.ratatouille.homePage

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.ratatouille.internetServices.MealRetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//class HomeViewModel: ViewModel() {
//    private  var randomMealLiveData= MutableLiveData<Meal>()
//    private fun getRandomMeal() {
//        try {
//            lifecycleScope.launch(Dispatchers.IO) {
//
//                val randomMealResponse = MealRetrofitInstance.retrofitService.getRandomMeal().body()
//                randomMeal = randomMealResponse?.meals?.firstOrNull()
//
//                withContext(Dispatchers.Main) {
//                    randomMeal?.let {
//                        withContext(Dispatchers.Main) {
//                            Glide.with(this@HomeFragment).load(randomMeal?.strMealThumb)
//                                .into(binding.randomMealImg)
//                        }
//                    }?:run{
//                        Log.e("RandomMeal", "Random meal is null")
//                    }
//
//                }
//            }
//        } catch (e: Exception) {
//            Log.e("IOException", "fetchRandomMeal:$e}")
//        }
//    }
//}