package com.example.ratatouille.mealView

import com.example.ratatouille.data.Ingredient
import com.example.ratatouille.data.LocalMeal
import com.example.ratatouille.data.Meal
import com.example.ratatouille.data.MealDB

interface MealView {
    fun showData(meal:LocalMeal)
    fun addToFavorite()
    fun removeFromFavorite()
    fun showMessage(message:String)

}