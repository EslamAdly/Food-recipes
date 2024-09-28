package com.example.ratatouille.ui.homePage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratatouille.data.remote.Category
import com.example.ratatouille.data.remote.DetailedMeal
import com.example.ratatouille.internetServices.API.MealRetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _randomMeal = MutableLiveData<DetailedMeal?>()
    val randomMeal: LiveData<DetailedMeal?>  = _randomMeal

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchRandomMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = MealRetrofitInstance.retrofitService.getRandomMeal()
                if (response.isSuccessful) {
                    _randomMeal.postValue(response.body()?.meals?.firstOrNull())
                } else {
                    _errorMessage.postValue("Failed to fetch random meal: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error fetching random meal: ${e.message}")
            }
        }
    }

    fun fetchCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = MealRetrofitInstance.retrofitService.getCategories()
                if (response.isSuccessful) {
                    _categories.postValue(response.body()?.categories ?: emptyList())
                } else {
                    _errorMessage.postValue("Failed to fetch categories: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error fetching categories: ${e.message}")
            }
        }
    }
}