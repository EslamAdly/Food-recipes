package com.example.ratatouille.data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.ratatouille.data.Ingredient
import com.example.ratatouille.data.LocalMeal

data class MealWithIngredient(
    @Embedded val meal: LocalMeal,
    @Relation(
        parentColumn = "idMeal",
        entityColumn = "strIngredient",
        associateBy = Junction(MealIngredientCrossRef::class)
    )
    val ingredients: List<Ingredient>
)
