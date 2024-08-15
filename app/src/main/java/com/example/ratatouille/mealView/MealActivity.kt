package com.example.ratatouille.mealView

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ratatouille.R
import com.example.ratatouille.data.Ingredient
import com.example.ratatouille.data.LocalMeal
import com.example.ratatouille.dataBase.FavoriteDatabase
import com.example.ratatouille.databinding.ActivityMealBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MealActivity : AppCompatActivity(), MealView, MealClickListener {

    private lateinit var binding: ActivityMealBinding
    private lateinit var adapter: IngredientsAdapter
    private lateinit var presenter: MealViewPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        setupPresenter()
        val id = intent.getStringExtra("mealId")
        lifecycleScope.launch(Dispatchers.IO) {
            presenter.getData(id.toString())
            //presenter.isFavorite(id.toString())
        }

    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun setupPresenter() {
        val mealDao = FavoriteDatabase.getFavoriteDatabase(this).getMealDao()
        presenter = MealViewPresenter(this, mealDao)
    }

    private fun setupUI() {

        //webView setup
        setupWebView()
        setupRecyclerView()
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.addToFavoriteBtn.setOnClickListener {
            lifecycleScope.launch {
                presenter.changeFavorite()
            }
        }
    }



    private fun setupRecyclerView() {
        adapter = IngredientsAdapter(emptyList(),emptyList(), this)
        binding.mealIngredients.layoutManager = LinearLayoutManager(this)
        binding.mealIngredients.adapter = adapter

    }

    private fun setupWebView() {
        binding.webView.settings.apply {
            javaScriptEnabled = true
            builtInZoomControls = true
            supportZoom()
            loadWithOverviewMode = true // Ensures the content fits the screen width
        }
        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.webViewClient = WebViewClient()
    }

    private fun getYoutubeVideoUrl(strYoutube: String): String {
        // Extract video ID from YouTube URL
        val videoId = strYoutube.substringAfterLast("v=").substringBefore("&")
        val embedUrl = "https://www.youtube.com/embed/$videoId"

        // Load YouTube video in WebView
        val videoHtml = """
                <html>
                <body style='margin:0; padding:0;'>
                    <iframe width="100%" height="100%" src="$embedUrl"
                    frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                    allowfullscreen></iframe>
                </body>
                </html>"""
        return videoHtml
    }

    override fun showData(meal: LocalMeal,ingredients:List<Ingredient>) {
        binding.apply {
            mealStr.text = meal.strMeal
            mealArea.text = "Area: ${meal.strArea}"
            mealCategory.text = "Category: ${meal.strCategory}"
            mealInstructions.text = meal.strInstructions
            Glide.with(this@MealActivity).load(meal.strMealThumb).into(mealImg)
            val videoHtml = getYoutubeVideoUrl(meal.strYoutube)
            webView.loadData(videoHtml, "text/html", "utf-8")
            adapter.ingredientList=ingredients
            adapter.measureList =meal.strMeasureList

            adapter.notifyDataSetChanged()
        }

    }

    override fun addToFavorite() {
        binding.addToFavoriteBtn.setImageResource(R.drawable.baseline_red_favorite_24)
    }

    override fun removeFromFavorite() {
        binding.addToFavoriteBtn.setImageResource(R.drawable.baseline_shadow_favorite_24)
    }

    override fun showMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onIngredientClick(position: Int) {
        presenter.selectIngredient(position)
    }
}
