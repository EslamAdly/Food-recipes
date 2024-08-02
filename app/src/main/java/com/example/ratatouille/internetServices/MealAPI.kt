package com.example.ratatouille.internetServices

import com.example.ratatouille.data.MealResponse
import retrofit2.http.GET

interface MealAPI {
    @GET("random.php")
    suspend fun getRandomMeal():MealResponse
}