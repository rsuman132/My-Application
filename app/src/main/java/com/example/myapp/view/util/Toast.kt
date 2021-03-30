package com.example.myapp.view.util

import android.content.Context
import android.widget.Toast

object ToastMessage{
    fun displayToast(context: Context, message : String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}