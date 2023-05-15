package com.example.hmql_ebookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecommendationAdapter(private val books : ArrayList<Book>)
    : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>(){

    var onItemClick: ((Book) -> Unit)? = null

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
        val book : Book = books[position]
//        val student: RealmStudent = listStudentFilter.get(position)
        // Set item views based on your views and data model
        val bookNameTv = holder.bookNameTv
        bookNameTv.text = book.title
        val authorNameTv = holder.authorNameTv
        authorNameTv.text = book.author
        val bookImgView = holder.bookImgView
        Glide.with(holder.bookImgView.context)
            .load(book.cover)
            .into(bookImgView)
    }

    override fun getItemCount(): Int = books.size
}