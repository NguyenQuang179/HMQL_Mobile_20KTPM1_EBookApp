package com.example.hmql_ebookapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

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

        val TagsRV = view.findViewById<RecyclerView>(R.id.TagsRV)
        val ChaptersRV = view.findViewById<RecyclerView>(R.id.ChaptersRV)
        val RecommendationBooksRV = view.findViewById<RecyclerView>(R.id.RecommendationBooksRV)
        val SeeMoreRecBtn = view.findViewById<Button>(R.id.SeeMoreRecBtn)
        val LikedButton = view.findViewById<ImageButton>(R.id.LikeBtn)
        LikedButton.tag = "bookmark"

        // this creates a vertical layout Manager
        TagsRV.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        ChaptersRV.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
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

        // Setting the Adapter with the recyclerview
        TagsRV.adapter = adapter_tags
        ChaptersRV.adapter = adapter_chapters
        RecommendationBooksRV.adapter = adapter_recommendations

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