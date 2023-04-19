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

class MyFilteredBookAdapter(private val books : ArrayList<SampleBook>)
    : RecyclerView.Adapter<MyFilteredBookAdapter.ViewHolder>(), Filterable{

    var onItemClick: ((SampleBook) -> Unit)? = null
    var filteredBooks = ArrayList<SampleBook>();
    init{
        filteredBooks = books as ArrayList<SampleBook>
    }

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val statusTextView : TextView = listItemView.findViewById<TextView>(R.id.statusTextView)!!
        val bookNameTv : TextView = listItemView.findViewById<TextView>(R.id.bookNameTextView)!!
        val authorNameTv : TextView = listItemView.findViewById<TextView>(R.id.authorTextView)!!
        val bookImgView : ImageView = listItemView.findViewById<ImageView>(R.id.bookImageView)!!
        val bookmarkImgBtn : ImageView = listItemView.findViewById<ImageView>(R.id.bookmarkImageButton)!!
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
        val book : SampleBook = filteredBooks[position]
//        val student: RealmStudent = listStudentFilter.get(position)
        // Set item views based on your views and data model
        val bookNameTv = holder.bookNameTv
        bookNameTv.setText(book.bookName)
        val authorNameTv = holder.authorNameTv
        authorNameTv.setText(book.authorName)
        val bookImgView = holder.bookImgView
        bookImgView.setImageResource(book.bookImg)

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
            holder.statusTextView.setTextColor(Color.parseColor("#FFFFFF"))
            holder.statusTextView.background.setTint(ContextCompat.getColor(holder.statusTextView.context, R.color.lightSecondary))
        }
    }

    override fun getItemCount(): Int = filteredBooks.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filteredBooks = books as ArrayList<SampleBook>
                } else {
                    val resultList = ArrayList<SampleBook>()
                    for (row in books) {
                        if (row.bookName.toLowerCase().contains(constraint.toString().toLowerCase())) {
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
                filteredBooks = results?.values as ArrayList<SampleBook>
                notifyDataSetChanged()
            }
        }
    }
}