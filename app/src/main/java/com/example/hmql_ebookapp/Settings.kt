package com.example.hmql_ebookapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate

class Settings : AppCompatActivity() {
    var isDark : Boolean = false
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme) //when dark mode is enabled, we use the dark theme
            isDark = true
        } else {
            setTheme(R.style.lightTheme)  //default app theme
            isDark = false
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val settingBtn = findViewById<Switch>(R.id.settingThemeSwitch)
        settingBtn.isChecked = isDark
        settingBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                finish()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                finish()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }
    }
}