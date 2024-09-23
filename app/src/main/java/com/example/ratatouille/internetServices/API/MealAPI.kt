package com.example.ratatouille.internetServices.API

import com.example.ratatouille.data.remote.CategoryResponse
import com.example.ratatouille.data.remote.DetailedMealResponse
import com.example.ratatouille.data.remote.IngredientResponse
import com.example.ratatouille.data.remote.MealResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealAPI {
    @GET("random.php")
    suspend fun getRandomMeal(): Response<DetailedMealResponse>

    @GET("lookup.php?i")
    suspend fun getMaelById(@Query("i") mealId:String):Response<DetailedMealResponse>

    //search
    @GET("search.php?s")
    suspend fun getMealsByName(@Query("s") mealName:String):Response<MealResponse>

    @GET("filter.php?i")
    suspend fun getMealsByIngredient(@Query("i") ingredientName:String):Response<MealResponse>

    @GET("filter.php?a")
    suspend fun getMealsByArea(@Query("a") areaName:String):Response<MealResponse>

    @GET("filter.php?c")
    suspend fun getMealsByCategory(@Query("c") categoryName:String):Response<MealResponse>
    //list
    @GET("categories.php")
    suspend fun getCategories():Response<CategoryResponse>

    @GET("list.php?i=list")
    suspend fun getIngredients():Response<IngredientResponse>
//
//    @GET("list.php?a=list")
//    suspend fun getAreas():Response<CategoryResponse>
}