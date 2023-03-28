package com.example.hmql_ebookapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class searchActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val listOfString = resources.getStringArray(R.array.recentSearch);
        var customRecyclerView = findViewById<RecyclerView>(R.id.recentSearchRecyclerView)
        var adapter = MyRecyclerViewForRecentSearchs(listOfString)
        customRecyclerView!!.adapter = adapter
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        customRecyclerView.layoutManager = layoutManager

        var listOfTypeSearch =  resources.getStringArray(R.array.SearchType)
        var spinner = findViewById<Spinner>(R.id.searchTypeSpinner);
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item, listOfTypeSearch
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        this@searchActivity, spinner.selectedItem.toString(), Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

    }
}