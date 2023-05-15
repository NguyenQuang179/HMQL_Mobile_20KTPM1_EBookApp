package com.example.hmql_ebookapp

import android.os.Bundle
import android.util.Log
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
 * Use the [AdminCategoryDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminCategoryDetailFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_admin_category_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = this.arguments
        val category : Category = args?.getSerializable("categoryDetail") as Category
        val categoryIndex : Int = args.getInt("categoryIndex")

        val backBtn = view.findViewById<Button>(R.id.AdminCategoryDetailBackBtn)
        backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        var categoryName = view.findViewById<EditText>(R.id.AdminCategoryDetailNameEt)
        var categoryId = view.findViewById<EditText>(R.id.AdminCategoryDetailIdEt)

        categoryId.setText(category.categoryID)
        categoryName.setText(category.categoryName)
        categoryId.isEnabled = false

        val saveBtn = view.findViewById<Button>(R.id.AdminCategoryDetailSaveBtn)
        saveBtn.setOnClickListener {
            val newName = categoryName.text.toString()
            if(newName == "") {
                Toast.makeText(requireContext(), "Please fill all information before u save", Toast.LENGTH_SHORT).show()
            }
            else {
                Log.i("index 1", categoryIndex.toString())
                Log.i("index 2", newName)
                setFragmentResult("editCategory",
                    bundleOf("newName" to newName,
                                    "categoryIndex" to categoryIndex)
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
         * @return A new instance of fragment AdminCategoryDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminCategoryDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}