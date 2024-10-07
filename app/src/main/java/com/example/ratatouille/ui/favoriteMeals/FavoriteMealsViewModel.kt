package com.example.ratatouille.ui.favoriteMeals

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ratatouille.data.database.LocalMeal
import com.example.ratatouille.dataBase.dao.MealDao
import com.example.ratatouille.internetServices.firebase.FirebaseHelper
import com.google.firebase.firestore.FirebaseFirestore

class FavoriteMealsViewModel(private val dao: MealDao) : ViewModel() {
    //private val _meals = MutableLiveData<List<LocalMeal>>()
    val meals: LiveData<List<LocalMeal>>
    //val firebaseHelper = FirebaseHelper(FirebaseFirestore.getInstance())
    init {
        meals=dao.getFavoriteMeals()
//        firebaseHelper.syncFavorites( meals.value?: emptyList())
//        firebaseHelper.syncFavorites(meals.value?: emptyList())
        //Log.d("FirebaseHelper", "Favorite meals synced: ${meals.value?.size ?: 0}")
        meals.observeForever { favoriteMeals ->
            if (favoriteMeals.isNotEmpty()) {
               // firebaseHelper.syncFavorites(favoriteMeals)
                Log.d("FirebaseHelper", "Favorite meals synced: ${favoriteMeals.size}")
            } else {
                Log.d("FirebaseHelper", "No favorite meals to sync")
            }
        }
    }


}