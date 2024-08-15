package com.example.ratatouille.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.ratatouille.data.Ingredient
import com.example.ratatouille.data.LocalMeal
import com.example.ratatouille.data.relations.MealIngredientCrossRef
import com.example.ratatouille.data.relations.MealWithIngredient

@Dao
interface MealDao {
    @Upsert
    suspend fun insertMeal(meal: LocalMeal)

    @Upsert
    suspend fun insertIngredient(ingredient: Ingredient)

    @Upsert fun insertMealIngredientCrossRef(crossRef: MealIngredientCrossRef)

    @Transaction
    @Query("SELECT * FROM favorite_table WHERE idMeal = :mealId")
    suspend fun getMealsWithIngredients(mealId: String): List<MealWithIngredient>

    @Query("SELECT * FROM favorite_table")
    suspend fun getAllMeals(): List<LocalMeal>

    @Query("SELECT * FROM favorite_table WHERE idMeal = :mealId")
    suspend fun getMealById(mealId: String): LocalMeal?

    @Delete
    suspend fun deleteMeal(meal: LocalMeal)

}