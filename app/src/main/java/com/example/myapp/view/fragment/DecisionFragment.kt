package com.example.myapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapp.R
import com.example.myapp.view.util.PreferenceProvider
import com.example.myapp.view.util.ToastMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class
DecisionFragment : Fragment(), View.OnClickListener{

    private lateinit var decisionRegister : Button
    private lateinit var preferenceProvider : PreferenceProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_decision, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        decisionRegister = view.findViewById(R.id.decision_register)

        decisionRegister.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.decision_register -> {
                preferenceProvider = PreferenceProvider(requireActivity())
                preferenceProvider.putBoolean("first_time_decision", true)
                findNavController().navigate(R.id.action_homeFragment_to_registerFragment)
            }
        }
    }
}