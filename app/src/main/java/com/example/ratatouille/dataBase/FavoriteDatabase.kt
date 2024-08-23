package com.example.ratatouille.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ratatouille.data.database.Ingredient
import com.example.ratatouille.data.database.LocalMeal
import com.example.ratatouille.data.database.MealsPlan
import com.example.ratatouille.data.relations.MealIngredientCrossRef
import com.example.ratatouille.dataBase.dao.CrossRefDao
import com.example.ratatouille.dataBase.dao.IngredientDao
import com.example.ratatouille.dataBase.dao.MealDao
import com.example.ratatouille.dataBase.dao.MealsPlanDao

@Database (entities = [LocalMeal::class, Ingredient::class, MealIngredientCrossRef::class, MealsPlan::class], version = 1)
@TypeConverters(Converters::class)
abstract class FavoriteDatabase: RoomDatabase() {

    abstract fun getMealDao(): MealDao
    abstract fun getIngredientDao(): IngredientDao
    abstract fun getCrossRefDao(): CrossRefDao
    abstract fun getMealsPlanDao(): MealsPlanDao
    companion object {
        @Volatile
        private var INSTANCE:FavoriteDatabase?=null
        fun getFavoriteDatabase(ctx: Context): FavoriteDatabase{
            return INSTANCE ?: synchronized(this){
                val tempInstance = Room.databaseBuilder(ctx, FavoriteDatabase::class.java, "favorite_table").build()
                INSTANCE = tempInstance
                tempInstance
            }
        }
    }

}