package com.example.myapp.view.util

import android.content.Context
import android.content.res.Resources
import java.util.*

class HelperLocale() {

    companion object{

        fun setLocale(context: Context,lang : String){
            val locale = Locale(lang)
            Locale.setDefault(locale)
            val resources : Resources =  context.resources

            val config = resources.configuration
            config.locale = locale
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }
}