package com.example.ratatouille.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.ratatouille.R
import com.example.ratatouille.dataBase.FavoriteDatabase
import com.example.ratatouille.internetServices.firebase.FirebaseHelper
import com.example.ratatouille.ui.ContainerPage.ContainerActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    private lateinit var firebaseHelper: FirebaseHelper
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInButton: SignInButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        googleSignInButton = findViewById(R.id.googleSignInButton)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseHelper = FirebaseHelper(FirebaseFirestore.getInstance())
        googleSignInButton.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        googleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }
//    private fun signIn() {
//        val signInIntent = googleSignInClient.signInIntent
//        launcher.launch(signInIntent)
//    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    Log.d(TAG, "firebaseAuthWithGoogle: ${account.id}")
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Log.e(TAG, "Google sign in failed", e)
                }
            } else {
                Log.e(TAG, "Google sign in failed: result code ${result.resultCode}")
                // Handle the cancellation or failure scenario
            }
        }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "firebaseAuthWithGoogle1: success")

                    syncUserData()

                    val intent = Intent(this@LoginActivity, ContainerActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Log.w(TAG, "firebaseAuthWithGoogle: failure", task.exception)
                }
            }
    }
    @SuppressLint("SuspiciousIndentation")
    private fun syncUserData() {
        val mealDao = FavoriteDatabase.getFavoriteDatabase(this).getMealDao()
        val ingredientDao = FavoriteDatabase.getFavoriteDatabase(this).getIngredientDao()
        val crossRefDao = FavoriteDatabase.getFavoriteDatabase(this).getCrossRefDao()
        val mealsPlanDao = FavoriteDatabase.getFavoriteDatabase(this).getMealsPlanDao()
        // Assuming your DAOs are properly initialized, inject them here
            // Sync data from Firebase to local Room DB
            CoroutineScope(Dispatchers.IO).launch {
                firebaseHelper.fetchUserDataAndSync(mealDao, ingredientDao, mealsPlanDao, crossRefDao)
            }
    }
}
