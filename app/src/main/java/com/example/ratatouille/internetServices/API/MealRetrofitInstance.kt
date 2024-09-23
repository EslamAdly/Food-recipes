package com.example.ratatouille.internetServices.API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MealRetrofitInstance {
    private val retrofit=Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitService: MealAPI = retrofit.create(MealAPI::class.java)

    fun getIngredientImage(ingredientName:String):String{
        return "https://www.themealdb.com/images/ingredients/$ingredientName-Small.png"
    }
}