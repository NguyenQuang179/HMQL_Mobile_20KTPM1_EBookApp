package com.example.hmql_ebookapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
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
 * Use the [SearchResultFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchResultFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var books : ArrayList<Book>
    lateinit var searchString: String
    var categoryChoiceList = ArrayList<String>()
    lateinit var customRecyclerView: RecyclerView;
    lateinit var adapter: MyFilteredBookAdapter;
    lateinit var customRecyclerView2: RecyclerView
    var categoryList = ArrayList<Category>()
    var adapter2 = MyRecyclerViewForCategoryChoice(ArrayList(), null)
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
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            searchString = bundle.getString("searchString").toString()
            categoryChoiceList = bundle.getStringArrayList("choiceList") as ArrayList<String>;
            for (category in categoryChoiceList) {
                Log.i("Choice", category)
            }
        }
        customRecyclerView2 = view.findViewById<RecyclerView>(R.id.categoryChoiceRV)

        customRecyclerView = view.findViewById<RecyclerView>(R.id.searchResultRV)
        sampleDataInit()

        var autoCompleteTV = view.findViewById<AutoCompleteTextView>(R.id.searchResultAutoCompleteTextView)
        autoCompleteTV.setText(searchString)

        autoCompleteTV!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                adapter.filter?.filter(p0)
                adapter.notifyDataSetChanged()
                //customRecyclerView!!.adapter = adapter
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
         * @return A new instance of fragment SearchResultFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun sampleDataInit() {
        books = ArrayList<Book>()
        val ref1: DatabaseReference = FirebaseDatabase.getInstance().getReference("book")
        ref1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val book = child.getValue(Book::class.java)
                        book?.let { books.add(it) }
                    }
                    Log.d("Books size", "Number of books: ${books.size}")
                    adapter = MyFilteredBookAdapter(books, categoryChoiceList)
                    customRecyclerView!!.adapter = adapter
                    adapter.onItemClick = { book ->
                        requireActivity().supportFragmentManager.commit {
                            replace<BookIntroductionFragment>(R.id.fragment_container_view)
                            setReorderingAllowed(true)
                            addToBackStack("bookIntroductionFragment")
                        }
                    }
                    adapter.filter.filter(searchString)
                    adapter.notifyDataSetChanged();
                    adapter2.setAdapter2(adapter)
                    adapter2.notifyDataSetChanged()
                    val layoutManager = LinearLayoutManager(context)
                    customRecyclerView.layoutManager = layoutManager
                    val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(context,
                        DividerItemDecoration.VERTICAL)
                    customRecyclerView.addItemDecoration(itemDecoration)
                    adapter.onItemClick = { book ->
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
                Log.e("Error", "Failed to read value.", error.toException())
            }
        })

        val ref2: DatabaseReference = FirebaseDatabase.getInstance().getReference("category")
        ref2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val category = child.getValue(Category::class.java)
                        category?.let { categoryList.add(it) }
                    }
                    Log.d("Books size", "Number of books: ${books.size}")
                    adapter2 = MyRecyclerViewForCategoryChoice(categoryList, adapter)
                    customRecyclerView2!!.adapter = adapter2
                    adapter2.setCategoryChoiceList(categoryChoiceList)
                    val layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    customRecyclerView2.layoutManager = layoutManager
                    adapter2.onItemClick = { category ->
                        Log.i("Click", "HEllo")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "Failed to read value.", error.toException())
            }
        })
    }
}