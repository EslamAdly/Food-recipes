package com.example.ratatouille.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ratatouille.dataBase.dao.CrossRefDao
import com.example.ratatouille.dataBase.dao.IngredientDao
import com.example.ratatouille.dataBase.dao.MealDao
import com.example.ratatouille.internetServices.MealAPI
import com.example.ratatouille.mealView.MealViewModel

class MealViewModelFactory(
    private val mealDao: MealDao,
    private val ingredientDao: IngredientDao,
    private val crossRefDao: CrossRefDao,
    private val retrofit: MealAPI
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MealViewModel::class.java)){
            return MealViewModel(mealDao,ingredientDao,crossRefDao,retrofit) as T
        }else{
            throw IllegalArgumentException()
        }
    }

}