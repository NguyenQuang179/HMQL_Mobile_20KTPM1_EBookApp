package com.example.hmql_ebookapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView

class MyRecyclerViewForCategoryChoice(
    private val listOfCategory: ArrayList<Category>, private var adapter: MyFilteredBookAdapter?
) : RecyclerView.Adapter<MyRecyclerViewForCategoryChoice.ViewHolder>() {
    var categoryChoiceList = ArrayList<String>()

    @JvmName("setCategoryChoiceList1")
    fun setCategoryChoiceList(array: ArrayList<String>){
        categoryChoiceList = array
        notifyDataSetChanged()
    }

    fun setAdapter2(adapter2: MyFilteredBookAdapter){
        adapter = adapter2
    }

    var onItemClick: ((Category) -> Unit)? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var categoryCheckBox: CheckBox

        init {
            categoryCheckBox = view.findViewById(R.id.categoryCheckBox)

            view.setOnClickListener { onItemClick?.invoke(listOfCategory[adapterPosition]) }

            categoryCheckBox.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val category = listOfCategory[position]
                    if (categoryChoiceList.contains(category.categoryName)) {
                        categoryChoiceList.remove(category.categoryName)
                    } else {
                        categoryChoiceList.add(category.categoryName)
                    }
                    Log.i("Category choice update", categoryChoiceList.size.toString())
                    if (adapter != null){
                        Log.i("In", categoryChoiceList.size.toString());
                        adapter!!.setCategoriesToFilter(categoryChoiceList)
                        Log.i("After in", adapter!!.getCategoriesToFilter().toString())
                        adapter!!.filter.filter(adapter!!.tempString);
                    }
                }
            }
        }
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.category_checkbox, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val category = listOfCategory[position]
        viewHolder.categoryCheckBox.setText(listOfCategory[position].categoryName)
        if (categoryChoiceList.contains(category.categoryName)){
            viewHolder.categoryCheckBox.isChecked = true
        }
//        viewHolder.categoryCheckBox.setOnClickListener({
//            if (categoryChoiceList.contains(category.categoryName)){
//                categoryChoiceList.remove(category.categoryName);
//            }
//            else{
//                categoryChoiceList.add(category.categoryName)
//            }
//            Log.i("Category choice update", categoryChoiceList.size.toString())
//        })
    }

    @JvmName("getCategoryChoiceList1")
    fun getCategoryChoiceList(): ArrayList<String>{
        return this.categoryChoiceList;
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        // filter the list to show only items satisfy the requirements
        return listOfCategory.size
    }
}