package com.example.hmql_ebookapp

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import android.content.Context

class MyFilteredBookAdapter(private val books : ArrayList<Book>)
    : RecyclerView.Adapter<MyFilteredBookAdapter.ViewHolder>(), Filterable{

    var onItemClick: ((Book) -> Unit)? = null
    var filteredBooks = ArrayList<Book>();
    init{
        filteredBooks = books as ArrayList<Book>
    }

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val statusTextView : TextView = listItemView.findViewById<TextView>(R.id.statusTextView)!!
        val bookNameTv : TextView = listItemView.findViewById<TextView>(R.id.bookNameTextView)!!
        val authorNameTv : TextView = listItemView.findViewById<TextView>(R.id.authorTextView)!!
        val bookImgView : ImageView = listItemView.findViewById<ImageView>(R.id.bookImageView)!!
        val bookmarkImgBtn : ImageView = listItemView.findViewById<ImageView>(R.id.bookmarkImageButton)!!
        val ratingTV: TextView = listItemView.findViewById<TextView>(R.id.ratingTextView);
        init { listItemView.setOnClickListener { onItemClick?.invoke(filteredBooks[adapterPosition]) } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val favBooksView : View = inflater.inflate(R.layout.book_list_view, parent, false)
        return ViewHolder(favBooksView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the data model based on position
        val book : Book = filteredBooks[position]
//        val student: RealmStudent = listStudentFilter.get(position)
        // Set item views based on your views and data model
        val bookNameTv = holder.bookNameTv
        bookNameTv.setText(book.title)
        val authorNameTv = holder.authorNameTv
        authorNameTv.setText(book.author)
        val ratingStarTV = holder.ratingTV
        ratingStarTV.setText(book.averageStar.toString())
        val bookImgView = holder.bookImgView
        Glide.with(holder.bookImgView.context)
            .load(book.cover)
            .into(bookImgView);

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

    override fun getItemCount(): Int = filteredBooks.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filteredBooks = books as ArrayList<Book>
                } else {
                    val resultList = ArrayList<Book>()
                    for (row in books) {
                        if (row.title.toLowerCase().contains(constraint.toString().toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    filteredBooks = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredBooks
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredBooks = results?.values as ArrayList<Book>
                notifyDataSetChanged()
            }
        }
    }
}