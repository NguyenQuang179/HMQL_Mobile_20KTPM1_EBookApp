package com.example.hmql_ebookapp

import android.app.Activity
import android.content.Intent
import android.media.Rating
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReviewPopupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@Suppress("DEPRECATION")
class ReviewPopupFragment : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO: code function for rating
        val cancelButton = view.findViewById<Button>(R.id.cancel_button)
        val submitButton = view.findViewById<Button>(R.id.submit_button)
        val reviewRatingBar = view.findViewById<RatingBar>(R.id.review_ratingBar)
        val reviewText = view.findViewById<EditText>(R.id.review_text)

        cancelButton.setOnClickListener {
            dismiss()
        }

        submitButton.setOnClickListener {
            val rating = reviewRatingBar.rating.toString()
            val review = reviewText.text.toString()
            val bundle = Bundle()
            bundle.putString("RATING", rating)
            bundle.putString("REVIEW", review)

            val intent = Intent().putExtras(bundle)

            targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, intent)

            Toast.makeText(this.context, "Submitted review!", Toast.LENGTH_SHORT).show()
//            Log.d("REVIEW", bundle.toString())
            dismiss()
        }
    }
}