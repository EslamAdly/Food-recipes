package com.example.ratatouille.mealView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ratatouille.R
import com.example.ratatouille.data.Ingredient

class IngredientsAdapter(var ingredientList: List<Ingredient>,var measureList:List<String>,val clickListener: MealClickListener) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ingredientTitle = view.findViewById<TextView>(R.id.txt_ingredient_name)
        val ingredientImg = view.findViewById<ImageView>(R.id.img_ingredient)
        val checkbox = view.findViewById<CheckBox>(R.id.checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ingredientTitle.text ="${measureList[position]} - ${ingredientList[position].strIngredient}"
        Glide.with(holder.itemView.context).load(ingredientList[position].strIngredientThump).into(holder.ingredientImg)

        holder.checkbox.setOnClickListener {
            clickListener.onIngredientClick(position)
        }
    }
}
