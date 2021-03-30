package com.example.myapp.view.fragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.myapp.R
import com.example.myapp.view.adapter.WalkThroughAdapter
import com.example.myapp.view.model.WalkThrough
import com.example.myapp.view.util.PreferenceProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException

class WalkThrowFragment : Fragment(), View.OnClickListener {

    private lateinit var walkThroughAdapter: WalkThroughAdapter
    private lateinit var viewPager2: ViewPager2
    private lateinit var container : LinearLayout
    private lateinit var nextText : ImageView
    private lateinit var skipButton : TextView

    private lateinit var handler : Handler
    private lateinit var runnable: Runnable
    private lateinit var preferenceProvider: PreferenceProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_walk_throw, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager2 = view.findViewById(R.id.viewPager_walk_through)
        container = view.findViewById(R.id.walk_through_indicator)
        nextText = view.findViewById(R.id.next_walk_through)
        skipButton = view.findViewById(R.id.skip_walk_through)
        viewPager2.adapter = walkThroughAdapter

        handler = Handler()
        runnable = Runnable {
            try {
                if (viewPager2.currentItem + 1 < walkThroughAdapter.itemCount){
                    viewPager2.currentItem += 1

                }
            }catch (e : IOException){
                e.printStackTrace()
            }
        }

        setUpIndicator()
        setUpCurrentIndicator(0)

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable, 2000)
                setUpCurrentIndicator(position)
            }
        })

        nextText.setOnClickListener(this)
        skipButton.setOnClickListener(this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        walkThroughAdapter = WalkThroughAdapter(
            listOf(
                WalkThrough(
                    R.drawable.covid,
                    resources.getString(R.string.corona_virus),
                    resources.getString(R.string.corona_virus_para)
                ),
                WalkThrough(
                    R.drawable.distance,
                    resources.getString(R.string.keep_distance),
                    resources.getString(R.string.keep_distance_para)
                ),
                WalkThrough(
                    R.drawable.hand,
                    resources.getString(R.string.hand_wash),
                    resources.getString(R.string.hand_wash_para)
                ),
                WalkThrough(
                    R.drawable.security,
                    resources.getString(R.string.sanitize),
                    resources.getString(R.string.sanitize_para)
                ),
                WalkThrough(
                    R.drawable.rules,
                    resources.getString(R.string.obey_rules),
                    resources.getString(R.string.obey_rules_para)
                )
            )
        )
    }

    private fun setUpIndicator(){
        val indicators = arrayOfNulls<ImageView>(walkThroughAdapter.itemCount)
        val layoutParams  : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {
            indicators[i] = ImageView(requireActivity())
            indicators[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(requireActivity(), R.drawable.indicator_inactive)
                )
                this?.layoutParams = layoutParams
            }
            container.addView(indicators[i])
        }
    }

    private fun setUpCurrentIndicator(index : Int){
        val childCount = container.childCount
        for (i in 0 until childCount){
            val imageView = container[i] as ImageView
            if (i == index){
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
            }
        }

    }

    override fun onClick(v: View?) {
        when(v?.id)  {
           R.id.next_walk_through -> {
               movement()
           }
            R.id.skip_walk_through -> {
                samePage()
            }
        }
        handler.removeCallbacks(runnable)
    }

    private fun movement(){
        if (viewPager2.currentItem + 1 < walkThroughAdapter.itemCount){
            viewPager2.currentItem += 1
        }else {
           samePage()
        }
    }

    private fun samePage(){

        preferenceProvider = PreferenceProvider(requireActivity())
        preferenceProvider.putBoolean("First_time_walk_through", true)

        findNavController().navigate(R.id.action_walkThrowFragment_to_homeFragment)

    }

}