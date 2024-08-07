package com.example.ratatouille.internetServices

import com.example.ratatouille.data.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealAPI {
    @GET("random.php")
    suspend fun getRandomMeal():MealResponse
    @GET("lookup.php?i")
    suspend fun getMaelById(@Query("i") mealId:String):MealResponse
}