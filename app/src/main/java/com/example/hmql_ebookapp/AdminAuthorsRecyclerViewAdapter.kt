package com.example.hmql_ebookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

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
        authorImg.setImageResource(author.img.toInt())
        val delBtn = holder.delBtn

        delBtn.setOnClickListener {
            Toast.makeText(holder.authorNameTv.context, "Delete Clicked On Item " + position, Toast.LENGTH_SHORT).show()
            authors.removeAt(position)
            this.notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = authors.size
}