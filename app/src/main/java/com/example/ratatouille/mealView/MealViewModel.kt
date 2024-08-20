package com.example.ratatouille.mealView

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratatouille.data.Ingredient
import com.example.ratatouille.data.LocalMeal
import com.example.ratatouille.data.DetailedMeal
import com.example.ratatouille.data.relations.MealIngredientCrossRef
import com.example.ratatouille.dataBase.dao.CrossRefDao
import com.example.ratatouille.dataBase.dao.IngredientDao
import com.example.ratatouille.dataBase.dao.MealDao
import com.example.ratatouille.internetServices.MealAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MealViewModel(
    private val mealDao: MealDao,
    private val ingredientDao: IngredientDao,
    private val crossRefDao: CrossRefDao,
    private val retrofit: MealAPI
) : ViewModel() {

    private val _mealData = MutableLiveData<Pair<LocalMeal, List<Ingredient>>>()
    val mealData: LiveData<Pair<LocalMeal, List<Ingredient>>> = _mealData

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private lateinit var localMeal: LocalMeal
    private lateinit var ingredientsList: List<Ingredient>

    private var isDataFetched:Boolean=false
    fun getData(mealId: String, source: String) {
        if (isDataFetched)return
        viewModelScope.launch(Dispatchers.IO) {
            if (source == "retrofit") {
                fetchMaelFromRetrofit(mealId)
            } else if (source == "database") {
                fetchMaelFromDatabase(mealId)
            }
        }
    }

    //get data methods
    private suspend fun fetchMaelFromRetrofit(mealId: String) {
        val mealResponse = retrofit.getMaelById(mealId)
        if (mealResponse.isSuccessful) {
            val meal = mealResponse.body()?.meals?.firstOrNull()
            if (meal != null) {
                localMeal = remoteMealToLocalMeal(meal)
                ingredientsList = extractIngredientsFromMeal(meal)
                val isFavorite = isFavorite(mealId)
                withContext(Dispatchers.Main) {
                    checkIngredientsInDatabase(mealId)
                    _mealData.value = Pair(localMeal, ingredientsList)
                    _isFavorite.value = isFavorite
                }

                isDataFetched=true
            } else {
                withContext(Dispatchers.Main) {
                    _message.value = "Meal not found"
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                _message.value = "Meal not found"
                Log.d("Error", "HTTP error: ${mealResponse.code()}")
            }
        }
    }

    private suspend fun fetchMaelFromDatabase(mealId: String) {
        val mealWithIngredient = mealDao.getMealsWithIngredients(mealId)
        localMeal = mealWithIngredient.first().meal
        ingredientsList = mealWithIngredient.first().ingredients
        withContext(Dispatchers.Main) {
//            _meal.value=mealWithIngredient.first().meal
//            _ingredients.value=mealWithIngredient.first().ingredients
            _mealData.value =
                Pair(mealWithIngredient.first().meal, mealWithIngredient.first().ingredients)
            _isFavorite.value = true
        }
        isDataFetched=true

    }

    private suspend fun isFavorite(mealId: String): Boolean {
        return mealDao.getMealById(mealId) != null
    }

    //favorite methods
    fun changeFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_isFavorite.value == true) {
                localMeal.isFavorite = false
                removeFromFavorite()
                withContext(Dispatchers.Main) {
                    _isFavorite.value = false
                }
            } else {
                localMeal.isFavorite = true
                addToFavorite()
                withContext(Dispatchers.Main) {
                    _isFavorite.value = true
                }
            }
        }
    }

    private suspend fun addToFavorite() {
        val result=mealDao.insertMeal(localMeal)
        ingredientsList.forEach {
            ingredientDao.insertIngredient(it)
            crossRefDao.insertMealIngredientCrossRef(
                MealIngredientCrossRef(
                    localMeal.idMeal,
                    it.strIngredient
                )
            )
        }
        if(result>0){
            _message.postValue("Meal added to favorites")
        }
        else{
            _message.postValue("Error adding meal to favorites")
        }
    }

    private suspend fun removeFromFavorite() {
        val result=mealDao.deleteMeal(localMeal)
        ingredientsList.forEach {
            crossRefDao.deleteMealIngredientCrossRef(MealIngredientCrossRef(localMeal.idMeal, it.strIngredient))
            if(!it.isSelected){
                ingredientDao.deleteIngredient(it)
            }
        }
        if(result>0){
            _message.postValue("Meal removed from favorites")
        }else{
            _message.postValue("Error removing meal from favorites")
        }
    }

    private suspend fun checkIngredientsInDatabase(ingredientName: String) {
        ingredientsList.forEach {
            val ingredient = ingredientDao.getIngredientByName(it.strIngredient)
            it.isSelected = ingredient != null && ingredient.isSelected
        }

    }

    //convert remote meal to local meal methods
    private fun remoteMealToLocalMeal(detailedMeal: DetailedMeal): LocalMeal {
        return LocalMeal(
            detailedMeal.idMeal,
            detailedMeal.strMeal,
            detailedMeal.strMealThumb,
            detailedMeal.strArea,
            detailedMeal.strCategory,
            detailedMeal.strYoutube,
            detailedMeal.strInstructions,
            extractMeasureListFromMeal(detailedMeal),
            false,
        )
    }

    private fun extractIngredientsFromMeal(detailedMeal: DetailedMeal): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()
        for (i in 1..20) {
            val ingredientName =
                detailedMeal.javaClass.getMethod("getStrIngredient$i").invoke(detailedMeal) as? String

            if (!ingredientName.isNullOrBlank()) {
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

    private fun extractMeasureListFromMeal(detailedMeal: DetailedMeal): List<String> {
        val measureList = mutableListOf<String>()
        for (i in 1..20) {

            val ingredientMeasure =
                detailedMeal.javaClass.getMethod("getStrMeasure$i").invoke(detailedMeal) as? String
            if (!ingredientMeasure.isNullOrBlank()) {
                measureList.add(ingredientMeasure)
            }
        }
        return measureList
    }


    fun selectIngredient(position: Int) {
        ingredientsList[position].isSelected = !ingredientsList[position].isSelected
        viewModelScope.launch(Dispatchers.IO) {

            ingredientDao.insertIngredient(ingredientsList[position])
        }
    }
}