package com.example.ratatouille.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class MealDB(
    @PrimaryKey
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strArea: String,
    val strCategory: String,
    val strYoutube: String,
//    val ingredients: List<Ingredient>,
)
