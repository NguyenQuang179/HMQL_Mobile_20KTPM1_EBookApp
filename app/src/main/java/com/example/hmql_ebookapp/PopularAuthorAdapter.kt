package com.example.hmql_ebookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PopularAuthorAdapter(private val authorNameList : ArrayList<String>, private val authorImgList : ArrayList<Int>)
    : RecyclerView.Adapter<PopularAuthorAdapter.ViewHolder>(){

    var onItemClick: ((authorName : String, authorImg : Int) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val authorNameTv : TextView = listItemView.findViewById<TextView>(R.id.authorNameTv)!!
        val authorImgView : ImageView = listItemView.findViewById<ImageView>(R.id.authorImgView)!!
        init { listItemView.setOnClickListener { onItemClick?.invoke(authorNameList[adapterPosition], authorImgList[adapterPosition]) } }
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
        authorNameTv.setText(authorNameList[position])
        val authorImgView = holder.authorImgView
        authorImgView.setImageResource(authorImgList[position])
    }

    override fun getItemCount(): Int = authorNameList.size
}