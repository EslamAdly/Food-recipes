package com.example.ratatouille

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.ratatouille.ContainerPage.ContainerActivity
import com.example.ratatouille.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

            if (currentUser == null) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            else{
                startActivity(Intent(this, ContainerActivity::class.java))
            }


    }


}
