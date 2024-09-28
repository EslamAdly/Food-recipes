package com.example.ratatouille

import android.content.Context
import android.view.View
import android.widget.Toast

fun makeToast(str: String, context: Context){
    Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
}
