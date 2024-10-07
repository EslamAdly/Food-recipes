package com.example.ratatouille.dataBase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ratatouille.data.database.Ingredient

@Dao
interface IngredientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(ingredients: List<Ingredient>)

    @Query("SELECT * FROM ingredient")
    suspend fun getAllIngredients(): List<Ingredient>

    @Query("SELECT * FROM ingredient WHERE strIngredient = :ingredientName")
    suspend fun getIngredientByName(ingredientName: String): Ingredient?

    @Query("SELECT * FROM ingredient WHERE isSelected = 1")
    suspend fun getSelectedIngredients(): List<Ingredient>

    @Delete
    suspend fun deleteIngredient(ingredient: Ingredient): Int

    @Query("DELETE FROM Ingredient")
    suspend fun clearIngredients()
}