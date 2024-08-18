package com.example.ratatouille.favoriteMeals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratatouille.data.LocalMeal
import com.example.ratatouille.dataBase.dao.MealDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteMealsViewModel(private val dao: MealDao) : ViewModel() {
    //private val _meals = MutableLiveData<List<LocalMeal>>()
    val meals: LiveData<List<LocalMeal>>

    init {
        meals=dao.getAllMeals()
    }

//    fun getFavoriteMeals() : LiveData<List<LocalMeal>> {
//        viewModelScope.launch(Dispatchers.IO) {
//            val mealsData = dao.getAllMeals()
//            withContext(Dispatchers.Main) {
//                meals.value=mealsData
//            }
//        }
//    }
}