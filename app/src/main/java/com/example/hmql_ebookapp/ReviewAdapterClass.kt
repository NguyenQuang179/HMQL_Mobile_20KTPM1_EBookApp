package com.example.hmql_ebookapp

import android.media.Rating
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ReviewAdapterClass(private val mList: List<ReviewViewModel>) : RecyclerView.Adapter<ReviewAdapterClass.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_list_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val review = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.Reviewer_nameTV.text = review.reviewer_name
        holder.ReviewRatingBar.rating = review.rating_bar_value
        holder.ReviewRate.text = review.rating_rate
        holder.ReviewTextTV.text = review.review_text
        holder.ReviewDateTV.text = review.review_date
        Glide.with(holder.ReviewerIV.context)
            .load(review.reviewer_avatar)
            .into(holder.ReviewerIV)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val Reviewer_nameTV : TextView = itemView.findViewById(R.id.Reviewer_nameTV)
        val ReviewRatingBar : RatingBar = itemView.findViewById(R.id.ReviewRatingBar)
        val ReviewerIV : ImageView= itemView.findViewById(R.id.ReviewerIV)
        val ReviewRate : TextView = itemView.findViewById(R.id.ReviewRate)
        val ReviewTextTV : TextView = itemView.findViewById(R.id.ReviewTextTV)
        val ReviewDateTV : TextView = itemView.findViewById(R.id.ReviewDateTV)
    }
}