package com.example.hmql_ebookapp

import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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

    lateinit var categoryIdList : Array<String>
    lateinit var categoryNameList : Array<String>

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
        return inflater.inflate(R.layout.fragment_admin_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backBtn = view.findViewById<Button>(R.id.AdminCategoryBackBtn)
        backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val listOfCategory = ArrayList<Category>()
        val ref = FirebaseDatabase.getInstance().getReference("category")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val category = child.getValue(Category::class.java)
                        category?.let { listOfCategory.add(it) }
                    }
                    Log.d("Books size", "Number of books: ${listOfCategory.size}")
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
                        Log.i("before", listOfCategory.size.toString())
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
                        val refTemp = FirebaseDatabase.getInstance().getReference("category/${listOfCategory[categoryIndex!!].categoryID}");
                        refTemp.setValue(listOfCategory[categoryIndex!!])
                        categoriesRvAdapter.notifyDataSetChanged()
                    }

                        // val addBtn = view.findViewById<FloatingActionButton>(R.id.AdminCategoryAddBtn)
                        // addBtn.setOnClickListener {
                        // requireActivity().supportFragmentManager.commit {
                        // replace<AdminCategoryAddFragment>(R.id.fragmentContainerView2)
                        // setReorderingAllowed(true)
                        // addToBackStack("adminCategoryAdd")
                        
                    val addBtn = view.findViewById<FloatingActionButton>(R.id.AdminCategoryAddBtn)
                    addBtn.setOnClickListener() {
                        requireActivity().supportFragmentManager.commit {
                            replace<AdminCategoryAddFragment>(R.id.fragmentContainerView2)
                            setReorderingAllowed(true)
                            addToBackStack("adminCategoryAdd")
                        }
                    }
                    setFragmentResultListener("addCategory") { key, bundle ->
                        val newName = bundle.getString("name")
                        val refTemp = FirebaseDatabase.getInstance().getReference("category");
                        val newChild = refTemp.push()
                        val newCategory : Category? = newChild.key?.let { Category(newName.toString(), it) }
                        newChild.setValue(newCategory)
                        if (newCategory != null) {
                            listOfCategory.add(newCategory)
                        }
                        categoriesRvAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "Failed to read value.", error.toException())
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
}