package com.example.myapp.view.util

import android.app.Activity
import android.app.Dialog
import android.content.Context
import com.example.myapp.R

class CustomProgressDialog {
    lateinit var dialog : Dialog

    fun show(context: Context): Dialog {
        val inflater = (context as Activity).layoutInflater
        val view = inflater.inflate(R.layout.loading_progress_bar, null)

        dialog = Dialog(context)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        return dialog
    }
}