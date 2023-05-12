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
 * Use the [AdminAuthorsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AdminAuthorsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var listOfAuthor : ArrayList<Author>
    lateinit var authorNameList : Array<String>
    lateinit var authorImgIdList : Array<Int>

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
        return inflater.inflate(R.layout.fragment_admin_authors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backBtn = view.findViewById<Button>(R.id.AdminAuthorBackBtn)
        backBtn.setOnClickListener(){
            requireActivity().supportFragmentManager.popBackStack()
        }

        val authorsRv = view.findViewById<RecyclerView>(R.id.AdminAuthorRecyclerView)
        val authorsRvAdapter = AdminAuthorsRecyclerViewAdapter(listOfAuthor)
        authorsRv.layoutManager = LinearLayoutManager(requireContext())
        authorsRv.adapter = authorsRvAdapter
        authorsRvAdapter.onItemClick = { author ->
            Toast.makeText(requireContext(), author.name.toString(), Toast.LENGTH_SHORT).show()
            val bundle = Bundle()
            bundle.putSerializable("authorDetail", author)
            bundle.putInt("authorIndex", listOfAuthor.indexOf(author))
            val authorDetailFragment = AdminAuthorDetailFragment()
            authorDetailFragment.arguments = bundle
            requireActivity().supportFragmentManager.commit {
                replace(R.id.fragmentContainerView2, authorDetailFragment)
                setReorderingAllowed(true)
                addToBackStack("adminBookDetail")
            }
        }

        setFragmentResultListener("editAuthor") { key, bundle ->
            val newName = bundle.getString("newName")
            val newDesc = bundle.getString("newDesc")
            val newImg = bundle.getInt("newImg")
            val authorIndex = bundle.getInt("authorIndex")
            listOfAuthor[authorIndex].name = newName.toString()
            listOfAuthor[authorIndex].description = newDesc.toString()
            listOfAuthor[authorIndex].img = newImg.toString()
            authorsRvAdapter.notifyDataSetChanged()
        }

        val addBtn = view.findViewById<FloatingActionButton>(R.id.AdminAuthorAddBtn)
        addBtn.setOnClickListener(){
            requireActivity().supportFragmentManager.commit {
                replace<AdminAuthorAddFragment>(R.id.fragmentContainerView2)
                setReorderingAllowed(true)
                addToBackStack("adminAuthorAdd")
            }
        }

        setFragmentResultListener("addAuthor") { key, bundle ->
            val name = bundle.getString("name")
            val desc = bundle.getString("desc")
            val img = bundle.getInt("img")
            val newAuthor = Author(name.toString(), desc.toString(), "")
            listOfAuthor.add(newAuthor)
            authorsRvAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AdminAuthorsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AdminAuthorsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun sampleDataInit() {
        listOfAuthor = arrayListOf<Author>()

        authorNameList = arrayOf(
            "Alice Schertle 1",
            "Alice Schertle 2",
            "Jill McElmurry 1",
            "Bret Bais 1",
            "Liana Moriatory 1",
            "Jill McElmurry 1",
            "Alice Schertle 3",
            "Jill McElmurry 3",
            "Bret Bais 2",
            "Liana Moriatory 5"
        )

        authorImgIdList = arrayOf(
            R.drawable.sampleauthor1,
            R.drawable.sampleauthor2,
            R.drawable.sampleauthor3,
            R.drawable.sampleauthor1,
            R.drawable.sampleauthor2,
            R.drawable.sampleauthor3,
            R.drawable.sampleauthor1,
            R.drawable.sampleauthor2,
            R.drawable.sampleauthor3,
            R.drawable.sampleauthor1
        )

        var sampleDesc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris at nulla pharetra, s" +
                "celerisque turpis ac, elementum ipsum. Sed vel bibendum est. Nunc pulvinar tellus ut libero fringilla, " +
                "finibus feugiat dui bibendum. Donec pellentesque nisl velit, vitae tincidunt risus posuere nec. " +
                "Praesent non mauris vitae orci sollicitudin congue quis quis ex. Sed non nunc sit amet risus molestie porta eget a tortor. " +
                "Cras non justo vel ex fringilla pharetra id ac neque. Etiam magna tellus, convallis vel orci aliquam, commodo malesuada tortor. " +
                "Cras eget lorem sed orci dignissim lobortis. Aliquam erat volutpat. Donec eu felis scelerisque, gravida mauris non, rhoncus arcu."

        for(i in authorNameList.indices) {
            val author = Author(authorNameList[i], sampleDesc, "")
            listOfAuthor.add(author)
        }
    }
}