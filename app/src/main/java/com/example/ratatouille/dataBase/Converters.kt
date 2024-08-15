package com.example.ratatouille.dataBase

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromListToString(list:List<String>?):String{
        return list?.joinToString(",") ?: ""
    }
    @TypeConverter
    fun fromStringToList(str:String?):List<String>{
        return str?.split(",") ?: emptyList()
    }

}