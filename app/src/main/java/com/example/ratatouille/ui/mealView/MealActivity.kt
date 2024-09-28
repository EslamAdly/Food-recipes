package com.example.ratatouille.ui.mealView

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.ratatouille.R
import com.example.ratatouille.data.DayOfWeek
import com.example.ratatouille.data.database.Ingredient
import com.example.ratatouille.data.database.LocalMeal
import com.example.ratatouille.dataBase.FavoriteDatabase
import com.example.ratatouille.databinding.ActivityMealBinding
import com.example.ratatouille.factory.MealViewModelFactory
import com.example.ratatouille.internetServices.API.MealRetrofitInstance
import com.example.ratatouille.makeToast

class MealActivity : AppCompatActivity(), MealClickListener {

    private lateinit var binding: ActivityMealBinding
    private lateinit var adapter: IngredientsAdapter
    private lateinit var viewModel: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        setupViewModel()
        val id = intent.getStringExtra("mealId")
        val source = intent.getStringExtra("source")
        if (id != null && source != null) {
            viewModel.getData(id, source)
        }
        observeViewModel()
    }

    //setup
    private fun setupViewModel() {
        val mealDao = FavoriteDatabase.getFavoriteDatabase(this).getMealDao()
        val ingredientDao = FavoriteDatabase.getFavoriteDatabase(this).getIngredientDao()
        val crossRefDao = FavoriteDatabase.getFavoriteDatabase(this).getCrossRefDao()
        val mealsPlanDao = FavoriteDatabase.getFavoriteDatabase(this).getMealsPlanDao()
        val retrofit = MealRetrofitInstance.retrofitService
        val factory =
            MealViewModelFactory(mealDao, ingredientDao, crossRefDao, mealsPlanDao, retrofit)
        viewModel = ViewModelProvider(this, factory)[MealViewModel::class.java]
    }

    private fun setupUI() {

        //webView setup
        setupWebView()
        setupRecyclerView()
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.addToFavoriteBtn.setOnClickListener {
            viewModel.changeFavorite()
        }
        binding.addToPlanBtn.setOnClickListener {
            val popupMenu = androidx.appcompat.widget.PopupMenu(this, binding.addToPlanBtn)
            popupMenu.menuInflater.inflate(R.menu.week_days_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                item ->daySelect(item)

            }
            popupMenu.show()
        }
    }

    private fun daySelect(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.saturday -> {
                Log.d("weekDebug", "Saturday clicked")
                viewModel.addMealToPlan(DayOfWeek.SATURDAY)
                true
            }
            R.id.sunday -> {
                viewModel.addMealToPlan(DayOfWeek.SUNDAY)
                true
            }
            R.id.monday -> {
                viewModel.addMealToPlan(DayOfWeek.MONDAY)
                true
            }
            R.id.tuesday -> {
                viewModel.addMealToPlan(DayOfWeek.TUESDAY)
                true
            }
            R.id.wednesday -> {
                viewModel.addMealToPlan(DayOfWeek.WEDNESDAY)
                true
            }
            R.id.thursday -> {
                viewModel.addMealToPlan(DayOfWeek.THURSDAY)
                true
            }
            R.id.friday -> {
                viewModel.addMealToPlan(DayOfWeek.FRIDAY)
                true
            }
            else -> false
        }
    }

    private fun setupRecyclerView() {
        adapter = IngredientsAdapter(emptyList(), emptyList(), this)
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

    private fun observeViewModel() {
        viewModel.mealData.observe(this) { (meal, ingredients) ->
            showData(meal, ingredients)

        }
        viewModel.isFavorite.observe(this) { isFavorite ->
            if (isFavorite) {
                binding.addToFavoriteBtn.setImageResource(R.drawable.baseline_red_favorite_24)
            } else {
                binding.addToFavoriteBtn.setImageResource(R.drawable.baseline_shadow_favorite_24)
            }
        }
        viewModel.message.observe(this) { message ->
            makeToast(message, this)
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

    fun showData(meal: LocalMeal, ingredients: List<Ingredient>) {
        binding.apply {
            mealStr.text = meal.strMeal
            mealArea.text = "Area: ${meal.strArea}"
            mealCategory.text = "Category: ${meal.strCategory}"
            mealInstructions.text = meal.strInstructions
            Glide.with(this@MealActivity).load(meal.strMealThumb).into(mealImg)
            val videoHtml = getYoutubeVideoUrl(meal.strYoutube)
            webView.loadData(videoHtml, "text/html", "utf-8")
            adapter.ingredientList = ingredients
            adapter.measureList = meal.strMeasureList
            adapter.notifyDataSetChanged()
        }
    }

    override fun onIngredientClick(position: Int) {
        viewModel.selectIngredient(position)
    }
}
