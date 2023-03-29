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

        var bookintroductionbutton = findViewById<Button>(R.id.bookintrobtn);
        bookintroductionbutton.setOnClickListener({
            var intent = Intent(this, BookIntroductionActivity::class.java)
            startActivity(intent);
        })
    }
}