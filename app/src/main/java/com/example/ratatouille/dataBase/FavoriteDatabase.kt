package com.example.ratatouille.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ratatouille.data.MealDB

@Database (entities = arrayOf(MealDB::class), version = 1)
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