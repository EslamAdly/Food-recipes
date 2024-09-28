package com.example.ratatouille.ui.favoriteMeals

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ratatouille.data.database.LocalMeal
import com.example.ratatouille.dataBase.dao.MealDao

class FavoriteMealsViewModel(private val dao: MealDao) : ViewModel() {
    //private val _meals = MutableLiveData<List<LocalMeal>>()
    val meals: LiveData<List<LocalMeal>>

    init {
        meals=dao.getFavoriteMeals()
    }


}