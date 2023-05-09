package com.example.hmql_ebookapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.*

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = this.arguments
        val book : SampleBook = args?.getSerializable("bookDetail") as SampleBook
        val bookIndex : Int = args.getInt("bookIndex")
        Toast.makeText(requireContext(), "Selected Book Index $bookIndex", Toast.LENGTH_SHORT).show()

        val backBtn = view.findViewById<Button>(R.id.AdminBookDetailBackBtn)
        backBtn.setOnClickListener(){
            requireActivity().supportFragmentManager.popBackStack()
        }

        val bookName = view.findViewById<EditText>(R.id.AdminBookTitleEt)
        val bookAuthor = view.findViewById<EditText>(R.id.AdminBookAuthorEt)
        val bookDesc = view.findViewById<EditText>(R.id.AdminBookDescEt)
        val bookTag = view.findViewById<EditText>(R.id.AdminBookTagsEt)

        bookName.setText(book.bookName)
        bookAuthor.setText(book.authorName)

        setFragmentResultListener("resultFromEdit") { key, bundle ->
            val result = bundle.getString("editResult")
            Toast.makeText(requireContext(), result.toString(), Toast.LENGTH_LONG).show()
            //Toast.makeText(requireContext(), bookDesc.text, Toast.LENGTH_LONG).show()
            bookName.setText(bundle.getString("title"))
            bookAuthor.setText(bundle.getString("author"))
            bookDesc.setText(bundle.getString("desc"))
            bookTag.setText(bundle.getString("tags"))
        }

        val saveBtn = view.findViewById<Button>(R.id.AdminBookSaveBtn)
        saveBtn.setOnClickListener(){
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
            if(newTitle == "" || newAuthor == "") {
                Toast.makeText(requireContext(), "Please fill all information before u save", Toast.LENGTH_SHORT).show()
            }
            else {
                setFragmentResult("editBookInfo",
                    bundleOf("newTitle" to newTitle,
                                    "newAuthor" to newAuthor,
                                    "bookIndex" to bookIndex)
                )
                requireActivity().supportFragmentManager.popBackStack()
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