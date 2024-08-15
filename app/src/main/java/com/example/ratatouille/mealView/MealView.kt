package com.example.ratatouille.mealView

import com.example.ratatouille.data.Ingredient
import com.example.ratatouille.data.LocalMeal
interface MealView {
    fun showData(meal:LocalMeal, ingredients: List<Ingredient>)
    fun addToFavorite()
    fun removeFromFavorite()
    fun showMessage(message:String)

}