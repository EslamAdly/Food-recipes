package com.example.ratatouille.mealView

import android.util.Log
import com.example.ratatouille.data.Ingredient
import com.example.ratatouille.data.LocalMeal
import com.example.ratatouille.data.Meal
import com.example.ratatouille.data.relations.MealIngredientCrossRef
import com.example.ratatouille.dataBase.MealDao
import com.example.ratatouille.internetServices.MealRetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MealViewPresenter(private val view: MealView, private val mealDao: MealDao) {
    lateinit var localMeal: LocalMeal
    lateinit var ingredients: List<Ingredient>
    suspend fun getData(mealID: String) {
        val mealResponse = MealRetrofitInstance.retrofitService.getMaelById(mealID).body()
        val meal = mealResponse?.meals?.firstOrNull()
        if (meal != null) {
            localMeal = fetchRemoteMealData(meal)
            ingredients = fetchIngredients(meal)
            withContext(Dispatchers.Main) {
                view.showData(localMeal, ingredients)
            }
        } else {
            withContext(Dispatchers.Main) {
                view.showMessage("Meal not found")
            }
        }
    }

    suspend fun changeFavorite() {
        if (localMeal.isFavorite) {
            localMeal.isFavorite = false
            withContext(Dispatchers.IO) {
                removeFromFavorite()
            }
            withContext(Dispatchers.Main) {
                view.removeFromFavorite()
            }
        } else {
            localMeal.isFavorite = true
            withContext(Dispatchers.IO) {
                addToFavorite()
            }
            withContext(Dispatchers.Main) {
                view.addToFavorite()
            }
        }
    }
    suspend fun isFavorite(mealID: String): Boolean {
        var meal: LocalMeal? = null
        meal= mealDao.getMealById(localMeal.idMeal)
        if(meal!=null)return true
        return false
    }
    private fun removeFromFavorite() {
        Log.d("tt", localMeal.toString())
    }

    private suspend fun addToFavorite() {
        mealDao.insertMeal(localMeal)
        ingredients.forEach {
            mealDao.insertIngredient(it)
            mealDao.insertMealIngredientCrossRef(MealIngredientCrossRef(localMeal.idMeal, it.strIngredient))
        }
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
            fetchMeasureList(meal),
            false,
            // fetchIngredients(meal)
        )
    }

    private fun fetchIngredients(meal: Meal): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()
        for (i in 1..20) {
            val ingredientName =
                meal.javaClass.getMethod("getStrIngredient$i").invoke(meal) as? String

            if (!ingredientName.isNullOrBlank() ) {
                val ingredientThumbUrl =
                    "https://www.themealdb.com/images/ingredients/${ingredientName}-Small.png"
                ingredients.add(
                    Ingredient(
                        ingredientName,
                        ingredientThumbUrl,
                        false,
                    )
                )
            }
        }
        return ingredients
    }

    private fun fetchMeasureList(meal: Meal): List<String> {
        val measureList = mutableListOf<String>()
        for (i in 1..20) {

            val ingredientMeasure = meal.javaClass.getMethod("getStrMeasure$i").invoke(meal) as? String
            if(!ingredientMeasure.isNullOrBlank()){
                measureList.add(ingredientMeasure)
            }
        }
        return measureList
    }

    fun selectIngredient(position: Int) {
        ingredients[position].isSelected = !ingredients[position].isSelected
        Log.d("Ingredient", "Selected ingredient: ${ingredients[position].strIngredient}")
    }
}