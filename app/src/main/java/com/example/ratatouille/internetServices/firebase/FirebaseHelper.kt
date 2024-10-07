package com.example.ratatouille.internetServices.firebase

import android.util.Log
import com.example.ratatouille.data.database.Ingredient
import com.example.ratatouille.data.database.LocalMeal
import com.example.ratatouille.data.database.MealsPlan
import com.example.ratatouille.data.relations.MealIngredientCrossRef
import com.example.ratatouille.dataBase.dao.CrossRefDao
import com.example.ratatouille.dataBase.dao.IngredientDao
import com.example.ratatouille.dataBase.dao.MealDao
import com.example.ratatouille.dataBase.dao.MealsPlanDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FirebaseHelper(private val firebaseStore: FirebaseFirestore) {
    val firebaseAuth = FirebaseAuth.getInstance()

    private val userId: String?
        get() = firebaseAuth.currentUser?.uid

    //meals methods
    fun addToFavorites(meal: LocalMeal) {
        firebaseStore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("favorites")
            .document(meal.idMeal).set(meal)
        Log.d("FirebaseHelper", "Favorite added: $meal.idMeal")
    }

    fun removeFromFavorites(mealId: String) {
        firebaseStore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("favorites")
            .document(mealId).delete()
        Log.d("FirebaseHelper", "Favorite removed: $mealId")

    }

    fun editFavorite(mealId: String, isFavorite: Boolean) {
        firebaseStore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("favorites")
            .document(mealId).update("isFavorite", isFavorite)
        Log.d("FirebaseHelper", "Favorite edited: $mealId")
    }

    //ingredients methods
//    fun addIngredientList(ingredients: List<Ingredient>) {
//        ingredients.forEach { ingredient ->
//            firebaseStore.collection("users").document(firebaseAuth.currentUser!!.uid)
//                .collection("ingredients")
//                .document(ingredient.strIngredient).set(ingredient)
//            Log.d("FirebaseHelper", "Ingredient synced: $ingredient.strIngredient")
//        }
//    }
    fun addIngredientList(ingredients: List<Ingredient>) {
        userId?.let {
            val batch = firebaseStore.batch()
            ingredients.forEach { ingredient ->
                val ingredientRef =
                    firebaseStore.collection("users").document(firebaseAuth.currentUser!!.uid)
                        .collection("ingredients")
                        .document(ingredient.strIngredient)
                batch.set(ingredientRef, ingredient)
            }
            batch.commit()
                .addOnSuccessListener {
                    Log.d("FirebaseHelper", "All ingredients synced")
                }
                .addOnFailureListener { e ->
                    Log.e("FirebaseHelper", "Error syncing ingredients", e)
                }
        }
    }

    fun addIngredient(ingredient: Ingredient) {
        firebaseStore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("ingredients")
            .document(ingredient.strIngredient).set(ingredient)
        Log.d("FirebaseHelper", "Ingredient added: $ingredient.strIngredient")
    }

    fun editIngredient(strIngredient: String, isSelected: Boolean) {
        firebaseStore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("ingredients")
            .document(strIngredient).update("isSelected", isSelected)
        Log.d("FirebaseHelper", "Ingredient edited: $strIngredient")
    }

    fun removeIngredient(ingredient: Ingredient) {
        firebaseStore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("ingredients")
            .document(ingredient.strIngredient).delete()
        Log.d("FirebaseHelper", "Ingredient removed: $ingredient.strIngredient")
    }

    fun addMealIngredientCrossRef(crossRef: MealIngredientCrossRef) {
        firebaseStore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("mealIngredientCrossRef")
            .document("${crossRef.idMeal}_${crossRef.strIngredient}").set(crossRef)
    }

    fun removeMealIngredientCrossRef(crossRef: MealIngredientCrossRef) {
        firebaseStore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("mealIngredientCrossRef")
            .document("${crossRef.idMeal}_${crossRef.strIngredient}").delete()
    }

    //meals plan methods
    fun addMealsPlan(mealsPlan: MealsPlan) {
        firebaseStore.collection("users").document(firebaseAuth.currentUser!!.uid)
            .collection("mealsPlan")
            .document("${mealsPlan.dayOfWeek.name}_${mealsPlan.mealId}").set(mealsPlan)
    }

    fun fetchUserDataAndSync(
        mealDao: MealDao,
        ingredientDao: IngredientDao,
        mealsPlanDao: MealsPlanDao,
        crossRefDao: CrossRefDao
    ) {
        val userId = firebaseAuth.currentUser!!.uid

        // Fetch meals
        firebaseStore.collection("users").document(userId)
            .collection("favorites")
            .get()
            .addOnSuccessListener { result ->
                val mealList = mutableListOf<LocalMeal>()
                for (document in result) {
                    document.toObject(LocalMeal::class.java).let { mealList.add(it) }
                }

                // Insert fetched meals into Room via MealDao in the background
                CoroutineScope(Dispatchers.IO).launch {
                    mealDao.insertAll(mealList)
                }
            }

        // Fetch ingredients
        firebaseStore.collection("users").document(userId)
            .collection("ingredients")
            .get()
            .addOnSuccessListener { result ->
                val ingredientList = mutableListOf<Ingredient>()
                for (document in result) {
                    document.toObject(Ingredient::class.java)?.let { ingredientList.add(it) }
                }

                // Insert fetched ingredients into Room via IngredientDao in the background
                CoroutineScope(Dispatchers.IO).launch {
                    ingredientDao.insertAll(ingredientList)
                }
            }

        // Fetch meal plans
        firebaseStore.collection("users").document(userId)
            .collection("mealsPlan")
            .get()
            .addOnSuccessListener { result ->
                val mealsPlanList = mutableListOf<MealsPlan>()
                for (document in result) {
                    document.toObject(MealsPlan::class.java)?.let { mealsPlanList.add(it) }
                }

                // Insert fetched meal plans via MealsPlanDao in the background
                CoroutineScope(Dispatchers.IO).launch {
                    mealsPlanDao.insertAll(mealsPlanList)
                }
            }

        // Fetch mealIngredientCrossRef
        firebaseStore.collection("users").document(userId)
            .collection("mealIngredientCrossRef")
            .get()
            .addOnSuccessListener { result ->
                val mealIngredientCrossRef = mutableListOf<MealIngredientCrossRef>()
                for (document in result) {
                    document.toObject(MealIngredientCrossRef::class.java)?.let {
                        mealIngredientCrossRef.add(it)
                    }
                }

                // Insert fetched crossRefs via CrossRefDao in the background
                CoroutineScope(Dispatchers.IO).launch {
                    crossRefDao.insertAll(mealIngredientCrossRef)
                }
            }
    }

}