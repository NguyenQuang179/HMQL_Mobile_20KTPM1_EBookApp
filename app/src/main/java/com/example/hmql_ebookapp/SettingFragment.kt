package com.example.hmql_ebookapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var isDark : Boolean = false
    var baseIsDark : Boolean = false
    var fontFamily : String = "sans_serif.ttf"

    override fun onCreate(savedInstanceState: Bundle?) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            //context?.setTheme(R.style.darkTheme) //when dark mode is enabled, we use the dark theme
            isDark = true
            baseIsDark = true
        } else {
            //context?.setTheme(R.style.lightTheme)  //default app theme
            isDark = false
            baseIsDark = false
        }
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backBtn = view.findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener(){
            requireActivity().supportFragmentManager.popBackStack()
        }

        val sansSerifBtn = view.findViewById<Button>(R.id.sansSerifBtn)
        val latoBtn = view.findViewById<Button>(R.id.latoBtn)
        val playfairDisplayBtn = view.findViewById<Button>(R.id.playfairDisplayBtn)
        val merriWeatherBtn = view.findViewById<Button>(R.id.merriWeatherBtn)

        val textview = view.findViewById<TextView>(R.id.textView6)
        sansSerifBtn.setOnClickListener() {
            fontFamily = "sans_serif.ttf"
            textview.typeface = sansSerifBtn.typeface
        }

        latoBtn.setOnClickListener() {
            fontFamily = "lato.ttf"
            textview.typeface = latoBtn.typeface
        }

        playfairDisplayBtn.setOnClickListener() {
            fontFamily = "playfair_display.ttf"
            textview.typeface = playfairDisplayBtn.typeface
        }

        merriWeatherBtn.setOnClickListener() {
            fontFamily = "merriweather.ttf"
            textview.typeface = merriWeatherBtn.typeface
        }

        val fontSizeSlider = view.findViewById<Slider>(R.id.settingFontsizeSB)
        val curFontSizeTv = view.findViewById<TextView>(R.id.fontSizeTv)
        fontSizeSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
                curFontSizeTv.setText(fontSizeSlider.value.toInt().toString())
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
                curFontSizeTv.setText(fontSizeSlider.value.toInt().toString())
            }
        })

        val themeBtn = view.findViewById<Switch>(R.id.settingThemeSwitch)
        themeBtn.isChecked = isDark
        themeBtn.setOnCheckedChangeListener { _, isChecked ->
            isDark = isChecked
        }

        val saveBtn = view.findViewById<Button>(R.id.settingSaveBtn)
        saveBtn.setOnClickListener() {
            val fontSizeSlider = view.findViewById<Slider>(R.id.settingFontsizeSB)
            var fontSize: Float = fontSizeSlider.value.toFloat()

//            Font Family
            //Toast.makeText(requireContext(), fontFamily.toString(), Toast.LENGTH_SHORT).show()
//            Font Size
            //Toast.makeText(requireContext(), fontSizeSlider.value.toString(), Toast.LENGTH_SHORT).show()
//            Pop Back Stack & Change Mode
            if (isDark != baseIsDark) {
                requireActivity().recreate()
                if (isDark) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            } else {
                setFragmentResult(
                    "settingResult",
                    bundleOf(
                        "fontSize" to fontSize,
                        "fontFamily" to fontFamily
                    )
                )
                requireActivity().supportFragmentManager.popBackStack()
                if (isDark) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}