package com.example.ratatouille.dataBase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.ratatouille.data.DayOfWeek
import com.example.ratatouille.data.database.MealsPlan

@Dao
interface MealsPlanDao {
    @Upsert
    suspend fun insertMealsPlan(mealsPlan: MealsPlan):Long

    @Query("SELECT * FROM MealsPlan")
    suspend fun getAllMealsPlan(): List<MealsPlan>

    @Query("SELECT * FROM MealsPlan WHERE dayOfWeek = :dayOfWeek")
    fun getMealsPlanForDay(dayOfWeek: DayOfWeek): LiveData<List<MealsPlan>>

    @Query("SELECT * FROM MealsPlan WHERE mealId = :mealId")
    suspend fun getMealsPlanByMealId(mealId: String): List<MealsPlan>

    @Query("DELETE FROM MealsPlan WHERE mealId = :mealId AND dayOfWeek = :dayOfWeek")
    suspend fun deleteMealsPlanById(mealId: String, dayOfWeek: DayOfWeek)

}