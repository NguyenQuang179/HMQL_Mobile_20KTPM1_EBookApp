package com.example.hmql_ebookapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdminBookAddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminBookAddFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_admin_book_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookTitleEt = view.findViewById<EditText>(R.id.AdminBookAddTitleEt)
        val bookAuthorEt = view.findViewById<EditText>(R.id.AdminBookAddAuthorEt)
        val AdminBookAddDescEt = view.findViewById<EditText>(R.id.AdminBookAddDescEt)
        val AdminBookAddTagsEt = view.findViewById<EditText>(R.id.AdminBookAddTagsEt)
        val saveBtn = view.findViewById<Button>(R.id.AdminBookAddSaveBtn)
        saveBtn.setOnClickListener {
            val title : String = bookTitleEt.text.toString()
            val author : String = bookAuthorEt.text.toString()
            val newCategoryList: String = AdminBookAddTagsEt.text.toString()
            val newDesc: String = AdminBookAddDescEt.text.toString()

            if(title == "" || author == "") {
                Toast.makeText(requireContext(), "Please fill all information before u save", Toast.LENGTH_SHORT).show()
            }
            else {
                setFragmentResult("newBookInfo",
                    bundleOf("newTitle" to title,
                        "newAuthor" to author,
                        "newCategoryList" to newCategoryList,
                        "newDesc" to newDesc
                    )
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
         * @return A new instance of fragment AdminBookAddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminBookAddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}