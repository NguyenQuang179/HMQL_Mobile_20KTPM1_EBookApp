package com.example.hmql_ebookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerViewForCategoryChoice(
    private val listOfCategory: Array<String>
) : RecyclerView.Adapter<MyRecyclerViewForCategoryChoice.ViewHolder>() {
    var onItemClick: ((String) -> Unit)? = null
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var categoryCheckBox: CheckBox
        init {
            // Define click listener for the ViewHolder's View.
            categoryCheckBox = view.findViewById(R.id.categoryCheckBox)
            //view.setOnClickListener { onItemClick?.invoke(listOfStudentFiltered[adapterPosition]) }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.category_checkbox, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.categoryCheckBox.setText(listOfCategory[position])

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        // filter the list to show only items satisfy the requirements
        return listOfCategory.size
    }
}