package com.example.myapp.view.fragment

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.myapp.R
import com.example.myapp.view.util.HelperLocale
import com.example.myapp.view.util.PreferenceProvider

class SplashFragment : Fragment() {

    private lateinit var splashImage : ImageView
    private lateinit var animation : Animation
    private lateinit var preferenceProvider: PreferenceProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Handler().postDelayed({
            openFirst()
        }, 2000)
        setLang()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        splashImage = view.findViewById(R.id.splash_icon)!!
        animation = AnimationUtils.loadAnimation(activity,
            R.anim.image_fade
        )
        splashImage.animation = animation
        super.onViewCreated(view, savedInstanceState)
    }

    private fun openFirst(){
        preferenceProvider = PreferenceProvider(requireActivity())
        val firstTimeLanguage : Boolean = preferenceProvider.getBoolean("First_time_language")
        val firstTimeWalkThrough : Boolean = preferenceProvider.getBoolean("First_time_walk_through")
        val getStarted : Boolean = preferenceProvider.getBoolean("first_time_decision")

        if (!firstTimeLanguage){

            findNavController().navigate(R.id.action_splashFragment_to_chooseLanguageFragment)

        } else {

            if (!firstTimeWalkThrough) {

                findNavController().navigate(R.id.action_splashFragment_to_walkThrowFragment)

            } else if(!getStarted){

                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)

            } else {

                findNavController().navigate(R.id.action_splashFragment_to_registerFragment)
            }

        }

    }

    private fun setLang(){
        preferenceProvider = PreferenceProvider(requireActivity())
        val lang = preferenceProvider.getLang("lang_code")
        HelperLocale.setLocale(requireActivity(), lang)
    }
}