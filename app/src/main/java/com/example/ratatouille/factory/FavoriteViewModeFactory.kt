package com.example.ratatouille.factory

import android.view.Display.Mode
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ratatouille.dataBase.MealDao
import com.example.ratatouille.favoriteMeals.FavoriteMealsViewModel

class FavoriteViewModeFactory (private val dao:MealDao):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FavoriteMealsViewModel::class.java)){
            return FavoriteMealsViewModel(dao) as T
        }else{
            throw IllegalArgumentException()
        }
    }
}