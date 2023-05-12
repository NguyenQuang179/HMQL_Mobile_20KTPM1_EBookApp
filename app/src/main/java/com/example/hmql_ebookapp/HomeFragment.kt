package com.example.hmql_ebookapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

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

    private lateinit var bookList : ArrayList<Book>


    lateinit var popularAuthorList : ArrayList<Author>

    lateinit var favBookRv: RecyclerView
    var favBookRvAdapter = FavouriteBookAdapter(ArrayList<Book>())
    lateinit var popularAuthorRv: RecyclerView
    var popularAuthorRvAdapter = PopularAuthorAdapter(ArrayList<Author>())

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

    fun sendViewToBack(child: View) {
        val parent = child.parent as ViewGroup
        if (null != parent) {
            parent.removeView(child)
            parent.addView(child, 0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sampleDataInit()

        favBookRv = view.findViewById<RecyclerView>(R.id.favouriteBooksRv)
        popularAuthorRv = view.findViewById<RecyclerView>(R.id.popularAuthorsRv)
        val avaBtn = view.findViewById<ImageButton>(R.id.avatarIBtn)
        val favBookMoreBtn = view.findViewById<Button>(R.id.favouriteBooksMoreBtn)
        val popularAuthorMoreBtn = view.findViewById<Button>(R.id.popularAuthorsMoreBtn)

//        favBookRvAdapter.onItemClick = { book ->
//            requireActivity().supportFragmentManager.commit {
//                replace<ReadingFragment>(R.id.fragment_container_view)
//                setReorderingAllowed(true)
//                addToBackStack("readingFragment")
//            }
////            var intent = Intent(this.requireContext(), BookIntroductionActivity::class.java)
////            startActivity(intent)
//        }

        popularAuthorRvAdapter.onItemClick = { author ->
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
        bookList = ArrayList<Book>()
        val ref1: DatabaseReference = FirebaseDatabase.getInstance().getReference("book")
        ref1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val book = child.getValue(Book::class.java)
                        book?.let { bookList.add(it) }
                    }
                    Log.d("Books size", "Number of books: ${bookList.size}")
                    bookList.sortDescending();
                    bookList = ArrayList(bookList.subList(0, 5))
                    favBookRvAdapter = FavouriteBookAdapter(bookList)
                    favBookRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    favBookRv.adapter = favBookRvAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "Failed to read value.", error.toException())
            }
        })

        popularAuthorList = ArrayList<Author>()
        val ref2: DatabaseReference = FirebaseDatabase.getInstance().getReference("author")
        ref2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val author = child.getValue(Author::class.java)
                        author?.let { popularAuthorList.add(it) }
                    }
                    Log.d("Books size", "Number of books: ${bookList.size}")
                    popularAuthorList = ArrayList(popularAuthorList.subList(0, 5))
                    popularAuthorRvAdapter = PopularAuthorAdapter(popularAuthorList)
                    popularAuthorRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    popularAuthorRv.adapter = popularAuthorRvAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "Failed to read value.", error.toException())
            }
        })

    }
}