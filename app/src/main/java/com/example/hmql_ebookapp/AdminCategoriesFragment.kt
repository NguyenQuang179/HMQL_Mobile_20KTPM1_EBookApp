package com.example.hmql_ebookapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AdminCategoriesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminCategoriesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var listOfCategory : ArrayList<Category>
    lateinit var categoryIdList : Array<String>
    lateinit var categoryNameList : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sampleDataInit()
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
        return inflater.inflate(R.layout.fragment_admin_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backBtn = view.findViewById<Button>(R.id.AdminCategoryBackBtn)
        backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val categoriesRv = view.findViewById<RecyclerView>(R.id.AdminCategoryRecyclerView)
        val categoriesRvAdapter = AdminCategoryRecyclerViewAdapter(listOfCategory)
        categoriesRv.layoutManager = LinearLayoutManager(requireContext())
        categoriesRv.adapter = categoriesRvAdapter
        categoriesRvAdapter.onItemClick = {category ->
            Toast.makeText(requireContext(), category.categoryName.toString(), Toast.LENGTH_SHORT).show()
            val bundle = Bundle()
            bundle.putSerializable("categoryDetail", category)
            bundle.putInt("categoryIndex", listOfCategory.indexOf(category))
            val categoryDetailFragment = AdminCategoryDetailFragment()
            categoryDetailFragment.arguments = bundle
            requireActivity().supportFragmentManager.commit {
                replace(R.id.fragmentContainerView2, categoryDetailFragment)
                setReorderingAllowed(true)
                addToBackStack("adminCategoryDetail")
            }
        }

        setFragmentResultListener("editCategory") { key, bundle ->
            val newName = bundle.getString("newName")
            val categoryIndex = bundle.getInt("categoryIndex")
            listOfCategory[categoryIndex].categoryName = newName!!
            categoriesRvAdapter.notifyDataSetChanged()
        }

        val addBtn = view.findViewById<FloatingActionButton>(R.id.AdminCategoryAddBtn)
        addBtn.setOnClickListener {
            requireActivity().supportFragmentManager.commit {
                replace<AdminCategoryAddFragment>(R.id.fragmentContainerView2)
                setReorderingAllowed(true)
                addToBackStack("adminCategoryAdd")
            }
        }
        setFragmentResultListener("addCategory") { key, bundle ->
            val newID = bundle.getString("id")
            val newName = bundle.getString("name")
            val newCategory : Category = Category(newID.toString(), newName.toString())
            listOfCategory.add(newCategory)
            categoriesRvAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdminCategoriesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminCategoriesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun sampleDataInit() {
        listOfCategory = arrayListOf<Category>()

        categoryIdList = arrayOf(
            "0001",
            "0002",
            "0003",
            "0004",
            "0005",
            "0006",
            "0007",
            "0008",
            "0009",
            "0010",
        )

        categoryNameList = arrayOf(
            "Classics",
            "Fantasy",
            "Sci-fi",
            "Adventure",
            "Horror",
            "Novel",
            "Short Story",
            "Mystery",
            "Historical Fiction",
            "Tragedy"
        )

        for(i in categoryIdList.indices) {
            val category = Category(categoryIdList[i], categoryNameList[i])
            listOfCategory.add(category)
        }
    }
}