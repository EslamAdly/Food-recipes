package com.example.ratatouille.ui.planMeals

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ratatouille.R
import com.example.ratatouille.data.database.MealsPlan
import com.example.ratatouille.ui.mealView.MealActivity

class PlanMealAdapter(var meals: List<MealsPlan>) :
    RecyclerView.Adapter<PlanMealAdapter.PlanMealViewHolder>() {
    class PlanMealViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mealName = view.findViewById<TextView>(R.id.meal_name_txt)
        val mealImg = view.findViewById<ImageView>(R.id.meal_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanMealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_view, parent, false)
        return PlanMealViewHolder(view)
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    override fun onBindViewHolder(holder: PlanMealViewHolder, position: Int) {
        holder.mealName.text = meals[position].strMeal
        Glide.with(holder.itemView.context).load(meals[position].strMealThumb).into(holder.mealImg)
        holder.mealImg.setOnClickListener {
            val intent = Intent(holder.itemView.context, MealActivity::class.java).apply {
                putExtra("mealId", meals[position].mealId)
                putExtra("source", "retrofit")
            }
            holder.itemView.context.startActivity(intent)
        }
    }
}