package com.example.ratatouille.ui.mealView

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ratatouille.data.DayOfWeek
import com.example.ratatouille.data.database.Ingredient
import com.example.ratatouille.data.database.LocalMeal
import com.example.ratatouille.data.database.MealsPlan
import com.example.ratatouille.data.relations.MealIngredientCrossRef
import com.example.ratatouille.data.remote.DetailedMeal
import com.example.ratatouille.dataBase.dao.CrossRefDao
import com.example.ratatouille.dataBase.dao.IngredientDao
import com.example.ratatouille.dataBase.dao.MealDao
import com.example.ratatouille.dataBase.dao.MealsPlanDao
import com.example.ratatouille.internetServices.API.MealAPI
import com.example.ratatouille.internetServices.firebase.FirebaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MealViewModel(
    private val mealDao: MealDao,
    private val ingredientDao: IngredientDao,
    private val crossRefDao: CrossRefDao,
    private val mealsPlanDao: MealsPlanDao,
    private val retrofit: MealAPI,
    private val firebaseHelper: FirebaseHelper
) : ViewModel() {
    private val _mealData = MutableLiveData<Pair<LocalMeal, List<Ingredient>>>()
    val mealData: LiveData<Pair<LocalMeal, List<Ingredient>>> = _mealData

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private var isDataFetched: Boolean = false

    fun getData(mealId: String, source: String) {
        if (isDataFetched) return
        viewModelScope.launch(Dispatchers.IO) {
            val dataResult = when (source) {
                "retrofit" -> fetchMealFromRetrofit(mealId)
                "database" -> fetchMealFromDatabase(mealId)
                else -> null
            }
            dataResult?.let { updateUI(it) } ?: postError("Meal not found")
        }
    }

    //get data methods
    private suspend fun fetchMealFromRetrofit(mealId: String): Pair<LocalMeal, List<Ingredient>>? {
        val mealResponse = retrofit.getMealById(mealId)
        return if (mealResponse.isSuccessful) {
            mealResponse.body()?.meals?.firstOrNull()?.let {
                val localMeal = remoteMealToLocalMeal(it)
                val ingredientsList = extractIngredientsFromMeal(it)
                localMeal.isFavorite = isFavorite(mealId)
                checkIngredientsInDatabase(ingredientsList);
                Pair(localMeal, ingredientsList)
            }
        } else {
            null
        }
    }

    private suspend fun fetchMealFromDatabase(mealId: String): Pair<LocalMeal, List<Ingredient>>? {
        val mealWithIngredients = mealDao.getMealsWithIngredients(mealId).firstOrNull()
        return mealWithIngredients?.let {
            Pair(it.meal, it.ingredients)
        }
    }

    private fun updateUI(mealData: Pair<LocalMeal, List<Ingredient>>) {
        viewModelScope.launch(Dispatchers.Main) {
            _mealData.value = mealData
            _isFavorite.value = mealData.first.isFavorite
            isDataFetched = true
        }
    }

    private fun postError(message: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _message.value = message
        }
    }


    fun toggleFavorite() {
        mealData.value?.first?.let { localMeal ->
            viewModelScope.launch(Dispatchers.IO) {
                localMeal.isFavorite = !_isFavorite.value!!
                mealDao.updateFavoriteStatus(mealId = localMeal.idMeal, isFavorite = localMeal.isFavorite)
                firebaseHelper.editFavorite(mealId = localMeal.idMeal, isFavorite = localMeal.isFavorite)
                if (localMeal.isFavorite) addToDatabase() else removeFromFavorites()
                updateFavoriteState(localMeal.isFavorite)
            }
        } ?: run {
            // Handle the case where mealData is null
            postError("Meal data is unavailable.")
        }
    }

    private fun updateFavoriteState(isFavorite: Boolean) {
        viewModelScope.launch(Dispatchers.Main) {
            _isFavorite.value = isFavorite
            _message.value =
                if (isFavorite) "Meal added to favorites" else "Meal removed from favorites"
        }
    }

    private suspend fun addToDatabase() {
        _mealData.value?.let { (localMeal, ingredientsList) ->
            if(!isInDatabase(localMeal.idMeal)) {
                mealDao.insertMeal(localMeal)
                firebaseHelper.addToFavorites(localMeal)
                firebaseHelper.addIngredientList(ingredientsList)
                ingredientsList.forEach { addIngredientToDatabase(it, localMeal.idMeal) }
            }
        } ?: run {
            // Handle the case where mealData is null
            postError("Meal data is unavailable.")
        }
    }

    private suspend fun addIngredientToDatabase(ingredient: Ingredient, mealId: String) {
        val crossRef = MealIngredientCrossRef(mealId, ingredient.strIngredient)
        ingredientDao.insertIngredient(ingredient)
        crossRefDao.insertMealIngredientCrossRef(crossRef)
        firebaseHelper.addMealIngredientCrossRef(crossRef)
        //firebaseHelper.addIngredient(ingredient)
    }

    private suspend fun removeFromFavorites() {

        _mealData.value?.let { (localMeal, ingredientsList) ->
            if (!isInPlan(localMeal.idMeal)) {
                mealDao.deleteMeal(localMeal)
                firebaseHelper.removeFromFavorites(localMeal.idMeal)
                ingredientsList.forEach { removeIngredientFromDatabase(it, localMeal.idMeal) }
            }
        } ?: run {
            // Handle the case where mealData is null
            postError("Meal data is unavailable.")
        }
    }

    private suspend fun removeIngredientFromDatabase(ingredient: Ingredient, mealId: String) {
        val crossRef = MealIngredientCrossRef(mealId, ingredient.strIngredient)
        crossRefDao.deleteMealIngredientCrossRef(crossRef)
        firebaseHelper.removeMealIngredientCrossRef(crossRef)
        if (!ingredient.isSelected) {
            ingredientDao.deleteIngredient(ingredient)
            firebaseHelper.removeIngredient(ingredient)
        }
    }

    //plan meals methods
    fun addMealToPlan(dayOfWeek: DayOfWeek) {
        viewModelScope.launch(Dispatchers.IO) {
            _mealData.value?.first?.let { localMeal ->
                addToDatabase()
                val mealPlan = MealsPlan(
                    dayOfWeek,
                    localMeal.idMeal,
                    localMeal.strMeal,
                    localMeal.strMealThumb
                )
                mealsPlanDao.insertMealsPlan(mealPlan)
                firebaseHelper.addMealsPlan(mealPlan)
                withContext(Dispatchers.Main) {
                    _message.postValue("Meal added to $dayOfWeek plan")
                }
            } ?: run {
                // Handle the case where mealData is null
                postError("Meal data is unavailable.")
            }
        }
    }

    fun selectIngredient(position: Int) {
        _mealData.value?.second?.let { ingredientsList ->
            ingredientsList[position].isSelected = !ingredientsList[position].isSelected
            viewModelScope.launch(Dispatchers.IO) {
                ingredientDao.insertIngredient(ingredientsList[position])
                firebaseHelper.addIngredient(ingredientsList[position])
            }
        } ?: run {
            // Handle the case where mealData is null
            postError("Meal data is unavailable.")
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
                detailedMeal.javaClass.getMethod("getStrIngredient$i")
                    .invoke(detailedMeal) as? String
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


    //get meal state methods
    private suspend fun isFavorite(mealId: String): Boolean {
        return mealDao.getMealById(mealId)?.isFavorite ?: false
    }

    private suspend fun isInDatabase(mealId: String): Boolean {
        return mealDao.getMealById(mealId) != null
    }
    private suspend fun checkIngredientsInDatabase(ingredientList: List<Ingredient>): List<Ingredient> {
        ingredientList.forEach {
            val ingredient = ingredientDao.getIngredientByName(it.strIngredient)
            it.isSelected = ingredient != null && ingredient.isSelected
        }
        return ingredientList
    }

    private suspend fun isInPlan(mealId: String): Boolean {
        val mealsPlan = mealsPlanDao.getMealsPlanByMealId(mealId)
        return mealsPlan.isNotEmpty()
    }
}