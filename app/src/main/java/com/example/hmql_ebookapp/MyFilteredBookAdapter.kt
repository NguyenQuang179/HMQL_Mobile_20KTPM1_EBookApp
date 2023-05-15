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
import java.util.*
import kotlin.collections.ArrayList

class MyFilteredBookAdapter(private val books : ArrayList<Book>, private var categoriesToFilter : ArrayList<String> )
    : RecyclerView.Adapter<MyFilteredBookAdapter.ViewHolder>(), Filterable{

    public var tempString: String = ""
    fun setCategoriesToFilter(categories: ArrayList<String>) {
        categoriesToFilter = categories
        notifyDataSetChanged()
    }

    fun getCategoriesToFilter(): ArrayList<String> {
        return categoriesToFilter
    }

    var onItemClick: ((Book) -> Unit)? = null
    var filteredBooks = ArrayList<Book>()

    init{
        filteredBooks = books
    }

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val statusTextView : TextView = listItemView.findViewById<TextView>(R.id.statusTextView)!!
        val bookNameTv : TextView = listItemView.findViewById<TextView>(R.id.bookNameTextView)!!
        val authorNameTv : TextView = listItemView.findViewById<TextView>(R.id.authorTextView)!!
        val bookImgView : ImageView = listItemView.findViewById<ImageView>(R.id.bookImageView)!!
        val bookmarkImgBtn : ImageView = listItemView.findViewById<ImageView>(R.id.bookmarkImageButton)!!
        val ratingTV: TextView = listItemView.findViewById<TextView>(R.id.ratingTextView)
        val bookmarkImageButton = listItemView.findViewById<ImageButton>(R.id.bookmarkImageButton)
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
        holder.statusTextView.visibility = View.INVISIBLE
        holder.bookmarkImgBtn.visibility = View.INVISIBLE
//        val student: RealmStudent = listStudentFilter.get(position)
        // Set item views based on your views and data model
        val bookNameTv = holder.bookNameTv
        bookNameTv.text = book.title
        val authorNameTv = holder.authorNameTv
        authorNameTv.text = book.author
        val ratingStarTV = holder.ratingTV
        ratingStarTV.text = book.averageStar.toString()
        val bookImgView = holder.bookImgView
        Glide.with(holder.bookImgView.context)
            .load(book.cover)
            .into(bookImgView)

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
            holder.statusTextView.text = "Finished"
            holder.statusTextView.setTextColor(getColor(holder.statusTextView.context, com.google.android.material.R.attr.colorOnSecondary))
            holder.statusTextView.background.setTint(getColor(holder.statusTextView.context, com.google.android.material.R.attr.colorSecondary))
        }
    }

    override fun getItemCount(): Int = filteredBooks.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                tempString = charSearch
                if (charSearch.isEmpty()) {
                    val resultList = ArrayList<Book>()
                    if (categoriesToFilter.size == 0){
                        filteredBooks = books;
                    }
                    else{
                        for (row in books) {
                            toBreak@ for (category in row.categories){
                                if (categoriesToFilter.contains(category.categoryName)){
                                    resultList.add(row)
                                    break@toBreak
                                }
                            }
                        }
                        filteredBooks = resultList
                    }
                } else {
                    val resultList = ArrayList<Book>()
                    for (row in books) {
                        if (row.title.toLowerCase().contains(charSearch.toLowerCase())) {
                            if (categoriesToFilter.size > 0){
                                Log.i("Filter size", categoriesToFilter.size.toString())
                                for (category in row.categories){
                                    if (categoriesToFilter.contains(category.categoryName)){
                                        resultList.add(row)
                                        break
                                    }
                                }
                            }
                            else{
                                resultList.add(row)
                            }
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