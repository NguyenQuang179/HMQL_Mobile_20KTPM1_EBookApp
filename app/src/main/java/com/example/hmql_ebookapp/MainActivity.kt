package com.example.hmql_ebookapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme) //when dark mode is enabled, we use the dark theme
        } else {
            setTheme(R.style.lightTheme)  //default app theme
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val intent = Intent(this, ReadingScreen::class.java)
//        startActivity(intent)

        supportFragmentManager.commit {
            add<HomeFragment>(R.id.fragment_container_view)
//            setReorderingAllowed(true)
//            addToBackStack("name") // name can be null
        }
        val navBar = findViewById<NavigationBarView>(R.id.bottom_navigation)
        //Bottom Navigation Menu
            navBar.setOnItemSelectedListener{ item ->
            when(item.itemId) {
                R.id.item_1 -> {
                    // Respond to navigation item 1 click
                    supportFragmentManager.commit {
                        replace<HomeFragment>(R.id.fragment_container_view)
                        setReorderingAllowed(true)
                        addToBackStack("home") // name can be null
                    }
                    true
                }
                R.id.item_2 -> {
                    supportFragmentManager.commit {
                        replace<SearchBookFragment>(R.id.fragment_container_view)
                        setReorderingAllowed(true)
                        addToBackStack("searchBook") // name can be null
                    }                    // Respond to navigation item 2 click
                    true

                }
                R.id.item_3 -> {
                    // Respond to navigation item 3 click
                    supportFragmentManager.commit {
                        replace<AccountInformationFragment>(R.id.fragment_container_view)
                        setReorderingAllowed(true)
                        addToBackStack("accountInformation") // name can be null
                    }
                    true
                }
                R.id.item_4 -> {
                    // Respond to navigation item 2 click
                    //setCurrentFragment(Fragment01())
                    supportFragmentManager.commit {
                        replace<MyBooksFragment>(R.id.fragment_container_view)
                        setReorderingAllowed(true)
                        addToBackStack("myBooks") // name can be null
                    }

                    true
                }
                else -> false
            }
        }
    }
}