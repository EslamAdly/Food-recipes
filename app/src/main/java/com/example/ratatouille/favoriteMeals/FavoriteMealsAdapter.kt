package com.example.ratatouille.favoriteMeals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ratatouille.R
import com.example.ratatouille.data.LocalMeal
import com.example.ratatouille.databinding.MealViewBinding

class FavoriteMealsAdapter (var meals:List<LocalMeal>):RecyclerView.Adapter<FavoriteMealsAdapter.MealViewHolder>(){
    class MealViewHolder(view: View):RecyclerView.ViewHolder(view) {
        val mealName=view.findViewById<TextView>(R.id.meal_name_txt)
        val mealImg=view.findViewById<ImageView>(R.id.meal_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.meal_view,parent,false)
        return MealViewHolder(view)
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        holder.mealName.text=meals[position].strMeal
        Glide.with(holder.itemView.context).load(meals[position].strMealThumb).into(holder.mealImg)
    }

}