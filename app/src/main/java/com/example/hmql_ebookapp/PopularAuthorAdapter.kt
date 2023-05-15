package com.example.hmql_ebookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PopularAuthorAdapter(private val authorList : ArrayList<Author>)
    : RecyclerView.Adapter<PopularAuthorAdapter.ViewHolder>(){

    var onItemClick: ((Author) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val authorNameTv : TextView = listItemView.findViewById<TextView>(R.id.authorNameTv)!!
        val authorImgView : ImageView = listItemView.findViewById<ImageView>(R.id.authorImgView)!!
        init { listItemView.setOnClickListener { onItemClick?.invoke(authorList[adapterPosition]) } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularAuthorAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val favBooksView : View = inflater.inflate(R.layout.popular_author_layout, parent, false)
        return ViewHolder(favBooksView)
    }

    override fun onBindViewHolder(holder: PopularAuthorAdapter.ViewHolder, position: Int) {
        val authorNameTv = holder.authorNameTv
        authorNameTv.text = authorList[position].name
        val authorImgView = holder.authorImgView
        Glide.with(holder.authorImgView.context)
            .load(authorList[position].img)
            .into(authorImgView)
    }

    override fun getItemCount(): Int = authorList.size
}