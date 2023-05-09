package com.example.hmql_ebookapp

import android.media.Rating
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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

        val ReviewViewModel = mList[position]

        // sets the text to the textview from our itemHolder class
        holder.Reviewer_nameTV.text = ReviewViewModel.reviewer_name
        holder.ReviewRatingBar.rating = ReviewViewModel.rating_bar_value
        holder.ReviewRate.text = ReviewViewModel.rating_rate
        holder.ReviewTextTV.text = ReviewViewModel.review_text
        holder.ReviewerIV.setImageResource(ReviewViewModel.reviewer_image)
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
    }
}