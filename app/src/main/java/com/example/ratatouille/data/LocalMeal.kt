package com.example.ratatouille.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class LocalMeal(
    @PrimaryKey(autoGenerate = false)
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String,
    val strArea: String,
    val strCategory: String,
    val strYoutube: String,
    val strInstructions: String,
    val strMeasureList:List<String>,
    var isFavorite: Boolean,
)
