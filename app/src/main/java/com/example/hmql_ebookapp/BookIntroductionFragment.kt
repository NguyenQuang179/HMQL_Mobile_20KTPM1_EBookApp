package com.example.hmql_ebookapp

import android.app.Activity
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BookIntroductionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@Suppress("DEPRECATION")
class BookIntroductionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var sampleBookList : ArrayList<SampleBook>
    lateinit var bookNameList : Array<String>
    lateinit var authorNameList : Array<String>
    lateinit var bookImgIdList : Array<Int>

    private fun sampleDataInit() {
        // Favourite Books
        sampleBookList = arrayListOf<SampleBook>()

        bookNameList = arrayOf(
            "Born a crime: Stories from a S...",
            "Merry Christmas",
            "Little Blue Truck's Halloween",
            "Born a crime: Stories from a S...",
            "Merry Christmas",
            "Little Blue Truck's Halloween",
            "Born a crime: Stories from a S...",
            "Merry Christmas",
            "Little Blue Truck's Halloween",
            "Born a crime: Stories from a S..."
        )

        authorNameList = arrayOf(
            "Alice Schertle, Jill McElmurry",
            "Alice Schertle",
            "Jill McElmurry",
            "Bret Bais",
            "Liana Moriatory",
            "Alice Schertle, Jill McElmurry",
            "Alice Schertle",
            "Jill McElmurry",
            "Bret Bais",
            "Liana Moriatory"
        )

        bookImgIdList = arrayOf(
            R.drawable.favbookimg1,
            R.drawable.favbookimg2,
            R.drawable.favbookimg3,
            R.drawable.favbookimg1,
            R.drawable.favbookimg2,
            R.drawable.favbookimg3,
            R.drawable.favbookimg1,
            R.drawable.favbookimg2,
            R.drawable.favbookimg3,
            R.drawable.favbookimg1
        )

        for(i in bookNameList.indices) {
            val sampleBook = SampleBook(bookNameList[i], authorNameList[i], bookImgIdList[i])
            sampleBookList.add(sampleBook)
        }
    }

    private  lateinit var sampleReviewList : ArrayList<ReviewViewModel>
    lateinit var reviewerImageList : Array<Int>
    lateinit var reviewerNameList : Array<String>
    lateinit var reviewRatingValueList : Array<Float>
    lateinit var reviewRateList : Array<String>
    lateinit var reviewTextList : Array<String>
    lateinit var ReviewRV : RecyclerView
    private fun sampleReviewInit() {
        // Favourite Books
        sampleReviewList = arrayListOf<ReviewViewModel>()


        reviewerNameList = arrayOf(
            "Luu Hoang Minh",
            "Truong Gia Huy",
            "Nguyen Ngoc Quang",
            "Ha Tuan Lam"
        )

        reviewerImageList = arrayOf(
            R.drawable.sampleauthor1,
            R.drawable.sampleauthor2,
            R.drawable.sampleauthor3,
            R.drawable.sampleauthor1
        )

        reviewRatingValueList = arrayOf(
            5.0f,
            4.0f,
            2.5f,
            1.5f
        )

        reviewRateList = arrayOf(
            "5.0",
            "4.0",
            "2.5",
            "1.5"
        )

        reviewTextList = arrayOf(
            "This book is amazing",
            "This book is shit",
            "This book change my life, I am now a millionaire",
            "This book makes me change my way of life"
        )

        for(i in reviewerNameList.indices) {
            val sampleReview = ReviewViewModel(reviewerImageList[i], reviewerNameList[i], reviewRatingValueList[i], reviewRateList[i],reviewTextList[i] )
            sampleReviewList.add(sampleReview)
        }
    }

    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    val REQUEST_CODE = 123

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_book_introduction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sampleDataInit()
        sampleReviewInit()



        val TagsRV = view.findViewById<RecyclerView>(R.id.TagsRV)
        val ChaptersRV = view.findViewById<RecyclerView>(R.id.ChaptersRV)
        val RecommendationBooksRV = view.findViewById<RecyclerView>(R.id.RecommendationBooksRV)
        ReviewRV = view.findViewById<RecyclerView>(R.id.ReviewRV)
        val SeeMoreRecBtn = view.findViewById<Button>(R.id.SeeMoreRecBtn)
        val AddReviewButton = view.findViewById<Button>(R.id.WriteReviewBtn)

        val ReadButton = view.findViewById<Button>(R.id.ReadBtn)

        val BackButton = view.findViewById<ImageButton>(R.id.backBtn)
        val LikedButton = view.findViewById<ImageButton>(R.id.LikeBtn)
        val DownloadButton = view.findViewById<ImageButton>(R.id.downloadBtn)

        LikedButton.tag = "bookmark"


        // this creates a vertical layout Manager
        TagsRV.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        ChaptersRV.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        ReviewRV.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        RecommendationBooksRV.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)


        // ArrayList of class ItemsViewModel
        val data_tags = ArrayList<TagsViewModel>()
        val data_chapters = ArrayList<ChapterViewModel>()


        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..20) {
            data_tags.add(TagsViewModel("Tags " + i))
        }

        for (i in 1..20) {
            data_chapters.add(ChapterViewModel("Ch. " + i, "Chapter name " + i))
        }

        // This will pass the ArrayList to our Adapter
        val adapter_tags = TagsAdapterClass(data_tags)
        val adapter_chapters = ChapterAdapterClass(data_chapters)
        val adapter_recommendations = RecommendationAdapter(sampleBookList)
        val adapter_review = ReviewAdapterClass(sampleReviewList)

        // Setting the Adapter with the recyclerview
        TagsRV.adapter = adapter_tags
        ChaptersRV.adapter = adapter_chapters
        RecommendationBooksRV.adapter = adapter_recommendations
        ReviewRV.adapter = adapter_review

        SeeMoreRecBtn!!.setOnClickListener() {
            Toast.makeText(this.context, "See More Book Button Clicked", Toast.LENGTH_SHORT).show()
        }

        LikedButton.setOnClickListener {
            if (LikedButton.tag == "bookmark") {
                LikedButton.setImageResource(R.drawable.bookmark_solid)
                LikedButton.tag = "bookmarked"
            } else {
                LikedButton.setImageResource(R.drawable.bookmark_regular)
                LikedButton.tag = "bookmark"
            }
        }

        ReadButton.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace<ReadingFragment>(R.id.fragment_container_view)
                setReorderingAllowed(true)
                addToBackStack("readFragment")
            }
        }

        DownloadButton.setOnClickListener{
            Toast.makeText(this.context, "Downloading!", Toast.LENGTH_SHORT).show()
        }

        BackButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        AddReviewButton.setOnClickListener {
//            Toast.makeText(this,"click me button clicked", Toast.LENGTH_LONG).show()
//            val activity = view.context.applicationContext as AppCompatActivity
            val reviewPopUp = ReviewPopupFragment()
            reviewPopUp.setTargetFragment(this, REQUEST_CODE)
            reviewPopUp.show((this.context as AppCompatActivity).supportFragmentManager, "reviewPopup")

        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val review_text = data!!.extras!!.getString("REVIEW")
            val rating_value = data.extras!!.getString("RATING")
            val rating_bar_value = rating_value + "f"
            Log.d("REVIEW", review_text.toString())
            Log.d("RATING", rating_value.toString())

            val new_review = ReviewViewModel(R.drawable.sampleauthor3, "New User", rating_bar_value.toFloat(), rating_value.toString(), review_text.toString() )

            sampleReviewList.add(0, new_review)

            ReviewRV.adapter?.notifyDataSetChanged()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BookIntroductionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BookIntroductionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}