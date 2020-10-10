package com.talentnest.everything.categoryAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.talentnest.everything.R
import com.talentnest.everything.listener.AdminCategoryListener

import kotlinx.android.synthetic.main.admin_category_item.view.*


class RecyclerViewAdapter(
    private val listener:AdminCategoryListener
) : RecyclerView.Adapter<RecyclerViewAdapter.MyRecyclerViewHolder>() {

    private val categories = CategoryRepository().getListOfCategory()

    inner class MyRecyclerViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_category_item,parent,false)
        return MyRecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRecyclerViewHolder, position: Int) {
        val data = categories[position]
        holder.itemView.iv_category.setImageResource(data.image)
        holder.itemView.setOnClickListener {
            listener.onItemClicked(data)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

}