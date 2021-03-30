package com.example.myapp.view.util

import android.content.Context

class PreferenceProvider(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("PREFS", 0)

    fun putBoolean(key: String, value : Boolean){
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String) : Boolean{
        return sharedPreferences.getBoolean(key, false)
    }

    fun putLang(key : String, value : String){
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getLang(key : String) : String {
        return sharedPreferences.getString(key, "")!!
    }
}