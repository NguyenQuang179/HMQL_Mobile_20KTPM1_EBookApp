package com.example.hmql_ebookapp

import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

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

        val backBtn = view.findViewById<Button>(R.id.AdminBookBackBtn)
        backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val listOfBook = ArrayList<Book>()
        val ref : DatabaseReference = FirebaseDatabase.getInstance().getReference("book")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val book = child.getValue(Book::class.java)
                        book?.let { listOfBook.add(it) }
                    }
                    Log.d("Books size", "Number of books: ${listOfBook.size}")
                    val booksRv = view.findViewById<RecyclerView>(R.id.AdminBookRv)
                    val booksRvAdapter = AdminBookRecyclerViewAdapter(listOfBook)
                    booksRv.layoutManager = LinearLayoutManager(requireContext())
                    booksRv.adapter = booksRvAdapter
                    booksRvAdapter.onItemClick = { book ->
                        Toast.makeText(requireContext(), book.title, Toast.LENGTH_SHORT).show()
                        val bundle = Bundle()
                        bundle.putString("bookID", book.bookID)
                        bundle.putString("bookIndex", listOfBook.indexOf(book).toString())
                        val bookDetailFragment = AdminBookDetail()
                        bookDetailFragment.arguments = bundle
                        requireActivity().supportFragmentManager.commit {
                            replace(R.id.fragmentContainerView2, bookDetailFragment)
                            setReorderingAllowed(true)
                            addToBackStack("adminBookDetail")
                        }
                    }

                    setFragmentResultListener("editBookInfo") { key, bundle ->
                        val newTitle = bundle.getString("newTitle")
                        val newAuthor = bundle.getString("newAuthor")
                        val bookID = bundle.getString("bookID")
                        val newDesc = bundle.getString("newDesc")
                        var newTag = bundle.getString("newCategoryList")
                        val bookIndex = bundle.getString("bookIndex")?.toInt()

                        listOfBook[bookIndex!!].title = newTitle!!
                        listOfBook[bookIndex].author = newAuthor!!
                        listOfBook[bookIndex].description = newDesc!!
                        newTag = newTag?.replace(", ", ",")
                        val substrings = newTag?.split(",")
                        val listOfCategory = ArrayList<Category>()
                        if (substrings != null) {
                            for (substring in substrings) {
                                val newCategory = Category(substring,"")
                                listOfCategory.add(newCategory)
                            }
                        }
                        listOfBook[bookIndex].categories = listOfCategory
                        val refTemp = FirebaseDatabase.getInstance().getReference("book/${bookID}")
                        refTemp.setValue(listOfBook[bookIndex])
                        booksRvAdapter.notifyDataSetChanged()
                    }

                    setFragmentResultListener("newBookInfo") { key, bundle ->
                        val newTitle = bundle.getString("newTitle")
                        val newAuthor = bundle.getString("newAuthor")
                        val newDesc = bundle.getString("newDesc")
                        var newTag = bundle.getString("newCategoryList")
                        newTag = newTag?.replace(", ", ",")
                        val substrings = newTag?.split(",")
                        val listOfCategory = ArrayList<Category>()
                        if (substrings != null) {
                            for (substring in substrings) {
                                val newCategory = Category(substring,"")
                                listOfCategory.add(newCategory)
                            }
                        }
                        val refTemp = FirebaseDatabase.getInstance().getReference("book")
                        val newChild = refTemp.push()
                        var newBook : Book? = newChild.key?.let { Book(it,newTitle!!, newAuthor!!, newDesc!!, 2002, 0.0, 0,"","", listOfCategory)}
                        newChild.setValue(newBook)
                        listOfBook.add(newBook!!)
                        booksRvAdapter.notifyDataSetChanged()
                    }

                    val addBtn = view.findViewById<FloatingActionButton>(R.id.AdminBookAddBtn)
                    addBtn.setOnClickListener {
                        requireActivity().supportFragmentManager.commit {
                            replace<AdminBookAddFragment>(R.id.fragmentContainerView2)
                            setReorderingAllowed(true)
                            addToBackStack("adminBookAdd")
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "Failed to read value.", error.toException())
            }
        })

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
}