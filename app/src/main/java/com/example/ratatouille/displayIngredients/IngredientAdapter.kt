package com.example.ratatouille.displayIngredients

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ratatouille.R
import com.example.ratatouille.data.database.Ingredient

class IngredientAdapter(var ingredientList: List<Ingredient>) :
    RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {
    class IngredientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ingredientTitle = view.findViewById<TextView>(R.id.txt_ingredient_name)
        val ingredientImg = view.findViewById<ImageView>(R.id.img_ingredient)
        val checkbox = view.findViewById<CheckBox>(R.id.checkbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
        return IngredientViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.ingredientTitle.text = ingredientList[position].strIngredient
        Glide.with(holder.itemView.context).load(ingredientList[position].strIngredientThump)
            .into(holder.ingredientImg)
        holder.checkbox.isChecked = ingredientList[position].isSelected
    }
}