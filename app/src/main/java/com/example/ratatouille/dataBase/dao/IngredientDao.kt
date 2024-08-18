package com.example.ratatouille.dataBase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.ratatouille.data.Ingredient
@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Query("SELECT * FROM ingredient")
    suspend fun getAllIngredients(): List<Ingredient>

    @Query("SELECT * FROM ingredient WHERE strIngredient = :ingredientName")
    suspend fun getIngredientByName(ingredientName: String): Ingredient?

    @Query("SELECT * FROM ingredient WHERE isSelected = 1")
    suspend fun getSelectedIngredients(): List<Ingredient>

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient):Int

}