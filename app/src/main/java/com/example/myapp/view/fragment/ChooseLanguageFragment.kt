package com.example.myapp.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat.recreate
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.R
import com.example.myapp.view.adapter.SelectLanguageAdapter
import com.example.myapp.view.model.SelectLanguage
import com.example.myapp.view.util.HelperLocale
import com.example.myapp.view.util.PreferenceProvider
import com.example.myapp.view.util.ToastMessage
import kotlin.collections.ArrayList

class ChooseLanguageFragment : Fragment() , SelectLanguageAdapter.LangClickListener, View.OnClickListener {

    private lateinit var selectLanguageText : TextView
    private lateinit var selectLanguageRV : RecyclerView
    private lateinit var selectLanguageAdapter: SelectLanguageAdapter
    private lateinit var skipLanguage : TextView
    private lateinit var searchView: EditText
    private val list = ArrayList<SelectLanguage>()
    private lateinit var etClose : ImageView
    private lateinit var resultNotFound : TextView
    private lateinit var selectLangButton : Button

    private lateinit var preferenceProvider: PreferenceProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skipLanguage = view.findViewById(R.id.skip_language)
        selectLanguageText = view.findViewById(R.id.chooseLanguageText)
        selectLanguageRV = view.findViewById(R.id.chooseLanguageRecyclerView)
        searchView = view.findViewById(R.id.search_language)
        etClose = view.findViewById(R.id.et_close)
        resultNotFound = view.findViewById(R.id.result_not_found)
        selectLangButton = view.findViewById(R.id.select_language_btn)


        selectLanguageAdapter = SelectLanguageAdapter(requireContext(), list, this)
        selectLanguageRV.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = selectLanguageAdapter
        }

        skipLanguage.setOnClickListener(this)
        etClose.setOnClickListener(this)
        selectLangButton.setOnClickListener(this)

        searchView.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {

                if (searchView.text.toString() != ""){
                    etClose.visibility = View.VISIBLE
                } else {
                    etClose.visibility = View.INVISIBLE

                }

                selectLanguageAdapter.filter.filter(charSequence.toString()) {

                    if (it == 0){
                        resultNotFound.visibility = View.VISIBLE
                        selectLangButton.visibility = View.GONE
                    } else {
                        resultNotFound.visibility = View.GONE
                        selectLangButton.visibility = View.VISIBLE
                    }

                    selectLanguageAdapter.notifyDataSetChanged()
                }

            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        list.add(SelectLanguage("English (Default)", "English", "en"))
        list.add(SelectLanguage("Arabic", "عربي", "ar"))
        list.add(SelectLanguage("Hindi", "हिन्दी", "hi"))
        list.add(SelectLanguage("Malayalam", "മലയാളം", "ml"))
        list.add(SelectLanguage("Tamil", "தமிழ்", "ta"))
        list.add(SelectLanguage("Telugu", "తెలుగు", "te"))
    }


      private fun setLocale(lang: String) {
          preferenceProvider = PreferenceProvider((requireActivity()))
          HelperLocale.setLocale(requireActivity(), lang)
          preferenceProvider.putLang("lang_code", lang)
          samePage()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.skip_language -> {
                setLocale("en")
            }
            R.id.et_close -> {
                searchView.text = null
                selectLanguageAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun samePage(){

        preferenceProvider = PreferenceProvider(requireActivity())
        preferenceProvider.putBoolean("First_time_language", true)

        findNavController().navigate(R.id.action_chooseLanguageFragment_to_walkThrowFragment)

    }

    override fun langClick(selectLanguage: SelectLanguage) {
        selectLanguageAdapter.showCheckMark()
        selectLangButton.visibility = View.VISIBLE
        selectLangButton.setOnClickListener {

            when(selectLanguage.defaultText){
                "English (Default)" -> {
                    setLocale("en")
                    recreate(requireActivity())
                }
                "Arabic" -> {
                    setLocale("ar")
                    recreate(requireActivity())
                }
                "Hindi" -> {
                    setLocale("hi")
                    recreate(requireActivity())
                }
                "Malayalam" -> {
                    setLocale("ml")
                    recreate(requireActivity())
                }
                "Tamil" -> {
                    setLocale("ta")
                    recreate(requireActivity())
                }
                "Telugu" -> {
                    setLocale("te")
                    recreate(requireActivity())
                }
            }

        }
    }

}

