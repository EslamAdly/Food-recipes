package com.example.ratatouille.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ratatouille.internetServices.API.MealAPI
import com.example.ratatouille.search.SearchViewModel

class SearchViewModelFactory(private val retrofit: MealAPI):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SearchViewModel::class.java)){
            return SearchViewModel(retrofit) as T
        }else{
            throw IllegalArgumentException()
        }
    }

}