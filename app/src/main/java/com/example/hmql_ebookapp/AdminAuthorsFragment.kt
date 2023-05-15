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
import com.google.firebase.database.*

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
    lateinit var authorNameList : Array<String>
    lateinit var authorImgIdList : Array<Int>

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
        return inflater.inflate(R.layout.fragment_admin_authors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backBtn = view.findViewById<Button>(R.id.AdminAuthorBackBtn)
        backBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val listOfAuthor = ArrayList<Author>()

        val ref : DatabaseReference = FirebaseDatabase.getInstance().getReference("author")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (child in snapshot.children) {
                        val author = child.getValue(Author::class.java)
                        author?.let { listOfAuthor.add(it) }
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
//                        listOfAuthor[authorIndex].img = newImg.toString()
                        val refTemp = FirebaseDatabase.getInstance().getReference("author/${listOfAuthor[authorIndex].authorID}");
                        refTemp.setValue(listOfAuthor[authorIndex!!])
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
                        val refTemp = FirebaseDatabase.getInstance().getReference("author");
                        val newChild = refTemp.push()
                        val newAuthor = newChild.key?.let { Author(name!!, it, desc!!, "",ArrayList<String>()) }
                        newChild.setValue(newAuthor);
                        listOfAuthor.add(newAuthor!!)
                        authorsRvAdapter.notifyDataSetChanged()
                    }
                }
            }

        // setFragmentResultListener("editAuthor") { key, bundle ->
        //     val newName = bundle.getString("newName")
        //     val newDesc = bundle.getString("newDesc")
        //     val newImg = bundle.getInt("newImg")
        //     val authorIndex = bundle.getInt("authorIndex")
        //     listOfAuthor[authorIndex].name = newName.toString()
        //     listOfAuthor[authorIndex].description = newDesc.toString()
        //     listOfAuthor[authorIndex].img = newImg.toString()
        //     authorsRvAdapter.notifyDataSetChanged()
        // }

        // val addBtn = view.findViewById<FloatingActionButton>(R.id.AdminAuthorAddBtn)
        // addBtn.setOnClickListener {
        //     requireActivity().supportFragmentManager.commit {
        //         replace<AdminAuthorAddFragment>(R.id.fragmentContainerView2)
        //         setReorderingAllowed(true)
        //         addToBackStack("adminAuthorAdd")
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
}