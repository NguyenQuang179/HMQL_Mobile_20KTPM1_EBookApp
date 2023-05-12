package com.example.hmql_ebookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PDFReaderAdapter(private val pages : ArrayList<String>)
    : RecyclerView.Adapter<PDFReaderAdapter.ViewHolder>(){

    var onItemClick: ((String) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val pageTv : TextView = listItemView.findViewById<TextView>(R.id.pageTv)!!
        init { listItemView.setOnClickListener { onItemClick?.invoke(pages[adapterPosition]) } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val pageView : View = inflater.inflate(R.layout.pdf_reader_file_layout, parent, false)
        return ViewHolder(pageView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val page: String = pages.get(position)
        // Set item views based on your views and data model
        val pageTv = holder.pageTv
        pageTv.setText(page)
    }

    override fun getItemCount(): Int = pages.size
}