package com.example.ratatouille.ui.mealView

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ratatouille.data.Ingredient
import com.example.ratatouille.data.Meal
import com.example.ratatouille.databinding.ActivityMealBinding
import com.example.ratatouille.internetServices.MealRetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MealActivity : AppCompatActivity() {

    private val TAG = "Error"
    private lateinit var binding: ActivityMealBinding
    private lateinit var adapter: IngredientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        val id = intent.getStringExtra("mealId")
        id?.let {
            fetchMeal(it)
        } ?: Log.d(TAG, "Meal ID is null")
    }

    private fun setupUI() {
        adapter = IngredientsAdapter(emptyList())
        binding.mealIngredients.layoutManager = LinearLayoutManager(this)
        binding.mealIngredients.adapter = adapter
    }

    private fun fetchMeal(mealId: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val meal = MealRetrofitInstance.retrofitService.getMaelById(mealId).meals.firstOrNull()
                if (meal != null) {
                    val ingredients = fetchIngredients(meal)
                    withContext(Dispatchers.Main) {
                        binding.apply {
                            mealStr.text = meal.strMeal
                            mealArea.text = "Area: ${meal.strArea}"
                            mealCategory.text = "Category: ${meal.strCategory}"
                            mealInstructions.text = meal.strInstructions
                            Glide.with(this@MealActivity).load(meal.strMealThumb).into(mealImg)


                        }
                        adapter.data=ingredients
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Log.d(TAG, "Meal not found")
                }
            } catch (e: HttpException) {
                Log.e(TAG, "HTTP error: ${e.message}")
            } catch (e: IOException) {
                Log.e(TAG, "Network error: ${e.message}")
            }
        }
    }

    private fun fetchIngredients(meal: Meal): List<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()
        for (i in 1..20) {
            val ingredientName = meal.javaClass.getMethod("getStrIngredient$i").invoke(meal) as? String
            val ingredientMeasure = meal.javaClass.getMethod("getStrMeasure$i").invoke(meal) as? String
            if (!ingredientName.isNullOrBlank() && !ingredientMeasure.isNullOrBlank()) {
                val ingredientThumbUrl = "https://www.themealdb.com/images/ingredients/${ingredientName}-Small.png"
                ingredients.add(Ingredient(ingredientName, ingredientThumbUrl, ingredientMeasure))
            }
        }
        return ingredients
    }
}
