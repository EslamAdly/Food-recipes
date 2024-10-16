package com.example.ratatouille.data.database

import androidx.room.Entity
import com.example.ratatouille.data.DayOfWeek

@Entity(primaryKeys = ["dayOfWeek","mealId"])
data class MealsPlan(
    val dayOfWeek:DayOfWeek=DayOfWeek.MONDAY,
    val mealId:String="",
    val strMeal: String="",
    val strMealThumb: String=""
){
    constructor () :this(DayOfWeek.MONDAY,"","","")
}
