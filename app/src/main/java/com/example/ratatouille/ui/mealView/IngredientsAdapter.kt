package com.example.ratatouille.ui.mealView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ratatouille.R
import com.example.ratatouille.data.Ingredient

class IngredientsAdapter(var data: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ingredientTitle = view.findViewById<TextView>(R.id.txt_ingredient_name)
        val ingredientImg = view.findViewById<ImageView>(R.id.img_ingredient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ingredientTitle.text ="${data[position].strMeasure} - ${data[position].strIngredient}"
        Glide.with(holder.itemView.context).load(data[position].strIngredientThump).into(holder.ingredientImg)
    }
}
//"https://www.themealdb.com/images/ingredients/${data[position].strIngredient}-Small.png"