package com.example.ratatouille.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ratatouille.dataBase.dao.IngredientDao
import com.example.ratatouille.displayIngredients.DisplayIngredientViewModel
import com.example.ratatouille.internetServices.MealAPI

class DisplayIngredientViewModelFactory(
    private val ingredientDao: IngredientDao,
    private val retrofit: MealAPI
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DisplayIngredientViewModel::class.java)) {
            return DisplayIngredientViewModel(ingredientDao, retrofit) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")

        }
    }
}