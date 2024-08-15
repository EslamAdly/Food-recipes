package com.example.ratatouille.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ratatouille.data.Ingredient
import com.example.ratatouille.data.LocalMeal
import com.example.ratatouille.data.relations.MealIngredientCrossRef

@Database (entities = [LocalMeal::class, Ingredient::class, MealIngredientCrossRef::class], version = 1)
@TypeConverters(Converters::class)
abstract class FavoriteDatabase: RoomDatabase() {
    abstract fun getMealDao(): MealDao
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