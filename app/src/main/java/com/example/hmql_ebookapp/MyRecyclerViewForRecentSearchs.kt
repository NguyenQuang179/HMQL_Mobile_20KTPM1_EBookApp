package com.example.hmql_ebookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerViewForRecentSearchs(
    private val listOfSearch: ArrayList<String>
) : RecyclerView.Adapter<MyRecyclerViewForRecentSearchs.ViewHolder>() {
    var onButtonClick: ((String) -> Unit)? = null
    var onItemClick: ((String) -> Unit)? = null
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var recentSearchTV: TextView
        var deleteRecentSearch: Button
        init {
            // Define click listener for the ViewHolder's View.
            recentSearchTV = view.findViewById(R.id.recentSearchTV)
            deleteRecentSearch = view.findViewById<Button>(R.id.deleteRecentSearchBTN)
            view.setOnClickListener { onItemClick?.invoke(listOfSearch[adapterPosition]) }
            val deleteRecentSearchTemp = view.findViewById<Button>(R.id.deleteRecentSearchBTN)
            deleteRecentSearchTemp.setOnClickListener {
                onButtonClick?.invoke(listOfSearch[adapterPosition])
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item

        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recent_search_btn, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.recentSearchTV.setText(listOfSearch[position])

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        // filter the list to show only items satisfy the requirements
        return listOfSearch.size
    }
}