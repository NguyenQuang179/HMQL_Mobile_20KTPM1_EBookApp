package com.example.hmql_ebookapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdminBooksFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminBooksFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var sampleBookList : ArrayList<SampleBook>
    lateinit var bookNameList : Array<String>
    lateinit var authorNameList : Array<String>
    lateinit var bookImgIdList : Array<Int>

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
        return inflater.inflate(R.layout.fragment_admin_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sampleDataInit()

        val booksRv = view.findViewById<RecyclerView>(R.id.AdminBookRv)
        val booksRvAdapter = AdminBookRecyclerViewAdapter(sampleBookList)
        booksRv.layoutManager = LinearLayoutManager(requireContext())
        booksRv.adapter = booksRvAdapter
        booksRvAdapter.onItemClick = { book ->
            Toast.makeText(requireContext(), book.bookName.toString(), Toast.LENGTH_SHORT).show()
            val bundle = Bundle()
            bundle.putSerializable("bookDetail", book)
            val bookDetailFragment = AdminBookDetail()
            bookDetailFragment.arguments = bundle
            requireActivity().supportFragmentManager.commit {
                replace(R.id.fragmentContainerView2, bookDetailFragment)
                setReorderingAllowed(true)
                addToBackStack("adminBookDetail")
            }
        }

        val backBtn = view.findViewById<Button>(R.id.AdminBookBackBtn)
        backBtn.setOnClickListener(){
            requireActivity().supportFragmentManager.popBackStack()
        }

        setFragmentResultListener("newBookInfo") { key, bundle ->
            val newTitle = bundle.getString("title")
            val newAuthor = bundle.getString("author")
            Toast.makeText(requireContext(), "$newTitle $newAuthor", Toast.LENGTH_SHORT ).show()
            var newBook : SampleBook = SampleBook(newTitle.toString(), newAuthor.toString(), R.drawable.favbookimg2)
            sampleBookList.add(newBook)
            booksRvAdapter.notifyDataSetChanged()
        }

        val addBtn = view.findViewById<FloatingActionButton>(R.id.AdminBookAddBtn)
        addBtn.setOnClickListener(){
            requireActivity().supportFragmentManager.commit {
                replace<AdminBookAddFragment>(R.id.fragmentContainerView2)
                setReorderingAllowed(true)
                addToBackStack("adminBookAdd")
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
         * @return A new instance of fragment AdminBooksFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminBooksFragment().apply {
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
    }
}