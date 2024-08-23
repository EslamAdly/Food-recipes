package com.example.ratatouille.planMeals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ratatouille.data.DayOfWeek
import com.example.ratatouille.data.database.MealsPlan
import com.example.ratatouille.dataBase.dao.MealsPlanDao

class PlanMealViewModel(private val dao: MealsPlanDao) : ViewModel() {
    val saturdayMeals: LiveData<List<MealsPlan>>
    val sundayMeals: LiveData<List<MealsPlan>>
    val mondayMeals : LiveData<List<MealsPlan>>
    val tuesdayMeals: LiveData<List<MealsPlan>>
    val wednesdayMeals: LiveData<List<MealsPlan>>
    val thursdayMeals : LiveData<List<MealsPlan>>
    val fridayMeals: LiveData<List<MealsPlan>>

    init {
        saturdayMeals = dao.getMealsPlanForDay(DayOfWeek.SATURDAY)
        sundayMeals = dao.getMealsPlanForDay(DayOfWeek.SUNDAY)
        mondayMeals=dao.getMealsPlanForDay(DayOfWeek.MONDAY)
        tuesdayMeals=dao.getMealsPlanForDay(DayOfWeek.TUESDAY)
        wednesdayMeals=dao.getMealsPlanForDay(DayOfWeek.WEDNESDAY)
        thursdayMeals=dao.getMealsPlanForDay(DayOfWeek.THURSDAY)
        fridayMeals=dao.getMealsPlanForDay(DayOfWeek.FRIDAY)
    }
}