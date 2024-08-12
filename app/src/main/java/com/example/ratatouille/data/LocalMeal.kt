package com.example.ratatouille.data

data class LocalMeal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strArea: String,
    val strCategory: String,
    val strYoutube: String,
    val strInstructions: String,
    var isFavorite: Boolean,
    val ingredients: List<Ingredient>,
)
