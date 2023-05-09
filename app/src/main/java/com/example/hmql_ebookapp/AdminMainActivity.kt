package com.example.hmql_ebookapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit

class AdminMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)

        supportFragmentManager.commit {
            add<AdminDashboardFragment>(R.id.fragmentContainerView2)

        }
    }
}