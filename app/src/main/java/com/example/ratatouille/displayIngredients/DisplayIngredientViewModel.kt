package com.example.ratatouille.displayIngredients

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratatouille.data.database.Ingredient
import com.example.ratatouille.data.remote.MealX
import com.example.ratatouille.dataBase.dao.IngredientDao
import com.example.ratatouille.internetServices.API.MealAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class DisplayIngredientViewModel(
    private val ingredientDao: IngredientDao,
    private val retrofit: MealAPI
) : ViewModel() {
    val _ingredientsLiveData = MutableLiveData<List<Ingredient>>()
    val ingredientLiveData: LiveData<List<Ingredient>> = _ingredientsLiveData
    private var allIngredients: List<Ingredient> = emptyList()
    fun getData() {
        viewModelScope.launch {
            try {
                val apiIngredients = withContext(Dispatchers.IO) { fetchApiIngredients() }
                val dbIngredients =
                    withContext(Dispatchers.IO) { ingredientDao.getAllIngredients() }
                allIngredients = mapIngredientsWithSelectionStatus(apiIngredients, dbIngredients)
                _ingredientsLiveData.postValue(allIngredients)
            } catch (e: IOException) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    private suspend fun fetchApiIngredients(): List<MealX> {
        val response = retrofit.getIngredients()
        return if (response.isSuccessful) {
            Log.d("Ingredient", response.body()?.meals.toString())
            response.body()?.meals ?: emptyList()
        } else {
            Log.e("IngredientViewModel", "API response failed: ${response.message()}")
            emptyList()
        }
    }

    private fun mapIngredientsWithSelectionStatus(
        apiIngredients: List<MealX>,
        dbIngredient: List<Ingredient>
    ): List<Ingredient> {
        val dbIngredientMap = dbIngredient.associateBy { it.strIngredient }
        return apiIngredients.map { ingredient ->
            Ingredient(
                strIngredient = ingredient.strIngredient,
                strIngredientThump = "https://www.themealdb.com/images/ingredients/${ingredient.strIngredient}-Small.png",
                isSelected = dbIngredientMap[ingredient.strIngredient]?.isSelected ?: false
            )
        }
    }

    fun searchIngredients(query: String) {
        val filteredIngredients = allIngredients.filter {
            it.strIngredient.contains(query, ignoreCase = true)
        }
        _ingredientsLiveData.postValue(filteredIngredients)
    }
    fun selectIngredient(position:Int) {
        allIngredients[position].isSelected = !allIngredients[position].isSelected
        viewModelScope.launch(Dispatchers.IO) {

            ingredientDao.insertIngredient(allIngredients[position])
        }
    }
}