package com.example.hmql_ebookapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyBooks_HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyBooks_HistoryFragment : Fragment() {
    lateinit var books: List<UserBook>


//    constructor(books: ArrayList<SampleBook>) : this() {
//        this.books = books
//    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_my_books__history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        val user = userViewModel.user
        if (user != null) {
            books = user.listOfBooks
        }
        val myBookRv = view.findViewById<RecyclerView>(R.id.historyBooksRecyclerView)
        val myBookRvAdapter = MyBookAdapter(books)


        myBookRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        myBookRv.adapter = myBookRvAdapter

        myBookRvAdapter.onItemClick = { book ->
            requireActivity().supportFragmentManager.commit {
                replace<BookIntroductionFragment>(R.id.fragment_container_view)
                setReorderingAllowed(true)
                addToBackStack("bookIntroductionFragment") // name can be null
            }
        }
    }

    companion object {
        var books = ArrayList<SampleBook>()
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyBooks_HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyBooks_HistoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}