package com.example.ratatouille.ui.displayMeals

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratatouille.data.remote.Meal
import com.example.ratatouille.internetServices.API.MealAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class DisplayMealsViewModel(private val retrofit: MealAPI) : ViewModel() {
    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> = _meals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading:LiveData<Boolean> = _isLoading

    fun fetchMeals(searchType: String, query: String) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val mealResponse = if (searchType == "category") {
                    retrofit.getMealsByCategory(query)
                } else {
                    retrofit.getMealsByArea(query)
                }
                if(mealResponse.isSuccessful){
                    Log.i("displayMeals", "Meals fetched successfully")
                    _meals.postValue(mealResponse.body()?.meals)
                    _isLoading.postValue(false)
                }
                else{
                    Log.w("fetch", "Error fetching meals: ${mealResponse.message()}")
                }
            } catch (e: IOException) {
                Log.e("exception", "Error fetching meals: ${e.message}")
            }
        }
    }
}