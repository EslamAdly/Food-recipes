package com.example.ratatouille.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratatouille.data.remote.Meal
import com.example.ratatouille.internetServices.API.MealAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(private val retrofit: MealAPI) : ViewModel() {
    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>> = _meals

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun search(query: String, searchType: SearchType) {
        Log.d("SearchViewModel", "Searching for $searchType with query: $query")
        viewModelScope.launch(Dispatchers.IO) {
            when (searchType) {
                SearchType.NAME -> searchByMealName(query)
                SearchType.INGREDIENT -> searchByIngredient(query)
                SearchType.AREA -> searchByArea(query)
                SearchType.CATEGORY -> searchByCategory(query)
            }
        }
    }

    private suspend fun searchByMealName(mealName: String) {
        Log.d("SearchViewModel", "Searching by meal name: $mealName")
        val mealResponse = retrofit.getMealsByName(mealName)
        if (mealResponse.isSuccessful) {
            val meals = mealResponse.body()?.meals ?: listOf()
            withContext(Dispatchers.Main) {
                    _meals.postValue(meals)
                if (meals.isEmpty()){
                    Log.d("SearchViewModel", "No meals found")
                    _message.postValue("No meals found")

                }
            }
        }
    }

    private suspend fun searchByIngredient(ingredient: String) {
        Log.d("SearchViewModel", "Searching by ingredient: $ingredient")
        val mealResponse = retrofit.getMealsByIngredient(ingredient)
        if (mealResponse.isSuccessful) {
            val meals = mealResponse.body()?.meals ?: listOf()
            withContext(Dispatchers.Main) {

                    _meals.postValue(meals)

                if (meals.isEmpty()){
                    Log.d("SearchViewModel", "No meals found")
                    _message.postValue("No meals found")
                }
            }
        }
    }

    private suspend fun searchByArea(area: String) {
        Log.d("SearchViewModel", "Searching by area: $area")
        val mealResponse = retrofit.getMealsByArea(area)
        if (mealResponse.isSuccessful) {
            val meals = mealResponse.body()?.meals ?: listOf()
            withContext(Dispatchers.Main) {

                    _meals.postValue(meals)

                if (meals.isEmpty()){
                    Log.d("SearchViewModel", "No meals found")
                    _message.postValue("No meals found")

                }
            }
        }
    }

    private suspend fun searchByCategory(category: String) {
        Log.d("SearchViewModel", "Searching by category: $category")

        val mealResponse = retrofit.getMealsByCategory(category)
        if (mealResponse.isSuccessful) {
            val meals = mealResponse.body()?.meals ?: listOf()
            withContext(Dispatchers.Main) {

                    _meals.postValue(meals)

                if (meals.isEmpty()){
                    Log.d("SearchViewModel", "No meals found")
                    _message.postValue("No meals found")

                }
            }
        }
    }



}