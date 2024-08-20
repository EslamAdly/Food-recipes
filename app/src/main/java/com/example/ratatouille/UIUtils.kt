package com.example.ratatouille

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun makeSnackBar(str: String, view: View){
    Snackbar.make(view, str, Snackbar.LENGTH_SHORT).show()
}
