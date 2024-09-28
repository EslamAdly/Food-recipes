package com.example.ratatouille.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ratatouille.ui.displayMeals.DisplayMealsViewModel
import com.example.ratatouille.internetServices.API.MealAPI
import com.example.ratatouille.ui.search.SearchViewModel

class RetrofitViewModelFactory(private val retrofit: MealAPI) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(retrofit) as T
        }
        if (modelClass.isAssignableFrom(DisplayMealsViewModel::class.java)) {
            return DisplayMealsViewModel(retrofit) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}