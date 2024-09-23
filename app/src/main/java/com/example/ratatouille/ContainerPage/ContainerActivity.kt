package com.example.ratatouille.ContainerPage

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.ratatouille.R
import com.example.ratatouille.internetServices.connectivity.ConnectivityObserver
import com.example.ratatouille.internetServices.connectivity.NetworkConnectivityObserver
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch


class ContainerActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var connectivityObserver: ConnectivityObserver
    private var alertDialog: AlertDialog? = null
    var isNetworkAvailable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)
        setContentView(R.layout.activity_home_activite)
        setupNavigation()
        observeNetworkConnection()
    }

    private fun setupNavigation() {
        val bottomNavbar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavbar, navController)
        // Intercept navigation clicks
        bottomNavbar.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.homeFragment || item.itemId == R.id.displayIngredientsFragment) {
                if (isNetworkAvailable) {
                    // Allow navigation to HomeFragment or displayIngredientsFragment if the internet is available
                    navController.navigate(item.itemId)
                    true
                } else {
                    // Show a Toast and don't navigate if the internet is unavailable
                    Toast.makeText(
                        this,
                        "No internet connection. Please check your connection.",
                        Toast.LENGTH_SHORT
                    ).show()
                    false
                }
            } else {
                // Allow other navigation items
                NavigationUI.onNavDestinationSelected(item, navController)
                true
            }
        }
    }

    private fun observeNetworkConnection() {
        // Immediately check network status on app start
        val initialStatus = connectivityObserver.getCurrentNetworkStatus()
        handleNetworkStatus(initialStatus)

        // Track network status change
        lifecycleScope.launch {
            connectivityObserver.observe().collect { status ->
                isNetworkAvailable = status == ConnectivityObserver.Status.Available
                handleNetworkStatus(status)
            }
        }
    }

    private fun handleNetworkStatus(status: ConnectivityObserver.Status) {
        when (status) {
            ConnectivityObserver.Status.Available -> {
                // Dismiss the dialog if the connection is available
                dismissAlertDialog()
                showToast("Connected")
            }

            ConnectivityObserver.Status.Lost, ConnectivityObserver.Status.Unavailable -> {
                // Show the dialog when there's no connection and restrict navigation
                showAlertDialog()
                showToast("Connection Lost")
                Log.d("connection", "unavailable")
            }

            ConnectivityObserver.Status.Losing -> {
                showToast("Losing Connection")
            }
        }
    }

    private fun showAlertDialog() {
        if (alertDialog == null) {
            alertDialog = AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("You are offline. You can only access Favorites and Meal Plans.")
                .setCancelable(false)
                .setPositiveButton("Go to Favorites") { dialog, _ ->
                    dialog.dismiss()
                    navHostFragment.navController.navigate(R.id.favoriteFragment)
                }
                .create()
        }
        alertDialog?.show()
    }

    private fun dismissAlertDialog() {
        alertDialog?.dismiss()
        alertDialog = null
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
