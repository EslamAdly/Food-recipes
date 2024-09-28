package com.example.ratatouille.ui.homePage

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ratatouille.R
import com.example.ratatouille.data.remote.Category
import com.example.ratatouille.ui.displayMeals.DisplayMeals
import com.example.ratatouille.ui.mealView.MealActivity

class CategoriesAdapter(var data: List<Category>) :
    RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryName = view.findViewById<TextView>(R.id.CategoryName)
        val categoryImg = view.findViewById<ImageView>(R.id.category_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_category, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.categoryName.text = data[position].strCategory
        Glide.with(holder.itemView.context).load(data[position].strCategoryThumb).into(holder.categoryImg)
        val str = data[position].strCategory
        holder.categoryImg.setOnClickListener {
            val intent = Intent(holder.itemView.context, DisplayMeals::class.java).apply{
                putExtra("searchType", "category")
                putExtra("query", str)

            }
            holder.itemView.context.startActivity(intent)
        }
    }
}