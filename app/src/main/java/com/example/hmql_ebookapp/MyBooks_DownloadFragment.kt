package com.example.hmql_ebookapp

import android.os.Bundle
import android.util.Log
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
 * Use the [MyBooks_DownloadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyBooks_DownloadFragment : Fragment() {
    lateinit var books: List<UserBook>


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
        return inflater.inflate(R.layout.fragment_my_books__download, container, false)
    }

    fun getDownloadedBooks(user: User): List<UserBook> {
        val books = mutableListOf<UserBook>()
        user.listOfBooks.forEach {
            if (it.downloaded == true) {
                books.add(it)
            }
        }
        return books
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        val user = userViewModel.user
        if (user != null) {
            books = getDownloadedBooks(user)
        }
        val myBookRv = view.findViewById<RecyclerView>(R.id.downloadBooksRecyclerView)
        val myBookRvAdapter = MyBookAdapter(books)


        myBookRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        myBookRv.adapter = myBookRvAdapter

        myBookRvAdapter.onItemClick = { book ->
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
    companion object {
        var books = ArrayList<SampleBook>()
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyBooks_DownloadFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyBooks_DownloadFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}