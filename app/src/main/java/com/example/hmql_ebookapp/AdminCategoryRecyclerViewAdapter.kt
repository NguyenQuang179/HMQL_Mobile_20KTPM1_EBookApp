package com.example.hmql_ebookapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class AdminCategoryRecyclerViewAdapter(private val categories : ArrayList<Category>)
    : RecyclerView.Adapter<AdminCategoryRecyclerViewAdapter.ViewHolder>() {
    var onItemClick: ((Category) -> Unit)? = null

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val categoryIdTv : TextView = listItemView.findViewById<TextView>(R.id.AdminCategoryIDTv)!!
        val categoryNameTv : TextView = listItemView.findViewById<TextView>(R.id.AdminCategoryNameTv)!!
        val delBtn : Button = listItemView.findViewById<Button>(R.id.AdminCategoryDeleteBtn)!!
        init { listItemView.setOnClickListener { onItemClick?.invoke(categories[adapterPosition]) } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminCategoryRecyclerViewAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val adminCategoryView : View = inflater.inflate(R.layout.admin_category_recycler_view_layout, parent, false)
        return ViewHolder(adminCategoryView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val catergory : Category = categories[position]
//        val student: RealmStudent = listStudentFilter.get(position)
        // Set item views based on your views and data model
        val categoryIdTv = holder.categoryIdTv
        categoryIdTv.text = catergory.categoryID
        val categoryNameTv = holder.categoryNameTv
        categoryNameTv.text = catergory.categoryName
        val delBtn = holder.delBtn

        delBtn.setOnClickListener {
            Toast.makeText(holder.categoryNameTv.context, "Delete Category On Position: " + position, Toast.LENGTH_SHORT).show()
            val ref = FirebaseDatabase.getInstance().getReference("category/${categories[position].categoryID}")
            ref.removeValue()
                .addOnSuccessListener {
                    // Data deleted successfully
                    categories.removeAt(position)
                    this.notifyDataSetChanged()
                }
                .addOnFailureListener {
                    // Handle error
                    println("Error deleting data: ${it.message}")
                }
        }
    }

    override fun getItemCount(): Int = categories.size
}