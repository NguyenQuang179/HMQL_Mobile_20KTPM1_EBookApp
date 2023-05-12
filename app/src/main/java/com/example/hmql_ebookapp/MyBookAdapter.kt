package com.example.hmql_ebookapp

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


fun getColor(context: Context, colorResId: Int): Int {

    //return ContextCompat.getColor(context, colorResId); // Doesn't seem to work for R.attr.colorPrimary
    val typedValue = TypedValue()
    val typedArray: TypedArray =
        context.obtainStyledAttributes(typedValue.data, intArrayOf(colorResId))
    val color = typedArray.getColor(0, 0)
    typedArray.recycle()
    return color
}

class MyBookAdapter(private val books : List<UserBook>)
    : RecyclerView.Adapter<MyBookAdapter.ViewHolder>(){

    var onItemClick: ((UserBook) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val statusTextView : TextView = listItemView.findViewById<TextView>(R.id.statusTextView)!!
        val bookNameTv : TextView = listItemView.findViewById<TextView>(R.id.bookNameTextView)!!
        val authorNameTv : TextView = listItemView.findViewById<TextView>(R.id.authorTextView)!!
        val bookImgView : ImageView = listItemView.findViewById<ImageView>(R.id.bookImageView)!!
        val bookmarkImgBtn : ImageView = listItemView.findViewById<ImageView>(R.id.bookmarkImageButton)!!
        init { listItemView.setOnClickListener { onItemClick?.invoke(books[adapterPosition]) } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val favBooksView : View = inflater.inflate(R.layout.book_list_view, parent, false)
        return ViewHolder(favBooksView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book : UserBook = books[position]
//        val student: RealmStudent = listStudentFilter.get(position)
        // Set item views based on your views and data model
        val bookNameTv = holder.bookNameTv
        bookNameTv.setText(book.bookName)
        val authorNameTv = holder.authorNameTv
        authorNameTv.setText("TB Added")
        //val bookImgView = holder.bookImgView
        //bookImgView.setImageResource(book.bookImg)

        //set on click listener for a button in a recycler View item that change into another image on click
        //holder.bookImgView.setOnClickListener { holder.bookImgView.setImageResource(R.drawable.ic_baseline_favorite_24) }
        holder.bookmarkImgBtn.setOnClickListener {
            if (holder.bookmarkImgBtn.tag == "bookmark") {
                holder.bookmarkImgBtn.setImageResource(R.drawable.bookmark_solid)
                holder.bookmarkImgBtn.tag = "bookmarked"
            } else {
                holder.bookmarkImgBtn.setImageResource(R.drawable.bookmark_regular)
                holder.bookmarkImgBtn.tag = "bookmark"
            }
        }
        //make statusTextView Change its text color, background colors, and text on click
        holder.statusTextView.setOnClickListener {
            holder.statusTextView.setText("Finished")
            holder.statusTextView.setTextColor(getColor(holder.statusTextView.context, com.google.android.material.R.attr.colorOnSecondary))
            holder.statusTextView.background.setTint(getColor(holder.statusTextView.context, com.google.android.material.R.attr.colorSecondary))
        }
    }

    override fun getItemCount(): Int = books.size
}