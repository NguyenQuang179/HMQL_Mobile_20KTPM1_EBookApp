package com.example.hmql_ebookapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

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
            var intent = Intent(this, MyBooksListActivity::class.java)
            startActivity(intent);
        })

    }
}