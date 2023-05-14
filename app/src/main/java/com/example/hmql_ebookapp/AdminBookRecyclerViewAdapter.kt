package com.example.hmql_ebookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class AdminBookRecyclerViewAdapter(private val books : ArrayList<Book>)
    : RecyclerView.Adapter<AdminBookRecyclerViewAdapter.ViewHolder>() {

    var onItemClick: ((Book) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val bookNameTv : TextView = listItemView.findViewById<TextView>(R.id.AdminBookNameTextView)!!
        val authorNameTv : TextView = listItemView.findViewById<TextView>(R.id.AdminBookAuthorTv)!!
        val bookImgView : ImageView = listItemView.findViewById<ImageView>(R.id.AdminBookCoverImageView)!!
        val delBtn : Button = listItemView.findViewById<Button>(R.id.AdminBookDeleteBtn)!!
        val editBtn : Button = listItemView.findViewById<Button>(R.id.AdminBookEditBtn)!!
        init { listItemView.setOnClickListener { onItemClick?.invoke(books[adapterPosition]) } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminBookRecyclerViewAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val adminBookView : View = inflater.inflate(R.layout.admin_book_recycler_view_layout, parent, false)
        return ViewHolder(adminBookView)
    }

    override fun onBindViewHolder(holder: AdminBookRecyclerViewAdapter.ViewHolder, position: Int) {
        val book : Book = books[position]
//        val student: RealmStudent = listStudentFilter.get(position)
        // Set item views based on your views and data model
        val bookNameTv = holder.bookNameTv
        bookNameTv.setText(book.title)
        val authorNameTv = holder.authorNameTv
        authorNameTv.setText(book.author)
        val bookImgView = holder.bookImgView
        Glide.with(holder.bookImgView.context)
            .load(book.cover)
            .into(bookImgView);
        val delBtn = holder.delBtn
        val editBtn = holder.editBtn
        //set on click listener for a button in a recycler View item that change into another image on click
        //holder.bookImgView.setOnClickListener { holder.bookImgView.setImageResource(R.drawable.ic_baseline_favorite_24) }
        holder.delBtn.setOnClickListener {
            Toast.makeText(holder.bookNameTv.context, "Delete Clicked On Item " + position, Toast.LENGTH_SHORT).show()
            val ref = FirebaseDatabase.getInstance().getReference("book/${books[position].bookID}")
            ref.removeValue()
                .addOnSuccessListener {
                    // Data deleted successfully
                    books.removeAt(position)
                    this.notifyDataSetChanged()
                    Toast.makeText(holder.bookNameTv.context, books.count().toString(), Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Handle error
                    println("Error deleting data: ${it.message}")
                }

        }

        holder.editBtn.setOnClickListener {
            Toast.makeText(holder.bookNameTv.context, "Edit Clicked On Item " + position, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = books.size
}