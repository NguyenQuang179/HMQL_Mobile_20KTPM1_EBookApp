package com.example.hmql_ebookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChapterAdapterClass(private val mList: List<ChapterViewModel>) : RecyclerView.Adapter<ChapterAdapterClass.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chapter_list_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ChapterViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.ChapterNumberTV.text = ChapterViewModel.chapter_number
        holder.ChapterNameTV.text = ChapterViewModel.chapter_name

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ChapterNumberTV: TextView = itemView.findViewById(R.id.ChapterNumberTV)
        val ChapterNameTV: TextView = itemView.findViewById(R.id.ChapterNameTV)
    }
}