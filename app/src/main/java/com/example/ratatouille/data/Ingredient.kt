package com.example.ratatouille.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = false) val strIngredient: String,
    val strIngredientThump: String,
    var isSelected:Boolean,
)
