package com.example.ratatouille.dataBase.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ratatouille.data.relations.MealIngredientCrossRef

@Dao
interface CrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealIngredientCrossRef(crossRef: MealIngredientCrossRef): Long

    @Delete
    suspend fun deleteMealIngredientCrossRef(crossRef: MealIngredientCrossRef): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(crossRefs: List<MealIngredientCrossRef>)

    @Query("SELECT * FROM MealIngredientCrossRef")
    suspend fun getAllCrossRefs(): List<MealIngredientCrossRef>

    @Query("DELETE FROM MealIngredientCrossRef")
    suspend fun clearCrossRefs()
}