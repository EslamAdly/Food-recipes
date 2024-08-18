package com.example.ratatouille.dataBase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.ratatouille.data.LocalMeal
import com.example.ratatouille.data.relations.MealIngredientCrossRef
import com.example.ratatouille.data.relations.MealWithIngredient

@Dao
interface MealDao {
    @Upsert
    suspend fun insertMeal(meal: LocalMeal):Long

    @Transaction
    @Query("SELECT * FROM favorite_table WHERE idMeal = :mealId")
    suspend fun getMealsWithIngredients(mealId: String): List<MealWithIngredient>

    @Query("SELECT * FROM favorite_table")
     fun getAllMeals(): LiveData<List<LocalMeal>>

    @Query("SELECT * FROM favorite_table WHERE idMeal = :mealId")
    suspend fun getMealById(mealId: String): LocalMeal?

    @Delete
    suspend fun deleteMeal(meal: LocalMeal):Int

}