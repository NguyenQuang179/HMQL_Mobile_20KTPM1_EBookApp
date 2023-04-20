package com.example.hmql_ebookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecommendationAdapter(private val books : ArrayList<SampleBook>)
    : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>(){

    var onItemClick: ((SampleBook) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val bookNameTv : TextView = listItemView.findViewById<TextView>(R.id.recBookNameTv)!!
        val authorNameTv : TextView = listItemView.findViewById<TextView>(R.id.recBookAuthorNameTv)!!
        val bookImgView : ImageView = listItemView.findViewById<ImageView>(R.id.recBookImgView)!!
        init { listItemView.setOnClickListener { onItemClick?.invoke(books[adapterPosition]) } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val recommendationbooksView : View = inflater.inflate(R.layout.recommendationbooks_layout, parent, false)
        return ViewHolder(recommendationbooksView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val book : SampleBook = books[position]
//        val student: RealmStudent = listStudentFilter.get(position)
        // Set item views based on your views and data model
        val bookNameTv = holder.bookNameTv
        bookNameTv.setText(book.bookName)
        val authorNameTv = holder.authorNameTv
        authorNameTv.setText(book.authorName)
        val bookImgView = holder.bookImgView
        bookImgView.setImageResource(book.bookImg)
    }

    override fun getItemCount(): Int = books.size
}