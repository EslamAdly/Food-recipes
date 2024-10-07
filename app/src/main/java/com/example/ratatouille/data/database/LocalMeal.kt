package com.example.ratatouille.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
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
){
    constructor (): this("","","","","","","",emptyList(),false)
}
