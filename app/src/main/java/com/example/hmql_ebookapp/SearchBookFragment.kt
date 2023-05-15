package com.example.hmql_ebookapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

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

    lateinit var categoryList: ArrayList<Category>
    var categoryChoiceList = ArrayList<String>();
    lateinit var categoryChoiceRecyclerView: RecyclerView
    var categoryAdapter = MyRecyclerViewForCategoryChoice(ArrayList<Category>(), null)
    var listOfSearchs = ArrayList<String>()
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

        readJSONFile()
        var customRecyclerView = view.findViewById<RecyclerView>(R.id.recentSearchRecyclerView)
        var adapter = MyRecyclerViewForRecentSearchs(listOfSearchs)
        adapter.onItemClick = { item ->
// do something with your item
            val bundle = Bundle()
            bundle.putString("searchString", item)
            bundle.putStringArrayList("choiceList", ArrayList<String>());
            val fragment = SearchResultFragment()
            fragment.arguments = bundle

            requireActivity().supportFragmentManager.commit {
                replace(R.id.fragment_container_view, fragment)
                setReorderingAllowed(true)
                addToBackStack("name")
            }
            Toast.makeText (context, "Row cliceked: $item", Toast.LENGTH_SHORT).show()
        }
        adapter.onButtonClick = { item ->
// do something with your item
            listOfSearchs.remove(item)
            writeDataToFile()
            adapter.notifyDataSetChanged()
            Toast.makeText (context, "Button cliceked: $item",Toast.LENGTH_SHORT).show()
        }
        customRecyclerView!!.adapter = adapter
        val layoutManager =
            LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)
        customRecyclerView.layoutManager = layoutManager
        categoryChoiceRecyclerView = view.findViewById<RecyclerView>(R.id.categoryChoiceRecyclerView)
        sampleDataInit();

        var autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView);
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
        searchBTN.setOnClickListener {
            if (autoCompleteTextView.text.toString().length > 0) {
                listOfSearchs.add(0, autoCompleteTextView.text.toString());
                writeDataToFile()
            }
            val bundle = Bundle()

            bundle.putString("searchString", autoCompleteTextView.text.toString())
            bundle.putStringArrayList("choiceList", categoryAdapter.getCategoryChoiceList());

            val fragment = SearchResultFragment()
            fragment.arguments = bundle

            requireActivity().supportFragmentManager.commit {
                replace(R.id.fragment_container_view, fragment)
                setReorderingAllowed(true)
                addToBackStack("name")
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

    private fun readJSONFile(){
        try {
            val file = File(requireContext().filesDir, "recentSearchs.json")
            val jsonString = file.readText()

            var recentSearch : recentSearch
            recentSearch = Json.decodeFromString<recentSearch>(jsonString)
            listOfSearchs = recentSearch.searchs
            Toast.makeText(requireContext(), "Data read successfully, ${listOfSearchs.size}", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("Sai j z",e.message.toString())
            Toast.makeText(requireContext(), "Error reading data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun writeDataToFile() {
        try {
            val recentSearchTemp = recentSearch(listOfSearchs)
            val jsonString = Json.encodeToString(recentSearchTemp)
            val file = File(requireContext().filesDir, "recentSearchs.json")
            file.writeText(jsonString)
            Toast.makeText(requireContext(), "Data saved successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.d("Error:/", e.message.toString())
            Toast.makeText(requireContext(), "Error saving data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sampleDataInit() {
        categoryList = ArrayList<Category>()
        val ref1: DatabaseReference = FirebaseDatabase.getInstance().getReference("category")
        ref1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val category = child.getValue(Category::class.java)
                        category?.let { categoryList.add(it) }
                    }
                    Log.d("Books size", "Number of books: ${categoryList.size}")
                    categoryAdapter = MyRecyclerViewForCategoryChoice(categoryList, null)
                    categoryChoiceRecyclerView!!.adapter = categoryAdapter
                    val categoryLayoutManager = GridLayoutManager(context, 2)
                    categoryChoiceRecyclerView.layoutManager = categoryLayoutManager
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Error", "Failed to read value.", error.toException())
            }
        })
    }
}