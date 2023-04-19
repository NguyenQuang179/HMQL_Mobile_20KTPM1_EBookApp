package com.example.hmql_ebookapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
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
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var sampleBookList : ArrayList<SampleBook>
    lateinit var bookNameList : Array<String>
    lateinit var authorNameList : Array<String>
    lateinit var bookImgIdList : Array<Int>

    lateinit var popularAuthorNameList : ArrayList<String>
    lateinit var popularAuthorImgList : ArrayList<Int>

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sampleDataInit()

        val favBookRv = view.findViewById<RecyclerView>(R.id.favouriteBooksRv)
        val favBookRvAdapter = FavouriteBookAdapter(sampleBookList)
        val popularAuthorRv = view.findViewById<RecyclerView>(R.id.popularAuthorsRv)
        val popularAuthorRvAdapter = PopularAuthorAdapter(popularAuthorNameList, popularAuthorImgList)
        val avaBtn = view.findViewById<ImageButton>(R.id.avatarIBtn)
        val favBookMoreBtn = view.findViewById<Button>(R.id.favouriteBooksMoreBtn)
        val popularAuthorMoreBtn = view.findViewById<Button>(R.id.popularAuthorsMoreBtn)

        favBookRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        favBookRv.adapter = favBookRvAdapter
        favBookRvAdapter.onItemClick = { book ->
            requireActivity().supportFragmentManager.commit {
                replace<ReadingFragment>(R.id.fragment_container_view)
                setReorderingAllowed(true)
                addToBackStack("readingFragment")
            }
        }

        popularAuthorRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        popularAuthorRv.adapter = popularAuthorRvAdapter
        popularAuthorRvAdapter.onItemClick = { authorName, authorImg ->  
            requireActivity().supportFragmentManager.commit {
                replace<AuthorInfoFragment>(R.id.fragment_container_view)
                setReorderingAllowed(true)
                addToBackStack("authorInfoFragment") // name can be null
            }
        }

        avaBtn!!.setOnClickListener(){
            Toast.makeText(context, "Avatar Button Clicked", Toast.LENGTH_SHORT).show()
        }


        favBookMoreBtn!!.setOnClickListener(){
            Toast.makeText(context, "See More Book Button Clicked", Toast.LENGTH_SHORT).show()
        }


        popularAuthorMoreBtn!!.setOnClickListener(){
            Toast.makeText(context, "See More Author Button Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
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

        // Popular Authors
        popularAuthorNameList = arrayListOf(
            "Alice Schertle",
            "Jill McElmurry",
            "Bret Bais",
            "Liana Moriatory",
            "Neil Giamen",
            "Alice Schertle",
            "Jill McElmurry",
            "Bret Bais",
            "Liana Moriatory",
            "Neil Giamen"
        )

        popularAuthorImgList = arrayListOf(
            R.drawable.sampleauthor1,
            R.drawable.sampleauthor2,
            R.drawable.sampleauthor3,
            R.drawable.sampleauthor1,
            R.drawable.sampleauthor2,
            R.drawable.sampleauthor3,
            R.drawable.sampleauthor1,
            R.drawable.sampleauthor2,
            R.drawable.sampleauthor3,
            R.drawable.sampleauthor1
        )
    }
}