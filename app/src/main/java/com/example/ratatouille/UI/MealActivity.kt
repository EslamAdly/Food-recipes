package com.example.ratatouille.UI

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.ratatouille.R
import com.example.ratatouille.databinding.ActivityMainBinding
import com.example.ratatouille.databinding.ActivityMealBinding
import com.example.ratatouille.internetServices.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class MealActivity : AppCompatActivity() {

    val TAG = "Error"
    lateinit var mealImage: ImageView
    lateinit var mealName: TextView
    lateinit var mealCategory: TextView
    lateinit var mealInstructions: TextView
    lateinit var mealArea: TextView

    lateinit var binding: ActivityMealBinding

    lateinit var mealIngredients: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        mealName=findViewById(R.id.mealStr)
//        mealCategory=findViewById(R.id.mealCategory)
//        mealInstructions=findViewById(R.id.mealInstructions)
//        mealArea=findViewById(R.id.mealArea)
//        mealImage=findViewById(R.id.mealImg)



        val id = intent.getStringExtra("mealId")
        if (id != null) {
            fetchMeal(id);
        }
        else {
            Log.d(TAG, "id is null")

        }
    }

    private fun fetchMeal(mealId: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val meal = RetrofitInstance.retrofitService.getMaelById(mealId).meals.firstOrNull()

                if (meal != null) {
                    withContext(Dispatchers.Main) {
                        binding.mealStr.text = meal.strMeal
                        binding.mealArea.text = "Area : ${meal.strArea}"
                        binding.mealCategory.text = "Category : ${meal.strCategory}"
                        binding.mealInstructions.text = meal.strInstructions
                        Glide.with(this@MealActivity).load(meal.strMealThumb).into(binding.mealImg)

                    }
                } else Log.d(TAG, "Meal not found")
            } catch (e: IOException) {
                Log.d(TAG, e.toString())
            }


        }
    }
}