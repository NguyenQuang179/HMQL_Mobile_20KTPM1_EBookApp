package com.example.hmql_ebookapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


@Suppress("DEPRECATION")
class BookIntroductionActivity : AppCompatActivity() {
    private lateinit var sampleBookList : ArrayList<SampleBook>
    lateinit var bookNameList : Array<String>
    lateinit var authorNameList : Array<String>
    lateinit var bookImgIdList : Array<Int>

    private  lateinit var sampleReviewList : ArrayList<ReviewViewModel>
    lateinit var reviewerImageList : Array<Int>
    lateinit var reviewerNameList : Array<String>
    lateinit var reviewRatingValueList : Array<Float>
    lateinit var reviewRateList : Array<String>
    lateinit var reviewTextList : Array<String>

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

        for (i in bookNameList.indices) {
            val sampleBook = SampleBook(bookNameList[i], authorNameList[i], bookImgIdList[i])
            sampleBookList.add(sampleBook)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_introduction)
        sampleDataInit()
        sampleReviewInit()



//        if (savedInstanceState == null) { // initial transaction should be wrapped like this
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.readlikebtn_fragmentCV, BookIntroduction_ReadFavoriteFragment())
//                .commitAllowingStateLoss()
//        }

        val TagsRV = findViewById<RecyclerView>(R.id.TagsRV)
        val ChaptersRV = findViewById<RecyclerView>(R.id.ChaptersRV)
        val RecommendationBooksRV = findViewById<RecyclerView>(R.id.RecommendationBooksRV)
        val ReviewRV = findViewById<RecyclerView>(R.id.ReviewRV)
        val SeeMoreRecBtn = findViewById<Button>(R.id.SeeMoreRecBtn)
        val LikedButton = findViewById<ImageButton>(R.id.LikeBtn)
        val AddReviewButton = findViewById<Button>(R.id.WriteReviewBtn)

        LikedButton.tag = "bookmark"


        // this creates a vertical layout Manager
        TagsRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        ChaptersRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        ReviewRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        RecommendationBooksRV.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


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

        AddReviewButton.setOnClickListener {
//            Toast.makeText(this,"click me button clicked", Toast.LENGTH_LONG).show()

            val reviewPopUp = ReviewPopupFragment()
//            reviewPopUp.(this,0)
            reviewPopUp.show((this as AppCompatActivity).supportFragmentManager, "reviewPopup")
        }

        SeeMoreRecBtn!!.setOnClickListener() {
            Toast.makeText(this, "See More Book Button Clicked", Toast.LENGTH_SHORT).show()
        }

        LikedButton.setOnClickListener {
            if (LikedButton.tag == "bookmark") {
                LikedButton.setImageResource(R.drawable.bookmark_solid_light)
                LikedButton.tag = "bookmarked"
            } else {
                LikedButton.setImageResource(R.drawable.bookmark_regular_light)
                LikedButton.tag = "bookmark"
            }
        }

    }


}