package com.example.hmql_ebookapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AuthorInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthorInfoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var sampleBookList : ArrayList<SampleBook>
    lateinit var bookNameList : Array<String>
    lateinit var authorNameList : Array<String>
    lateinit var bookImgIdList : Array<Int>
    lateinit var authorID: String
    lateinit var authorWorksRv: RecyclerView
    lateinit var authorWorksRvAdapter: FavouriteBookAdapter

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
        return inflater.inflate(R.layout.fragment_author_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            authorID = bundle.getString("authorID").toString()
        }
        sampleDataInit()

        authorWorksRv = view.findViewById<RecyclerView>(R.id.authorWorksRv)
        //val authorWorksRvAdapter = FavouriteBookAdapter(sampleBookList)
        authorWorksRvAdapter = FavouriteBookAdapter(ArrayList<Book>())
        authorWorksRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        authorWorksRv.adapter = authorWorksRvAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AuthorInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AuthorInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun sampleDataInit() {
        val ref2: DatabaseReference = FirebaseDatabase.getInstance().getReference("author/${authorID}")
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val data = snapshot.getValue(Author::class.java)
                    val authorNameInfoTV = view?.findViewById<TextView>(R.id.authorNameInfoTV)
                    authorNameInfoTV!!.text = data!!.name
                    val authorDescriptionTV = view?.findViewById<TextView>(R.id.authorDescriptionTV)
                    authorDescriptionTV!!.text = data.description
                    val authorIV = view?.findViewById<ImageView>(R.id.authorImgInfoIV)
                    if (authorIV != null) {
                        Glide.with(requireContext())
                            .load(data.img)
                            .into(authorIV)
                    }
                    var authorWorkList = ArrayList<Book>()
                    val ref2: DatabaseReference = FirebaseDatabase.getInstance().getReference("book")
                        ref2.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    for (child in snapshot.children) {
                                        val book = child.getValue(Book::class.java)
                                        if (book!!.author == data.name)
                                        {
                                            authorWorkList.add((book))
                                        }

                                    }
                                    Log.d("Books related size", "Number of books: ${authorWorkList.size}")
                                    authorWorkList.sortDescending()
                                    if (authorWorkList.size > 5) authorWorkList = ArrayList(authorWorkList.subList(0, 5))
                                    authorWorksRvAdapter = FavouriteBookAdapter(authorWorkList)
                                    authorWorksRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                                    authorWorksRv.adapter = authorWorksRvAdapter
                                    authorWorksRvAdapter.onItemClick = { book ->
                                        Log.i("In", "Chuyen trang")
                                        val bundle = Bundle()
                                        bundle.putString("bookID", book.bookID)

                                        val fragment = BookIntroductionFragment()
                                        fragment.arguments = bundle

                                        requireActivity().supportFragmentManager.commit {
                                            replace(R.id.fragment_container_view, fragment)
                                            setReorderingAllowed(true)
                                            addToBackStack("BookIntroductionFragment")
                                        }
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}