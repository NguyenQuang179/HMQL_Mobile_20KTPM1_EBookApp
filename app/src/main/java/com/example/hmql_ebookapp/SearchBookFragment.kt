package com.example.hmql_ebookapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchBookFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchBookFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_search_book, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listOfString = resources.getStringArray(R.array.recentSearch);
        var customRecyclerView = view.findViewById<RecyclerView>(R.id.recentSearchRecyclerView)
        var adapter = MyRecyclerViewForRecentSearchs(listOfString)
        adapter.onItemClick = { item ->
// do something with your item
            Toast.makeText (context, "Row clicekd: $item", Toast.LENGTH_SHORT).show()
        }
        adapter.onButtonClick = { item ->
// do something with your item
            Toast.makeText (context, "Button clicekd: $item",Toast.LENGTH_SHORT).show()
        }
        customRecyclerView!!.adapter = adapter
        val layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
        customRecyclerView.layoutManager = layoutManager

        val listOfCategory = resources.getStringArray(R.array.category);
        var categoryChoiceRecyclerView = view.findViewById<RecyclerView>(R.id.categoryChoiceRecyclerView)
        var categoryAdapter = MyRecyclerViewForCategoryChoice(listOfCategory)
        categoryChoiceRecyclerView!!.adapter = categoryAdapter
        val categoryLayoutManager = GridLayoutManager(this.requireContext(), 2)
        categoryChoiceRecyclerView.layoutManager = categoryLayoutManager


        var listOfTypeSearch =  resources.getStringArray(R.array.SearchType)
        var spinner = view.findViewById<Spinner>(R.id.searchTypeSpinner);
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this.requireContext(),
                android.R.layout.simple_spinner_dropdown_item, listOfTypeSearch
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    Toast.makeText(
                        context, spinner.selectedItem.toString(), Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        val searchBTN = view.findViewById<ImageButton>(R.id.searchBTN)
        searchBTN.setOnClickListener({
            requireActivity().supportFragmentManager.commit {
                replace<SearchResultFragment>(R.id.fragment_container_view)
                setReorderingAllowed(true)
                addToBackStack("name") // name can be null
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
         * @return A new instance of fragment SearchBookFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchBookFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}