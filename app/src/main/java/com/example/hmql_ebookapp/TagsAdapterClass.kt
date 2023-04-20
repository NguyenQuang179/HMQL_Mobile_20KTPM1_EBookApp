package com.example.hmql_ebookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TagsAdapterClass(private val mList: List<TagsViewModel>) : RecyclerView.Adapter<TagsAdapterClass.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tags_book_intro, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val TagsViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.tagsTV.text = TagsViewModel.tag

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tagsTV: TextView = itemView.findViewById(R.id.tagsTV)
    }
}