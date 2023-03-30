package com.example.hmql_ebookapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btn = findViewById<Button>(R.id.button);
        btn.setOnClickListener({
            var intent = Intent(this, searchActivity::class.java)
            startActivity(intent);
        })

        var huybtn = findViewById<Button>(R.id.huyButton);
        huybtn.setOnClickListener({
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<MyBooksFragment>(R.id.fragment_container_view)
            }
        })


            val navBar = findViewById<NavigationBarView>(R.id.bottom_navigation)
        //Bottom Navigation Menu
            navBar.setOnItemSelectedListener{ item ->
            when(item.itemId) {
                R.id.item_1 -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.item_2 -> {
                    Toast.makeText(this,"open Find Menu",Toast.LENGTH_SHORT).show()
                    // Respond to navigation item 2 click
                    true

                }
                R.id.item_3 -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.item_4 -> {
                    // Respond to navigation item 2 click
                    //setCurrentFragment(Fragment01())
                    supportFragmentManager.commit {
                        add<MyBooksFragment>(R.id.fragment_container_view)
                        setReorderingAllowed(true)
                        addToBackStack("name") // name can be null
                    }

                    true
                }
                else -> false
            }
        }
    }
}