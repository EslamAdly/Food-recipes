package com.example.ratatouille.data.relations

import androidx.room.Entity

@Entity(primaryKeys = ["idMeal", "strIngredient"])
data class MealIngredientCrossRef(
    val idMeal: String,
    val strIngredient: String
){
    constructor():this("","")
}
