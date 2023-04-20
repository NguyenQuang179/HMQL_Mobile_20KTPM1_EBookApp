package com.example.hmql_ebookapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class BookIntroductionActivity : AppCompatActivity() {
    private lateinit var sampleBookList : ArrayList<SampleBook>
    lateinit var bookNameList : Array<String>
    lateinit var authorNameList : Array<String>
    lateinit var bookImgIdList : Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_introduction)
        sampleDataInit()

        val TagsRV = findViewById<RecyclerView>(R.id.TagsRV)
        val ChaptersRV = findViewById<RecyclerView>(R.id.ChaptersRV)
        val RecommendationBooksRV = findViewById<RecyclerView>(R.id.RecommendationBooksRV)
        val SeeMoreRecBtn = findViewById<Button>(R.id.SeeMoreRecBtn)


        // this creates a vertical layout Manager
        TagsRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        ChaptersRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        RecommendationBooksRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


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

        // Setting the Adapter with the recyclerview
        TagsRV.adapter = adapter_tags
        ChaptersRV.adapter = adapter_chapters
        RecommendationBooksRV.adapter = adapter_recommendations


        SeeMoreRecBtn!!.setOnClickListener(){
            Toast.makeText(this, "See More Book Button Clicked", Toast.LENGTH_SHORT).show()
        }

        supportFragmentManager.commit {
//            add<HomeFragment>(R.id.fragment_book_intro_purchasebtn)
//            setReorderingAllowed(true)
//            addToBackStack("name") // name can be null
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

        for(i in bookNameList.indices) {
            val sampleBook = SampleBook(bookNameList[i], authorNameList[i], bookImgIdList[i])
            sampleBookList.add(sampleBook)
        }
    }

}