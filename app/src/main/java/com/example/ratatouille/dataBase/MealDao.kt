package com.example.ratatouille.dataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.ratatouille.data.Meal
import androidx.room.Query
import com.example.ratatouille.data.MealDB

@Dao
interface MealDao {
    @Query("SELECT * FROM favorite_table")
    suspend fun getAllMeals(): List<MealDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: MealDB)

    @Delete
    suspend fun deleteMeal(meal: MealDB)
}