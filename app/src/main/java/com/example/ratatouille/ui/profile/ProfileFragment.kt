package com.example.ratatouille.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.ratatouille.R
import com.example.ratatouille.dataBase.FavoriteDatabase
import com.example.ratatouille.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {
    private lateinit var logoutBtn: Button
    private lateinit var userName: TextView
    private lateinit var profileImage:ImageView
    lateinit var favoriteDatabase : FavoriteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteDatabase = FavoriteDatabase.getFavoriteDatabase(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logoutBtn = view.findViewById(R.id.logout_btn)
        userName = view.findViewById(R.id.user_name)
        profileImage = view.findViewById(R.id.profile_image)
        userName.text = FirebaseAuth.getInstance().currentUser?.displayName ?: "eee"
        logoutBtn.setOnClickListener {
            userLogout()
        }

        Glide.with(this).load(FirebaseAuth.getInstance().currentUser?.photoUrl).into(profileImage)
    }

    private fun userLogout() {
        lifecycleScope.launch(Dispatchers.IO) {
            clearAllTables()
            FirebaseAuth.getInstance().signOut()
            withContext(Dispatchers.Main) {
                startActivity(Intent(requireContext(), LoginActivity::class.java))
            }
        }
    }
    private suspend fun clearAllTables() {

            favoriteDatabase.getMealDao().clearMeals()
            favoriteDatabase.getIngredientDao().clearIngredients()
            favoriteDatabase.getMealsPlanDao().clearMealPlans()
            favoriteDatabase.getCrossRefDao().clearCrossRefs()

    }
}
