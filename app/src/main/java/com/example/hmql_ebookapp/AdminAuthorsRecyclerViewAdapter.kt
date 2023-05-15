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

class AdminAuthorsRecyclerViewAdapter(private val authors : ArrayList<Author>)
    : RecyclerView.Adapter<AdminAuthorsRecyclerViewAdapter.ViewHolder>() {

    var onItemClick: ((Author) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val authorNameTv : TextView = listItemView.findViewById<TextView>(R.id.AdminAuthorNameTv)!!
        val authorImgView : ImageView = listItemView.findViewById<ImageView>(R.id.AdminAuthorImageView)!!
        val delBtn : Button = listItemView.findViewById<Button>(R.id.AdminAuthorDeleteBtn)!!
        init { listItemView.setOnClickListener { onItemClick?.invoke(authors[adapterPosition]) } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminAuthorsRecyclerViewAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val adminAuthorView : View = inflater.inflate(R.layout.admin_author_recycler_view_layout, parent, false)
        return ViewHolder(adminAuthorView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val author : Author = authors[position]

        val authorName = holder.authorNameTv
        authorName.text = author.name
        val authorImg = holder.authorImgView
        Glide.with(holder.authorImgView.context)
            .load(author.img)
            .into(authorImg);
        val delBtn = holder.delBtn

        delBtn.setOnClickListener {
            Toast.makeText(holder.authorNameTv.context, "Delete Clicked On Item " + position, Toast.LENGTH_SHORT).show()
            val ref = FirebaseDatabase.getInstance().getReference("author/${authors[position].authorID}")
            ref.removeValue()
                .addOnSuccessListener {
                    // Data deleted successfully
                    authors.removeAt(position)
                    this.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    // Handle error
                    println("Error deleting data: ${it.message}")
                }
        }
    }

    override fun getItemCount(): Int = authors.size
}