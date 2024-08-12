package com.example.ratatouille.mealView

import android.util.Log
import com.example.ratatouille.data.Ingredient
import com.example.ratatouille.data.LocalMeal
import com.example.ratatouille.data.Meal
import com.example.ratatouille.dataBase.MealDao
import com.example.ratatouille.internetServices.MealRetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MealViewPresenter(private val view: MealView, private val mealDao: MealDao) {
    lateinit var localMeal: LocalMeal
    suspend fun getData(mealID: String) {
        val mealResponse = MealRetrofitInstance.retrofitService.getMaelById(mealID).body()
        val meal = mealResponse?.meals?.firstOrNull()
        if (meal != null) {
            localMeal = fetchRemoteMealData(meal)
            withContext(Dispatchers.Main) {
                view.showData(localMeal)
            }
        } else {
            withContext(Dispatchers.Main) {
                view.showMessage("Meal not found")
            }
        }
    }

    suspend fun changeFavorite() {
        if (localMeal.isFavorite) {
            localMeal.isFavorite = !localMeal.isFavorite
            withContext(Dispatchers.IO) {
                removeFromFavorite()
            }
            withContext(Dispatchers.Main){
                view.removeFromFavorite()
            }
        } else {
            localMeal.isFavorite = !localMeal.isFavorite
            withContext(Dispatchers.IO) {
                addToFavorite()
            }
            withContext(Dispatchers.Main){
                view.addToFavorite()
            }
        }
    }

    private fun removeFromFavorite() {
        Log.d("tt", localMeal.toString())
    }

     fun addToFavorite() {
        Log.d("tt", localMeal.toString())

    }

    private fun fetchRemoteMealData(meal: Meal): LocalMeal {
        return LocalMeal(
            meal.idMeal,
            meal.strMeal,
            meal.strMealThumb,
            meal.strArea,
            meal.strCategory,
            meal.strYoutube,
            meal.strInstructions,
            false,
            fetchIngredients(meal)
        )
    }

    private fun fetchIngredients(meal: Meal): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()
        for (i in 1..20) {
            val ingredientName =
                meal.javaClass.getMethod("getStrIngredient$i").invoke(meal) as? String
            val ingredientMeasure =
                meal.javaClass.getMethod("getStrMeasure$i").invoke(meal) as? String
            if (!ingredientName.isNullOrBlank() && !ingredientMeasure.isNullOrBlank()) {
                val ingredientThumbUrl =
                    "https://www.themealdb.com/images/ingredients/${ingredientName}-Small.png"
                ingredients.add(
                    Ingredient(
                        ingredientName,
                        ingredientThumbUrl,
                        ingredientMeasure,
                        false
                    )
                )
            }
        }
        return ingredients
    }

    fun selectIngredient(position: Int) {
        localMeal.ingredients[position].isSelected = !localMeal.ingredients[position].isSelected
        Log.d("Ingredient", "Selected ingredient: ${localMeal.ingredients[position].strIngredient}")
    }
}
