package com.example.hmql_ebookapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import com.bumptech.glide.Glide
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdminBookDetail.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminBookDetail : Fragment() {
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
        return inflater.inflate(R.layout.fragment_admin_book_detail, container, false)
    }

    lateinit var bookID: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Detail", "True")
        val args = this.arguments
        bookID = args?.getString("bookID").toString()
        var bookIndex: String? = args?.getString("bookIndex")
        Toast.makeText(requireContext(), "Selected Book Index $bookID", Toast.LENGTH_SHORT).show()

        val backBtn = view.findViewById<Button>(R.id.AdminBookDetailBackBtn)
        backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        var book: Book

        val ref2: DatabaseReference = FirebaseDatabase.getInstance().getReference("book/${bookID}")
        ref2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    book = snapshot.getValue(Book::class.java)!!
                    val bookName = view.findViewById<EditText>(R.id.AdminBookTitleEt)
                    val bookAuthor = view.findViewById<EditText>(R.id.AdminBookAuthorEt)
                    val bookDesc = view.findViewById<EditText>(R.id.AdminBookDescEt)
                    val bookTag = view.findViewById<EditText>(R.id.AdminBookTagsEt)

                    bookName.setText(book.title)
                    bookAuthor.setText(book.author)
                    bookDesc.setText(book.description)
                    var categoryString: String = ""
                    for (child in book.categories){
                        if (categoryString != "")
                            categoryString += ", ${child.categoryName}"
                        else categoryString = child.categoryName
                    }
                    bookTag.setText(categoryString)

//                    setFragmentResultListener("resultFromEdit") { key, bundle ->
//                        val result = bundle.getString("editResult")
//                        Toast.makeText(requireContext(), result.toString(), Toast.LENGTH_LONG).show()
//                        //Toast.makeText(requireContext(), bookDesc.text, Toast.LENGTH_LONG).show()
//                        bookName.setText(bundle.getString("title"))
//                        bookAuthor.setText(bundle.getString("author"))
//                        bookDesc.setText(bundle.getString("desc"))
//                        bookTag.setText(bundle.getString("tags"))
//                    }

                    val saveBtn = view.findViewById<Button>(R.id.AdminBookSaveBtn)
                    saveBtn.setOnClickListener {
            //            val data = "Sending Data From AdminBookDetail"
            //            val bundle = Bundle()
            //            bundle.putString("dataFromBookDetail", data)
            //            val bookEditFragment = AdminBookEditFragment()
            //            bookEditFragment.arguments = bundle
            //            //fragmentManager?.beginTransaction()?.replace(R.id.fragmentContainerView2, bookEditFragment)?.commit()
            //            requireActivity().supportFragmentManager.commit {
            //                replace(R.id.fragmentContainerView2, bookEditFragment)
            //                setReorderingAllowed(true)
            //                addToBackStack("EditBook")
            //            }
                        val newTitle = bookName.text.toString()
                        val newAuthor = bookAuthor.text.toString()
                        val newCategoryList = bookTag.text.toString()
                        val newDesc = bookDesc.text.toString()
                        if(newTitle == "" || newAuthor == "") {
                            Toast.makeText(requireContext(), "Please fill all information before u save", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            setFragmentResult("editBookInfo",
                                bundleOf("newTitle" to newTitle,
                                                "newAuthor" to newAuthor,
                                                "bookID" to bookID,
                                                "newCategoryList" to newCategoryList,
                                                "newDesc" to newDesc,
                                                "bookIndex" to bookIndex
                                )
                            )
                            requireActivity().supportFragmentManager.popBackStack()
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
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
         * @return A new instance of fragment AdminBookDetail.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminBookDetail().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}