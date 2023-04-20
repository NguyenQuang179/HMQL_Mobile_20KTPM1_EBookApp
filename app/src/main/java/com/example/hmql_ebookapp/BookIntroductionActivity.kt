package com.example.hmql_ebookapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class BookIntroductionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_introduction)
        val TagsRV = findViewById<RecyclerView>(R.id.TagsRV)

        // this creates a vertical layout Manager
        TagsRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<TagsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..20) {
            data.add(TagsViewModel("Item " + i))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = TagsAdapterClass(data)

        // Setting the Adapter with the recyclerview
        TagsRV.adapter = adapter
    }

    internal class Row_Author_Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    internal class Row_Tags_Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    internal class Row_Synopsis_Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    internal class Row_Chapters_Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

    internal class RvAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == 0) {
                val view: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.row_author_book_intro, parent, false)
                return  Row_Author_Holder(view)
            }
            else if (viewType == 1){
                val view: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.row_tags_book_intro, parent, false)
                return  Row_Tags_Holder(view)
            }
            else if (viewType == 2){
                val view: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.row_synopsis_book_intro, parent, false)
                return  Row_Synopsis_Holder(view)
            }
            else if (viewType == 3){
                val view: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.row_chapters_book_intro, parent, false)
                return  Row_Chapters_Holder(view)
            }
            else {
                val view: View =
                    LayoutInflater.from(parent.context).inflate(R.layout.row_tags_book_intro, parent, false)
                return  Row_Tags_Holder(view)
            }

        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}
        override fun getItemViewType(position: Int): Int {
            return if (position == 0) 1 else position % 3
        }

        override fun getItemCount(): Int {
            return 10
        }
    }

}