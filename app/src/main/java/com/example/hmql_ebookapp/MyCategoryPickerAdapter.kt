package com.example.hmql_ebookapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MyCategoryPickerAdapter(private val CategoryList : ArrayList<Category>)
    : RecyclerView.Adapter<MyCategoryPickerAdapter.ViewHolder>() {

    var onItemClick: ((Category) -> Unit)? = null

    init{

    }

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val toggleButton = listItemView.findViewById<ToggleButton>(R.id.toggleButton)!!
        init { listItemView.setOnClickListener { onItemClick?.invoke(CategoryList[adapterPosition]) } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val favBooksView : View = inflater.inflate(R.layout.toggle_btn_for_category_pick, parent, false)
        return ViewHolder(favBooksView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val category : Category = CategoryList[position]
        holder.toggleButton.setText(category.categoryName);
    }

    override fun getItemCount(): Int = CategoryList.size
}