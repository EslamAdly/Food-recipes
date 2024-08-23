package com.example.ratatouille.dataBase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.ratatouille.data.database.LocalMeal
import com.example.ratatouille.data.relations.MealWithIngredient

@Dao
interface MealDao {
    @Upsert
    suspend fun insertMeal(meal: LocalMeal):Long

    @Transaction
    @Query("SELECT * FROM localmeal WHERE idMeal = :mealId")
    suspend fun getMealsWithIngredients(mealId: String): List<MealWithIngredient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(localMeals: List<LocalMeal>)

    @Query("SELECT * FROM localmeal")
     fun getAllMeals(): LiveData<List<LocalMeal>>

    @Query("SELECT * FROM localmeal")
    fun getAllMealsData(): List<LocalMeal>

    @Query("SELECT * FROM localmeal WHERE isFavorite = 1")
     fun getFavoriteMeals(): LiveData<List<LocalMeal>>

    @Query("SELECT * FROM localmeal WHERE idMeal = :mealId")
    suspend fun getMealById(mealId: String): LocalMeal?

    @Query("UPDATE LocalMeal SET isFavorite = :isFavorite WHERE idMeal = :mealId")
    suspend fun updateFavoriteStatus(mealId: String, isFavorite: Boolean):Int

    @Delete
    suspend fun deleteMeal(meal: LocalMeal):Int
    @Query("DELETE FROM LocalMeal")
    suspend fun clearMeals()
}