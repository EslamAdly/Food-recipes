package com.example.ratatouille.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ratatouille.dataBase.dao.MealsPlanDao
import com.example.ratatouille.ui.planMeals.PlanMealViewModel

class PlanMealsViewModelFactory(private val dao: MealsPlanDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlanMealViewModel::class.java)) {
                return PlanMealViewModel(dao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")

        }
}